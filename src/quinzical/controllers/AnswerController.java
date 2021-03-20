package quinzical.controllers;
import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import quinzical.models.dataModel.Question;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.GameModuleModel;
import quinzical.models.UserModel;
import quinzical.tts.TextToSpeechController;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



/**
 * Class for controlling the answer view of questions.
 */
public class AnswerController extends AbstractController{
    private GameModuleModel gameModel = GameModuleModel.getInstance();
    private UserModel userModel = UserModel.getInstance();

    private TextToSpeechController tts = new TextToSpeechController();
    private Boolean isPractice;

    @FXML
    private StackPane stack;
    @FXML
    private Label messageLabel;
    @FXML
    private Label correctLabel;
    @FXML
    private Label clue;
    @FXML
    private Button menuButton;
    @FXML
    private Button questionBtn;

    /**
     * Initialise the Question display from a Question object.
     * @param question the question object this view is for
     */
    public void initView(boolean isCorrect, Question question, boolean isPractice){
        menuButton.setTooltip(new QuinzicalTooltip("Return back to the Main Menu"));
        questionBtn.setTooltip(new QuinzicalTooltip("Return back and select another question"));
        //setup background image to be displayed
        addImage(stack,backgroundImage());
        this.isPractice = isPractice;
        if(isCorrect){
            //if the question was answered correctly display this message and say 'correct'
            correctLabel.setText("Correct!");
            messageLabel.setText("Good job");
            runTTS("Correct");
        }
        else {
            //if the question was answered incorrectly display this message and say 'incorrect'
            correctLabel.setText("Incorrect");
            messageLabel.setText("The correct answer was: "+question.getAnswer());
            runTTS("Incorrect");
        }

        if(!isPractice){
            //if the question is not a practice question, then also calculate the score to be added for user
            int questionValue = question.getValue();
            if(!isCorrect){
                questionValue=0;
            }

            userModel.updateScore(questionValue);
            userModel.saveUser();
        }
        if(isPractice) {
            //if it is practice then display the original question/clue that is spoken
            clue.setText("The original clue was: " + question.getQuestion());
            clue.setVisible(true);
        }
    }

    /**
     * Helper function to use text to speech to tell the user if they are wrong or right.
     * @param toSay
     */
    private void runTTS(String toSay){
        tts.speak(toSay);
    }

    /**
     * Changes the view back to either the Category module/Practice module or Reward scene depending on game state/user.
     * @param event
     */
    public void changeToNextScene(ActionEvent event) {
        //checks if its a practice module question or real game module question
        if(!isPractice) {
            //checks if all the available questions have been answered, if so it will change to reward view
            if (gameModel.checkCompletetion()) {
                changeToScene(event,"/quinzical/views/RewardScene.fxml");
            } else {
                changeToScene(event,"/quinzical/views/CategoryView.fxml");
            }
        } else {
            //else it is a practice question and rather than change to game category, we want to change to practice category
            changeToScene(event,"/quinzical/views/PracticeCategoryView.fxml");
        }
    }

}