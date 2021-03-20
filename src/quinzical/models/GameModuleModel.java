package quinzical.models;

import quinzical.models.dataModel.Category;

import quinzical.models.dataModel.Question;
import quinzical.setup.loadInfo;
import quinzical.setup.saveInfo;

import java.util.*;

/**
 * Model that contains all the data and logic handling for running a game mode of quinzical.
 */
public class GameModuleModel {
    private static GameModuleModel model = new GameModuleModel();
    private List<Category> currentCategories;
    private List<Category> allCategories;
    private Category internationalCategory;
    private Category currentSubsetInternationalCategory;
    private Map<String, Integer> currentQuestion;
    private Map<String, Boolean> completedCategories;
    private boolean gameInPlay = false;

    final private int NUM_OF_CATEGORIES=5;
    final private int NUM_OF_QUESTIONS=5;

    /**
     * Private constructor for a singleton pattern (single instance of the model created).
     */
    private GameModuleModel(){}

    /**
     * Returns the single instance to any object that calls.
     */
    public static GameModuleModel getInstance(){
        return model;
    }

    /**
     * Intialises the game model with a list of categories that are read in from a file.
     * @param categories the categories read in from a previous game.
     * @param international
     */
    public void initGameModel(List<Category> categories, Category international){
        loadCategories(categories);
        internationalCategory =international;

        //loads if last time the program was run a game was in progress
        gameInPlay = loadInfo.loadLastGameState();

        if(!gameInPlay){
            newGameStart();//if no game was in progress, start a new one, otherwise load in game data
        } else {
            currentCategories =loadInfo.loadCategories("./.userData/gameData/currentCategories/");
            currentSubsetInternationalCategory =loadInfo.loadCategories("./.userData/gameData/currentInternational/").get(0);
            currentQuestion =loadInfo.loadQuestionNumber();
            completedCategories =loadInfo.loadCategoryCompletion();
        }
    }

    /**
     * Loads categories into a model for a possible pool of categories that can be pulled from to be used.
     * @param categoriesToLoad all the possible categories that can be selected.
     */
    private void loadCategories(List<Category> categoriesToLoad){
        allCategories = new ArrayList<Category>(categoriesToLoad);
    }

    /**
     * Sets up all the information required for a new game to be run with random categories/questions for the user to answer.
     */
    public void newGameStart(){
        currentCategories = new ArrayList<Category>();
        Collections.shuffle(allCategories);

        //Adds 5 categories from a randomly shuffled category list
        for(int i=0;i<NUM_OF_CATEGORIES;i++){
            Category categoryToChange = allCategories.get(i);
            categoryToChange.resetQuestions();
            Category categoryToAdd = createNewSubsetCategories(categoryToChange);
            currentCategories.add(categoryToAdd);
        }
        //create a subset of the international category with only a limited number of random questions
        currentSubsetInternationalCategory = createNewSubsetCategories(internationalCategory);

        gameInPlay =true;
        initCategoryMap();
        saveCurrentCategories();
    }

    /**
     * Sets up category hashmaps that are used to keep track of current question and also the state of whether a
     * category is complete or not.
     */
    private void initCategoryMap(){
        //stores a number(Integer) representing which question the user is up to for a category(String)
        currentQuestion = new HashMap<String, Integer>();
        completedCategories = new HashMap<String, Boolean>();
        for(Category category: currentCategories){
            currentQuestion.put(category.getName(),0);
            completedCategories.put(category.getName(), false);
        }
        //add international section
        currentQuestion.put(internationalCategory.getName(),0);
        completedCategories.put(internationalCategory.getName(),false);
    }

