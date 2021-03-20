package quinzical;

import javafx.application.Application;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import quinzical.models.*;
import quinzical.models.dataModel.Category;
import quinzical.setup.ProgramSetup;
import quinzical.setup.loadInfo;

/**
 * Main class for handling start up of program, this involves setup of file hierarchy structure and then loading and reading
 * of saved data in files for setup of models.
 */
public class Quinzical extends Application {
    //We create instance fields of the Game, Practice and User Model to load in the categories and previous user info,
    //and store the info into is respective objects.
    private final GameModuleModel gameModel = GameModuleModel.getInstance();
    private final PractiseModuleModel practiceModel = PractiseModuleModel.getInstance();
    private final UserModel userModel = UserModel.getInstance();
    private final LeaderboardModel leaderboardModel = LeaderboardModel.getInstance();
    private final RewardModel rewardModel = RewardModel.getInstance();

    @Override
    public void start(Stage primaryStage) throws Exception {
        ProgramSetup.setupStructure();
        //Load the categories into PractiseModule and GameModule.
        practiceModel.loadCategories(loadInfo.loadCategories("./.userData/categories/"));

        Category InternationalCategory = loadInfo.loadCategories("./.userData/international").get(0);
        gameModel.initGameModel(loadInfo.loadCategories("./.userData/categories/"),InternationalCategory);


        //Load the info of the last user (score/name) if there was a user
        loadInfo.loadLastUser(userModel);

        //load the info of the leaderboard (users|score) if there are any present
        leaderboardModel.initLeaderboardModel(loadInfo.loadLeaderboard());

        //load the info of the unlocked rewards if any have been
        rewardModel.initRewardModel(loadInfo.loadRewards());

        //We set the Stage title to be Quinzical, and load the MenuView fxml file, the home screen, and set a new scene.
        primaryStage.setTitle("Quinzical");

        FXMLLoader loader = new FXMLLoader();

        loader.setLocation(getClass().getResource("/quinzical/views/MenuView.fxml"));
        Parent MenuView = loader.load();
        Scene MenuScene = new Scene(MenuView,800,800);


        primaryStage.setScene(MenuScene);
        primaryStage.show();
    }

    /**
     * starts the program
     */
    public static void main(String[] args) {
        launch(args);
    }
}
