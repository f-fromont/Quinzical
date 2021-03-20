package quinzical.controllers;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import quinzical.Quinzical;
import quinzical.models.dataModel.Reward;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.LeaderboardModel;
import quinzical.models.RewardModel;


import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * Class is used to represent the score rankings of Quinzical users and Rewards users have unlocked.
 * URLS:
 * Diamond medal- https://www.vecteezy.com/vector-art/82102-diamond-vectors
 * Other Medals -https://www.freepik.com/free-vector/trophies-awards-s-set_5585248.htm#page=1&query=medals&position=3
 */
public class LeaderboardController extends AbstractController implements Initializable {

    @FXML
    private ListView leaderboardView;
    @FXML
    private Label text;
    @FXML
    private Pane medalParticipation;
    @FXML
    private Pane medalBronze;
    @FXML
    private Pane medalSilver;
    @FXML
    private Pane medalGold;
    @FXML
    private Pane medalDiamond;
    @FXML
    private Button returnBtn;
    private LeaderboardModel leaderboardModel = LeaderboardModel.getInstance();
    private ObservableList<String> users;
    @FXML
    private Label unlock;

    /**
     * Initialise the Leaderboard view with the the custom font and setup views.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        unlock.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 19));
        returnBtn.setTooltip(new QuinzicalTooltip("Return back to the Main Menu"));
        text.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 55));
        users = FXCollections.observableArrayList(leaderboardModel.convertToStringView());
        leaderboardView.setItems(users);

        //display the medals if the user has unlocked the medal
        Map<Reward, Boolean> unlockedRewards = RewardModel.getInstance().getRewards();
        for (Reward reward : unlockedRewards.keySet()) {
            if (unlockedRewards.get(reward)) {
                String medalImage;
                //check medal that is unlocked and enable display
                switch (reward.getName()) {
                    case "Participation":
                        medalImage = "-fx-background-image: url(" + reward.getURL() + ") !important";
                        medalParticipation.setStyle(medalImage);
                        Tooltip.install(medalParticipation, new QuinzicalTooltip("You have unlocked the Participation Medal!"));
                        break;

                    case "Bronze":
                        medalImage = "-fx-background-image: url(" + reward.getURL() + ") !important";
                        medalBronze.setStyle(medalImage);
                        Tooltip.install(medalBronze, new QuinzicalTooltip("You have unlocked the Bronze Medal!"));
                        break;

                    case "Silver":
                        medalImage = "-fx-background-image: url(" + reward.getURL() + ") !important";
                        medalSilver.setStyle(medalImage);
                        Tooltip.install(medalSilver, new QuinzicalTooltip("You have unlocked the Silver Medal!"));
                        break;

                    case "Gold":
                        medalImage = "-fx-background-image: url(" + reward.getURL() + ") !important";
                        medalGold.setStyle(medalImage);
                        Tooltip.install(medalGold, new QuinzicalTooltip("You have unlocked the Gold Medal!"));
                        break;

                    case "Diamond":
                        medalImage = "-fx-background-image: url(" + reward.getURL() + ") !important";
                        medalDiamond.setStyle(medalImage);
                        Tooltip.install(medalDiamond, new QuinzicalTooltip("Congratulations on a perfect score!"));
                        break;

                    default:
                        throw new IllegalStateException("Unexpected value: " + reward.getName());
                }
            }
        }
    }
}