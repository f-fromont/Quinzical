package quinzical.models;

import javafx.scene.control.ListView;


import quinzical.models.dataModel.Category;
import quinzical.models.dataModel.Question;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PractiseModuleModel {
    private static PractiseModuleModel model = new PractiseModuleModel();
    private int practiceCount = 3;
    private List<Category> allCategories;

    /**
     * Returns the PractiseModuleModel object.
     */
    public static PractiseModuleModel getInstance() {
        return model;
    }

    /**
     * From a list of categories, determine what the user has selected and return a random question from that category.
     * @param categories
     */
    public Question initQuestion(ListView categories) {
        Object selectedCategory = categories.getSelectionModel().getSelectedItem();
        List<Question> questionList = ((Category)selectedCategory).getQuestions();
        Collections.shuffle(questionList);
        return questionList.get(0);
    }

    /**
     * Displays the first letter of the answer to the user once 2 attempts have been used.
     * @param question
     */
    public String displayFirstLetter(Question question) {
        String Answer = question.getAnswer().strip();
        return String.valueOf(Answer.charAt(0));
    }

    /**
     * Loads in all the categories for the PractiseCategoryView.
     * @param categoriesToLoad
     */
    public void loadCategories(List<Category> categoriesToLoad){
        allCategories = new ArrayList<>(categoriesToLoad);
    }

    /**
     * Sets the number of attempts the user has.
     * @param count
     */
    public void setPracticeCount(int count) {
        practiceCount = count;
    }

    //Getter methods
    public List<Category> getCategories() {
        return allCategories;
    }
    public int getPracticeCount() {
        return practiceCount;
    }
}