    /**
     * Creates a subset of a category with 5 randomly chosen questions from within the parent category.
     * @param category parent category we are creating a subset from.
     */
    private Category createNewSubsetCategories(Category category){
        //this will create a list that contains the questions in a category and shuffle it in a random order
        List<Question> questions = category.getQuestions();
        Collections.shuffle(questions);

        List<Question> randomSelectedQuestions = new ArrayList<Question>();

        //the first however many questions are selected from that random order to produce randomised questions
        for(int i=0;i<NUM_OF_QUESTIONS;i++){
            if(i<questions.size()) {
                questions.get(i).resetQuestion();
                randomSelectedQuestions.add(questions.get(i));
            }
        }
        //each question gets a value assigned to it
        assignQuestionValues(randomSelectedQuestions);

        //return a subset category with the list of random questions
        return new Category(category,randomSelectedQuestions);
    }

    /**
     * Assigns values to the questions in the given list in an ascending order.
     * @param questions the questions to assign values to.
     */
    private void assignQuestionValues(List<Question> questions){
        int i=1;
        for(Question question: questions){
            int value = i*100;
            question.setQuestionValue(value);
            i++;
        }
    }

    /**
     * Increases the number which shows which question the user is up to for a specific category.
     * @param category the category to increase the question count for
     */
    public void increaseCurrentQuestionForCategory(Category category){
        String categoryName = category.getName();
        //if the category name is in the current question then it will get that category
        int count = currentQuestion.containsKey(categoryName) ? currentQuestion.get(categoryName) : null;
        currentQuestion.put(categoryName, count+1);//increase the current question number for that category
    }

    /**
     * Update the map to check through all the categories if it is all complete.
     */
    private void updateCategoryCompleteness(){
        for(Category category: currentCategories){
            updateCategoryComplete(category.getName());
        }
    }

    /**
     * Updates the map if a category is complete or not, true if category is complete, false otherwise.
     * @param categoryName string name of the category to update.
     */
    public void updateCategoryComplete(String categoryName){
        Category category;
        if(completedCategories.containsKey(categoryName)){
            category=getSpecificCategory(categoryName);
            completedCategories.put(categoryName,category.isComplete());
        }
    }

    /**
     * Checks if at least 2 categories are finished so that international section becomes opened.
     */
    public boolean isInternationalOpen(){
        int categoryCompletetionCount = 0;

        for(Boolean value: completedCategories.values()){
            if(value==true){
                categoryCompletetionCount++;
            }
        }
        if(categoryCompletetionCount>1){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Returns the current number of the questions the user is up to for a given category.
     * @param category the category to check which question number they are up to.
     */
    public int getCurrentQuestionNumber(Category category) {
        int count = currentQuestion.get(category.getName());
        return count;
    }

    /**
     * Helper function to find category when passed in a category name.
     * @param categoryName the string name of the category to find
     */
    private Category getSpecificCategory(String categoryName){
        if(categoryName.equals(currentSubsetInternationalCategory.getName())){
            return currentSubsetInternationalCategory;
        }
        for(Category category: currentCategories){
            if (categoryName.equals(category.getName())){
                return category;
            }
        }
        return null;
    }

    /**
     * Checks through all the current categories in play if any are not finished
     * the method will return false, it will return true if all categories are complete.
     * @return true if categories are complete, false if unanswered questions remain.
     */
    public boolean checkCompletetion(){
        boolean isGameFinished = true;
        for(Boolean value: completedCategories.values()){
            if(value==false){
                isGameFinished=false;
                break;
            }
        }

        saveInfo.setGameState(!isGameFinished);
        return isGameFinished;
    }

    /**
     * Calls a static helper class to save the model information into files.
     */
    public void saveCurrentCategories(){
        updateCategoryCompleteness();
        saveInfo.saveGameData();
    }

    //GETTER METHODS
    public List<Category> getCategories(){ return currentCategories; }
    public Category getInternationalCategory(){ return currentSubsetInternationalCategory; }
    public Map<String, Integer> getQuestionNumbers() { return currentQuestion; }
    public Map<String, Boolean> getCategoryFinished() { return completedCategories; }
}
