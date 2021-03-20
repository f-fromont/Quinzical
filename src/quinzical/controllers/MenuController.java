package quinzical.controllers;

import javafx.event.ActionEvent;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import quinzical.Quinzical;
import quinzical.design.QuinzicalTooltip;
import quinzical.models.GameModuleModel;
import quinzical.models.LeaderboardModel;
import quinzical.models.UserModel;

import java.net.URL;
import java.util.ResourceBundle;

/**
 * Class that controls the Menu view, and changing between views from there.
 * URL:
 * NZ Map: https://www.vecteezy.com/vector-art/173294-new-zealand-map
 * Quinzical title font  - http://freakfonts.com/fonts/penumbra-half-serif-std.html
 *
 * This font is used in several other classes for the titles.
 */
public class MenuController extends AbstractController implements Initializable {
    private GameModuleModel gameModel = GameModuleModel.getInstance();
    private UserModel userModel = UserModel.getInstance();
    private LeaderboardModel leaderboardModel = LeaderboardModel.getInstance();

    @FXML
    private Label label;
    @FXML
    private Button gameButton;
    @FXML
    private Button leader;
    @FXML
    private Text gameText;
    @FXML
    private Button settings;
    @FXML
    private Button practise;
    @FXML
    private Text practiseText;
    @FXML
    private Text settingText;
    @FXML
    private Text leaderboardText;

    /**
     * Initialise the fonts and the tooltips of buttons and labels in the Main Menu.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        label.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 72));
        gameText.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 16));
        practiseText.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 16));
        settingText.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 16));
        leaderboardText.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 16));
        gameButton.setTooltip(new QuinzicalTooltip("This is the Main Mode! Answer questions about New Zealand, earn Rewards and try beat other users in the Leaderboard!"));
        practise.setTooltip(new QuinzicalTooltip("This is the place to Practise questions to improve whatever section you choose about New Zealand, there's no scoring or rewards in this mode."));
        settings.setTooltip(new QuinzicalTooltip("Settings allows you to change the speed of the text reader, if you would like it faster or slower"));
        leader.setTooltip(new QuinzicalTooltip("Compare your scores with other users! As well as view all the rewards you've unlocked!"));
    }
    /**
     *The changeToModule method is called whenever the 'Games Module' or 'Practice Module' button is pressed in the
     * main menu. It then loads the CategoryView Scene and displays it.
     * @param event
     */
    public void changeToGameModule(ActionEvent event){
        //checks if all the available questions have been answered, if so it will change to reward view
        if(gameModel.checkCompletetion()){
            changeToRewardView(event);
        } else {
            changeToScene(event,"/quinzical/views/CategoryView.fxml");
        }
    }

    /**
     * This method is called when the game is finished and all questions have been asked, and will change the view
     * to the reward screen.
     * @param event
     */
    public void changeToRewardView(ActionEvent event){
        changeToScene(event,"/quinzical/views/RewardScene.fxml");

        //add a new user record to the highscore screen
        leaderboardModel.addNewUser(userModel);

        //after reward screen is initialised the game/user models can be reset for a new game and saved
        gameModel.newGameStart();
        userModel.restartGame();
        //save game state
        gameModel.saveCurrentCategories();
        userModel.saveUser();
        leaderboardModel.saveLeaderboard();
    }

    /**
     *This method acts as an EventHandler to the PracticeModule button in the Menu, when pressed, we go into the
     * category view for the PractiseModule.
     * @param event
     */
    public void changeToPracticeModule(ActionEvent event) {
        changeToScene(event,"/quinzical/views/PracticeCategoryView.fxml");
    }

    /**
     *The changeToSettings method is called whenever the Setting button in the Main Menu is pressed. It then loads
     * the SettingsView Scene and displays it.
     * @param event
     */
    public void changeToSettings(ActionEvent event){
        changeToScene(event,"/quinzical/views/SettingView.fxml");
    }

    /**
     *The changeToLeaderboards method is called whenever the Leaderboard button in the Main Menu is pressed. It then loads
     * the Leaderboard Scene and displays it.
     * @param event
     */
    public void changeToLeaderboard(ActionEvent event){
        changeToScene(event,"/quinzical/views/LeaderboardView.fxml");
    }
}

