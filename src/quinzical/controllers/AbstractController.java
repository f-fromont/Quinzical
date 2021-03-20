package quinzical.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Abstract class used to provide common methods for the other controllers for a method used often (returnToMenu)
 */
abstract class AbstractController {

    /**
     * Provides implementation for changing the view back to the menu view and can be inherited and used by any
     * controller classes that extend this abstract class.
     * @param actionEvent
     */
    public void returnToMenu(ActionEvent actionEvent){
        changeToScene(actionEvent,"/quinzical/views/MenuView.fxml");
    }

    /**
     * Provides a useful method for switching a scene to the view from a given view url.
     * @param actionEvent
     * @param url - pathway to the view file the scene is being changed to
     */
    public void changeToScene(ActionEvent actionEvent, String url){
        try {
            Parent View = FXMLLoader.load(getClass().getResource(url));
            Stage window = (Stage) ((Node) actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(View,window.getWidth(),window.getHeight()-37);


            window.setScene(scene);
            window.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *  Helper function to select from a list of background images a random image to be displayed for the background.
     *  URL of Backgrounds:
     *  Boat - https://wallpaperaccess.com/minimalist-sea
     *  Mountain - https://wallpaperaccess.com/minimalist-mountain
     *  Big Mountain -https://www.lefthudson.com/wp-content/uploads/2019/11/minimalist-mountain-wallpaper-hd-inspirational-mountain-forest-landscape-minimalist-4k-2590-of-the-day-of-minimalist-mountain-wallpaper-hd.jpg
     *  Winter -  https://www.vecteezy.com/vector-art/641294-vector-winter-horizontal-landscape-with-snow-capped-hills-and-triangle-coniferous-trees-cartoon-illustration
     */

    public String backgroundImage() {
        List<String> backgroundImages = new ArrayList<>();
        backgroundImages.add("/quinzical/resources/backgrounds/winter.png");
        backgroundImages.add("/quinzical/resources/backgrounds/boat.jpg");
        backgroundImages.add("/quinzical/resources/backgrounds/mountain.jpg");
        backgroundImages.add("/quinzical/resources/backgrounds/bigmountain.jpg");
        Collections.shuffle(backgroundImages);
        //select which ever image is first after being randomly shuffled
        String file = backgroundImages.get(0);
        String backgroundImage = "-fx-background-image: url(" + file +")";
        return backgroundImage;
    }

    /**
     * Adds the image to the background.
     * @param stack
     * @param text
     */
    public void addImage(StackPane stack, String text) {
        stack.setStyle(text);
    }
}
