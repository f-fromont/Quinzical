package quinzical.controllers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import quinzical.Quinzical;
import quinzical.models.dataModel.Reward;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.RewardModel;
import quinzical.models.UserModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * class that controls the reward view, displayed at the end of the game
 */
public class RewardController extends AbstractController implements Initializable {
    UserModel userModel = UserModel.getInstance();
    RewardModel rewardModel = RewardModel.getInstance();
    Reward userReward;
    @FXML
    private Label scoreLabel;
    @FXML
    private Label medalLabel;
    @FXML
    private Pane rewardPane;
    @FXML
    private Button returnBtn;
    @FXML
    private Label congratulationsLabel;


    /**
     * We add styling formats to the Reward Scene when we initialise it.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        congratulationsLabel.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 50));
        scoreLabel.setText(String.valueOf(userModel.getScore()));
        scoreLabel.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 50));
        userReward = rewardModel.calculateRewardMedal(userModel.getScore());
        medalLabel.setText(userReward.getName()+" Medal");

        String rewardImage = "-fx-background-image: url(" + userReward.getURL() +")";
        rewardPane.setStyle(rewardImage);
        returnBtn.setTooltip(new QuinzicalTooltip("Return to Main Menu"));

        userNameForm.display();
    }
}
