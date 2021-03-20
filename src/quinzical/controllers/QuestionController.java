package quinzical.controllers;


import javafx.beans.value.ChangeListener;

import javafx.beans.value.ObservableValue;
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
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import quinzical.models.dataModel.Question;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.GameModuleModel;
import quinzical.timer.Timer;
import quinzical.tts.TextToSpeechController;
import java.util.*;

/**
 * Class that controls the question view, for whenever a question is asked.
 * URLS:
 * Submit: https://www.flaticon.com/free-icon/right-arrow-button_1225
 * Don't Know: https://www.pngwing.com/en/free-png-nwmqo
 * Replay has already been atrributed in settings.
 */
public class QuestionController extends AbstractController{
    private GameModuleModel model = GameModuleModel.getInstance();
    private Question currentQuestion;
    private List<String> backgroundImages = new ArrayList<>();
    private TextToSpeechController tts = new TextToSpeechController();
    private Timer timer = new Timer();

    @FXML
    private Label timerLabel;
    @FXML
    private TextField userAnswer;
    @FXML
    private Button gameSubmit;
    @FXML
    private Button replayGameQuestion;
    @FXML
    private Button dontKnowBtn;
    @FXML
    private StackPane stack;
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
     * Initialise the Question display from a Question object.
     * @param question the question object this view is for
     */
    public void initView(Question question){
        gameSubmit.setTooltip(new QuinzicalTooltip("Submit your answer"));
        dontKnowBtn.setTooltip(new QuinzicalTooltip("Dont know the answer? Then click here to proceed(will take whatever answer you have typed.)"));
        replayGameQuestion.setTooltip(new QuinzicalTooltip("Replay the speech of the question."));
        currentQuestion = question;
        addImage(stack,backgroundImage());
        initMacronBtn();
        runTasks();
    }

    /**
     * Sets up the button handling for the macron buttons to add macrons to the users answer.
     */
    public void initMacronBtn(){
        macronA.setOnAction(e->addMacron("ā"));
        macronE.setOnAction(e->addMacron("ē"));
        macronI.setOnAction(e->addMacron("ī"));
        macronO.setOnAction(e->addMacron("ō"));
        macronU.setOnAction(e->addMacron("ū"));
    }

    /**
     * Run background tasks when we initiate the view in the GameModule.
     */
    private void runTasks() {
        //Speak the question to the user.
        tts.speak(currentQuestion.getQuestion());
        //Create a new Timer to countdown for use.
        tts.getAudio().setOnSucceeded(new EventHandler<WorkerStateEvent>() {
            @Override
            public void handle(WorkerStateEvent workerStateEvent) {
                replayGameQuestion.setDisable(false);
                gameSubmit.setDisable(false);
                dontKnowBtn.setDisable(false);

                //Create a thread to run the timer
                Thread timerThread = new Thread(timer);
                timerThread.setDaemon(true);
                //Start the thread
                timerThread.start();
                //Update the Timer Label as we count down.
                timer.messageProperty().addListener(new ChangeListener<String>() {
                    @Override
                    public void changed(ObservableValue<? extends String> observableValue, String s, String newValue) {
                        timerLabel.setText(newValue);
                        //If the timer reaches 0 we fire the submit button to go to the answer scene.
                        if(Integer.valueOf(newValue) == 0) {
                            gameSubmit.fire();
                        }
                    }
                });
            }
        });
    }

    /**
     * Replays the question in practice and game module.
     */
    public void replayQuestionClue() {
        tts.speak(currentQuestion.getQuestion());
    }

    /**
     * Add the given macron to the users answer.
     * @param macron the macron string to add to the users answer
     */
    public void addMacron(String macron){
        String stringAnswer = userAnswer.getText();
        userAnswer.setText(stringAnswer+macron);
    }

    /**
     * Event Handler for the Submit button in Games Module. We proceed to the Answer Scene.
     * @param event
     */
    public void changeToAnswer(ActionEvent event) {
        //We get the response of the user.
        String response = userAnswer.getText();
        //We check to see if the user's answer was correct or incorrect.
        boolean isCorrect = currentQuestion.checkAnswerCorrect(response);
        //update the model category to check whether it is finished or not
        model.updateCategoryComplete(currentQuestion.getCategory().toString());

        //We stop the tts from speaking, as well as the timer.
        tts.getAudio().stopSpeaking();
        timer.cancel();

        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/quinzical/views/AnswerView.fxml"));
            Parent AnswerView = loader.load();
            Stage window = (Stage) ((Node)event.getSource()).getScene().getWindow();
            Scene AnswerScene = new Scene(AnswerView,window.getWidth(), window.getHeight()-37);

            AnswerController controller = loader.getController();
            //We load in the AnswerView, and set the labels messages depending on whether the user answered the question
            //correctly or not.
            controller.initView(isCorrect, currentQuestion, false);
            model.saveCurrentCategories();
            //We move into the Answer Scene.

            window.setScene(AnswerScene);
            window.show();
        } catch(Exception e){
            e.printStackTrace();
        }
    }
}