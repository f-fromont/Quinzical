package quinzical.controllers;
import javafx.event.ActionEvent;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import quinzical.Quinzical;
import quinzical.models.dataModel.Category;
import quinzical.models.dataModel.Question;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.GameModuleModel;
import quinzical.models.UserModel;
import quinzical.setup.saveInfo;


import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;


/**
 * Class for controlling the category view and displaying category questions.
 * URLS
 * Reset Game - https://www.kindpng.com/imgv/iwJbbxh_reset-button-png-transparent-png/
 */
public class CategoryController extends AbstractController implements Initializable {
    private GameModuleModel gameModel = GameModuleModel.getInstance();
    private UserModel userModel = UserModel.getInstance();

    @FXML
    private HBox CategoryBox;
    @FXML
    private Label ScoreLabel;
    @FXML
    private Button ResetButton;
    @FXML
    private HBox internationalHBox;
    @FXML
    private Button returnBtn;
    @FXML
    private Label header;

    /**
     * Initialises the category view to have all the required information for a game view,
     * where it will display the questions/categories currently in play.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        header.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 45));
        returnBtn.setTooltip(new QuinzicalTooltip("Return back to the Main Menu"));
        ResetButton.setTooltip(new QuinzicalTooltip("Restart Game"));

        //sets up views with categories from gameModel
        List<Category> categories = gameModel.getCategories();
        Category internationalCategory = gameModel.getInternationalCategory();

        //displays users current score
        Integer score = userModel.getScore();
        ScoreLabel.setText("Score: "+score.toString());

        CategoryBox.getChildren().clear();
        CategoryBox.setSpacing(15);

        //for each of the categories dynamically create a container that holds all the questions
        for (Category category : categories) {
            VBox newCategoryBox = createCategory(category,true);
            CategoryBox.getChildren().add(newCategoryBox);
        }
        //add International category section to display
        internationalHBox.getChildren().clear();
        VBox internationalBox = createCategory(internationalCategory, gameModel.isInternationalOpen());
        internationalHBox.getChildren().add(internationalBox);

        ScoreLabel.setVisible(true);
        ResetButton.setVisible(true);
    }

    /**
     * Method for dynamically setting up all the question buttons inside a category.
     * @param category the category to set the view up for
     * @return a VBox containing all the buttons of the questions inside
     */
    private VBox createCategory(Category category, boolean toDisplay) {
        VBox newCategoryBox = new VBox();
        //Settings for display of VBox
        newCategoryBox.setSpacing(12);
        newCategoryBox.setAlignment(Pos.CENTER);

        //list that contains all the buttons that will need to be stored in newCategoryBox
        List<Button> btnList = new ArrayList<Button>();
        //list of questions that will be represented by buttons
        List<Question> questions = category.getQuestions();

        int questionNumber = 0;
        for (Question question : questions) {
            Button questionBtn = new Button(question.getStringValue());
            questionBtn.setPrefWidth(100);

            //Seting up button handling so that each button will trigger a change of view with data for that question
            questionBtn.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    changeToQuestion(actionEvent, question);
                }
            });

            btnList.add(questionBtn);

            //calculates if the user can select this questionBtn
            boolean userAllowedToAnswer = questionNumber== gameModel.getCurrentQuestionNumber(category);
            if (!question.isAvailable()||!userAllowedToAnswer||!toDisplay) {
                //if question is not available/user not up to it or not to be displyed, disable button
                questionBtn.setDisable(true);

                if(!question.isAvailable()) {//checks if question has already been answered and change colour
                    questionBtn.setStyle("-fx-background-color: rgba(0,255,0,0.8)");
                } else if(!toDisplay){//used to disable international categories and change colour
                    questionBtn.setStyle("-fx-background-color: rgba(250,100,50,0.9)");
                }
            }
            else {
                questionBtn.setDisable(false);
            }
            questionNumber++;
        }

        //Adds label to top of VBox with name of category
        Label categoryTitle = new Label(category.getName());
        categoryTitle.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesBold.otf").toExternalForm(), 16));

        newCategoryBox.getChildren().add(categoryTitle);

        //Adds all the buttons into the Vbox
        newCategoryBox.getChildren().addAll(btnList);

        return newCategoryBox;
    }


    /**
     * Called when the game is to be reset, this requires the userModel and gameModel to reset their data,
     * and the new states to be saved into file form, and finally the game view will be re-initialised.
     */
    public void resetGame(){
        gameModel.newGameStart();
        userModel.restartGame();

        saveInfo.saveGameData();
        saveInfo.saveUser(userModel);

        initialize(null,null);
    }

    /**
     * Will change the current scene to a question view with the corresponding question data initialised.
     * @param event the event thrown(user pressing quesiton button)
     * @param question the question the users selected
     */
    public void changeToQuestion(ActionEvent event, Question question) {
        try {
            FXMLLoader loader = new FXMLLoader();

            loader.setLocation(getClass().getResource("/quinzical/views/QuestionView.fxml"));
            Parent QuestionView = loader.load();
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene QuestionScene = new Scene(QuestionView,window.getWidth(),window.getHeight()-37);

            //load a view with a particular question
            QuestionController controller = loader.getController();
            controller.initView(question);

            //updates the gameModel so it keeps track of which questions have been asked
            gameModel.increaseCurrentQuestionForCategory(question.getCategory());

            window.getWidth();
            window.setScene(QuestionScene);//temp scene for testing
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}