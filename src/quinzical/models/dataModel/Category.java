package quinzical.models.dataModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A Category object will hold a list of question objects and has utility for handling questions.
 */
public class Category {
    private List<Question> questions = new ArrayList<Question>();
    private String name;
    private boolean isComplete;

    /**
     * Cbreate a specific category with a name and the directory that holds the files containing the questions.
     * @param name - name the category will be called.
     */
    public Category(String name) {
        this.name = name;
    }

    /**
     * Create a new category object with a specific set of questions.
     * @param categoryToCopy the category that the constructor will copy.
     * @param questionsToCopy the list of questions to copy over.
     */
    public Category(Category categoryToCopy, List<Question> questionsToCopy) {
        name =categoryToCopy.getName();
        questions.addAll(questionsToCopy);
    }
    /**
     * Creates a question from a line of string.
     * @param questionString
     */
    public void addQuestion(String questionString) {
        String questionSection = null, questionType = null, answer = null;
        boolean available = true;
        int value = 0;
        String[] details = questionString.split("[|]+");

        questionSection = details[0];//question is the first section
        questionType = details[1];//question type is the second section within ()
        answer = details[2];//answer is the third section
        available = Boolean.parseBoolean(details[3]);//boolean parsed that represents if the question is available, or if already asked
        value = Integer.parseInt(details[4]);

        Question newQuestion = new Question(questionSection,questionType,answer,available,value,this);//create a question objects representing the question
        if(newQuestion.checkValidQuestion()) {
            questions.add(newQuestion);//add question if it is valid
        }
        else{
            System.out.println("Question Invalid format: "+questionString);
        }
    }

    /**
     * Checks all the questions if any are still available (haven't been chosen yet)
     * if any are available, return false.
     */
    public boolean isComplete() {
        if(isComplete) {
            return true;
        } else {
            isComplete =true;
            for(Question question: questions) {
                if(question.isAvailable()) {
                    isComplete =false;
                    break;
                }
            }
        }
        return isComplete;

    }

    /**
     * Resets the questions to being available.
     */
    public void resetQuestions(){
        for(Question question: questions){
            question.resetQuestion();
        }
    }

    /**
     * Getter Methods
     */
    public String getName() {
        return name;
    }
    public List<Question> getQuestions(){
        return questions;
    }

    /**
     * specific overriden toString method for correct displaying of categories in list view
     */
    @Override
    public String toString() {
        return name;
    }

}
