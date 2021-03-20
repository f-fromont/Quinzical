package quinzical.controllers;

import javafx.concurrent.WorkerStateEvent;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import quinzical.models.dataModel.Question;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.PractiseModuleModel;

import quinzical.tts.TextToSpeechController;

/**
 * Controller class with extended and overrided parts of the QuestionController for the differences in question being
 * asked when its a practice question.
 * URLS:
 * In addition to those already mentioned:
 * Try again - https://www.pngwing.com/en/free-png-bkorw
 */
public class PracticeQuestionController extends QuestionController {
    private PractiseModuleModel practiceModule = PractiseModuleModel.getInstance();
    private Question currentPracticeQuestion;
    private TextToSpeechController tts = new TextToSpeechController();

    @FXML
    private Label practiceQuestionLabel;
    @FXML
    private TextField userAnswer;
    @FXML
    private Button tryAgain;
    @FXML
    private Button returnBtn;
    @FXML
    private Button submit;
    @FXML
    private Button replayQuestion;
    @FXML
    private Label remainingQ;
    @FXML
    private StackPane practiseStack;
    @FXML
    private Button macronA;
    @FXML
    private Button macronE;
    @FXML
    private Button macronI;
    @FXML
    private Button macronO;
    @FXML
    private Button macronU;

    /**
     * Replays the question if the user pressed the button to hear the clue spoken out.
     */
    public void replayPracticeClue() {
        tts.speak(currentPracticeQuestion.getQuestion());
    }

    /**
     * Initiate the practice view by grabbing a question and asking that question to the user.
     * @param categories
     */
    public void initPracticeView(ListView categories) {
        //load tool tips for all the buttons
        submit.setTooltip(new QuinzicalTooltip("Submit your answer"));
        replayQuestion.setTooltip(new QuinzicalTooltip("Replay the speech of the question"));
        returnBtn.setTooltip(new QuinzicalTooltip("Return to the Menu"));
        tryAgain.setTooltip(new QuinzicalTooltip("Try the question again"));

        //add background image and intialise the rest of the information on the screen
        addImage(practiseStack,backgroundImage());
        currentPracticeQuestion = practiceModule.initQuestion(categories);
        practiceQuestionLabel.setText(currentPracticeQuestion.getQuestion());
        remainingQ.setText("You have " + practiceModule.getPracticeCount() + " attempt(s) remaining");

        initMacronBtn();

        tts.speak(currentPracticeQuestion.getQuestion());
        tts.getAudio().setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                //disable buttons until the question is finished speaking
                submit.setDisable(false);
                replayQuestion.setDisable(false);
            }
        });
    }



    /**
     *  Resets the question if the user wants to try again.
     */
    public void resetPracticeQuestion() {
        practiceQuestionLabel.setText(currentPracticeQuestion.getQuestion());
        tryAgain.setVisible(false);
        submit.setVisible(true);
        replayQuestion.setVisible(true);
        returnBtn.setVisible(false);
        macronA.setDisable(false);
        macronE.setDisable(false);
        macronI.setDisable(false);
        macronO.setDisable(false);
        macronU.setDisable(false);
        //Here if the user has 1 more attempt we display the first letter of the answer.
        if(practiceModule.getPracticeCount() == 1) {
            userAnswer.setText(practiceModule.displayFirstLetter(currentPracticeQuestion));
        }
    }


    /**
     * Checks whether the answer was correct or not in the Practise Module.
     * @param event
     */
    public void checkPracticeAnswer(ActionEvent event) {
        String response = userAnswer.getText();
        //If it is not correct we tell the user it is incorrect and ask them if they want to try again.
        if (!currentPracticeQuestion.checkAnswerCorrect(response)) {
            practiceModule.setPracticeCount(practiceModule.getPracticeCount()-1);
            practiceQuestionLabel.setText("Incorrect!");
            tryAgain.setVisible(true);
            submit.setVisible(false);
            replayQuestion.setVisible(false);
            returnBtn.setVisible(true);
            tts.getAudio().stopSpeaking();
            remainingQ.setText("You have " + practiceModule.getPracticeCount() + " attempt(s) remaining");
            macronA.setDisable(true);
            macronE.setDisable(true);
            macronI.setDisable(true);
            macronO.setDisable(true);
            macronU.setDisable(true);
            //If the user has no more attempts we move to the answer scene.
            if(practiceModule.getPracticeCount() == 0) {
                changeToAnswer(event);
            }
        }
        //If the user is correct we move to the answer scene.
        else {
            changeToAnswer(event);
        }
    }

    /**
     * Here we change to the PracticeAnswerScene.
     * @param event
     */
    @Override
    public void changeToAnswer(ActionEvent event) {
        //Gets the response of the user.
        String response = userAnswer.getText();
        //Checks the response of the user, whether it was correct or incorrect.
        boolean isCorrect = currentPracticeQuestion.checkAnswerCorrect(response);
        //Stops the tts from speaking when we move into the next scene.
        tts.getAudio().stopSpeaking();
        //Reset the attempts to 3.
        practiceModule.setPracticeCount(3);

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/quinzical/views/AnswerView.fxml"));
            Parent AnswerView = loader.load();
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene AnswerScene = new Scene(AnswerView,window.getWidth(),window.getHeight()-37);

            AnswerController controller = loader.getController();
            //We load the view, and set the labels in the view, depending on whether the user answered the questions
            //correctly or incorrectly.
            controller.initView(isCorrect, currentPracticeQuestion, true);

            //We move into the Answer scene.

            window.setScene(AnswerScene);
            window.show();


        } catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Return to menu with the reset of the practice module count.
     * @param event
     */
    public void practiseReturnToMenu(ActionEvent event) {
        practiceModule.setPracticeCount(3);
        returnToMenu(event);
    }

}
