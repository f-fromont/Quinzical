package quinzical.controllers;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import quinzical.models.GameModuleModel;
import quinzical.models.LeaderboardModel;
import quinzical.models.UserModel;

import java.io.File;

/**
 * Class representing a pop-up form window for the user to fill out their information upon completeing a gameModule.
 */
public class userNameForm {
    private static TextField nameSubmission;
    private static Stage formWindow;

    /**
     * Method called when the window is required.
     * userForm background image url: 'http://www.pinterest.nz/pin/699113542134819171'
     */
    public static void display()
    {
        formWindow = new Stage();
        formWindow.initModality(Modality.APPLICATION_MODAL);
        formWindow.setTitle("Name Submission");

        Label text = new Label("Please enter your name:");
        nameSubmission = new TextField();

        Button submitBtn = new Button("Submit");
        submitBtn.setOnAction(e->submissionHandler());

        //create VBOX to house the different componenets
        VBox layout = new VBox(10);
        layout.getChildren().addAll(text, nameSubmission,submitBtn);
        layout.setAlignment(Pos.CENTER);

        Scene scene = new Scene(layout,400,300);

        //load stylesheet with graphic details for the user form
        scene.getStylesheets().add((new File("src/quinzical/resources/stylesheets/Form.css")).toURI().toString());

        formWindow.setScene(scene);

        formWindow.showAndWait();
    }

    /**
     * Submission handler for when the user presses submit and has included their details.
     */
    private static void submissionHandler(){
        //load models which require information and processing
        GameModuleModel gameModel = GameModuleModel.getInstance();
        LeaderboardModel leaderboardModel = LeaderboardModel.getInstance();
        UserModel userModel = UserModel.getInstance();

        //set the users name they entered
        userModel.setName(nameSubmission.getText());

        //add the user as a new entry on leaderboard
        leaderboardModel.addNewUser(userModel);

        //trigger a restart of the models
        gameModel.newGameStart();
        userModel.restartGame();

        //save the information and state of the game
        gameModel.saveCurrentCategories();
        userModel.saveUser();
        leaderboardModel.saveLeaderboard();

        formWindow.close();
    }
}
