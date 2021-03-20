package quinzical.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import quinzical.Quinzical;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.PractiseModuleModel;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class for controlling the practice category view and displaying the categories the user can select.
 */
public class PracticeCategoryController extends AbstractController implements Initializable {
    private PractiseModuleModel practiceModel = PractiseModuleModel.getInstance();

    @FXML
    private Label categoryText;
    @FXML
    private Button returnBtn;
    @FXML
    private Button submit;
    @FXML
    private ListView CategoryList;

    /**
     * Populates the list of categories inside of PracticeCategoryView.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        submit.setTooltip(new QuinzicalTooltip("Submit the category and answer a random question"));
        returnBtn.setTooltip(new QuinzicalTooltip("Return back to the Main Menu"));
        categoryText.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 48));

        for (int i = 0; i < practiceModel.getCategories().size(); i++) {
            CategoryList.getItems().add(practiceModel.getCategories().get(i));
        }
    }

    /**
     * Will change the current scene to a question view for a practice mode, with a random question from a selected category
     * @param event
     */
    public void changeToPracticeQuestion(ActionEvent event) {
        try {
            if (CategoryList.getSelectionModel().getSelectedItems().size() > 0) {
                FXMLLoader loader = new FXMLLoader();
                loader.setLocation(getClass().getResource("/quinzical/views/PracticeQuestionView.fxml"));
                Parent PracticeQuestionView = loader.load();
                Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene PracticeQuestionScene = new Scene(PracticeQuestionView,window.getWidth(),window.getHeight()-37);

                //initialise the practice question view with the category list
                PracticeQuestionController controller = loader.getController();
                controller.initPracticeView(CategoryList);

                window.setScene(PracticeQuestionScene);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
