package quinzical.controllers;


import javafx.collections.FXCollections;


import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import quinzical.Quinzical;
import quinzical.design.QuinzicalTooltip;
import quinzical.tts.TextToSpeechController;
import quinzical.tts.FestivalVoice;

import java.net.URL;
import java.util.ResourceBundle;


/**
 * Class that controls the Settings view, and the related buttons inside.
 * URLs:
 * Play Button - https://www.pngitem.com/middle/iTToTwb_green-transparent-play-button-hd-png-download/
 * Return To Menu -https://dryicons.com/icon/return-arrow-icon-12647
 * Reset - https://commons.wikimedia.org/wiki/File:Flat_restart_icon.svg
 * Settings icon - https://www.clipartmax.com/middle/m2H7K9d3A0G6A0A0_love-rc-crawlers-buggies-or-planes-settings-button-png/
 * Settings Fonts - https://befonts.com/coves-font.html
 */
public class SettingController extends AbstractController implements Initializable {
    @FXML
    private Slider SpeedSlider;
    private TextToSpeechController tts = new TextToSpeechController();
    @FXML
    private Button testSpeed;
    @FXML
    private Text title;
    @FXML
    private Button reset;
    @FXML
    private Button returnBtn;
    @FXML
    private Text quinzical;
    @FXML
    private ChoiceBox voiceList;
    @FXML
    private Button confirm;

    /**
     * Resets the Speed of the slider back to its original value.
     */
    public void resetSliderValue() {
        SpeedSlider.setValue(1.0);
        tts.setSpeed(1.0);
    }

    /**
     * Changes the current used voice to the selected voice.
     */
    public void setVoice() {
       FestivalVoice voice = (FestivalVoice) voiceList.getSelectionModel().getSelectedItem();
       tts.setVoice(voice.getName());
    }

    /**
     * Returns the value of the slider after the user has dragged or pressed the slider to adjust the speed of the
     * text.
     */
    public void getSliderValue() {
        double sliderValue = SpeedSlider.getValue();
        tts.setSpeed(sliderValue);
    }

    /**
     *Says 'Welcome to Quinzical' with the current speed the user has selected.
     */
    public void testSpeed() {
        tts.speak("Welcome to Quinzical");
    }
    /**
     * Initialise the setting view with all the custom fonts and tooltips and the speed/voices installed.
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        confirm.setTooltip(new QuinzicalTooltip("If you want to change the voice, press the confirm button"));
        title.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/CovesLight.otf").toExternalForm(), 48));
        quinzical.setFont(Font.loadFont(Quinzical.class.getResource("/quinzical/resources/fonts/Penumbra-HalfSerif-Std_35114.ttf").toExternalForm(), 72));
        testSpeed.setTooltip(new QuinzicalTooltip("Test the speed of the text, this will play a sample line for you."));
        reset.setTooltip(new QuinzicalTooltip("Reset the speed of the Text Reader to the default value of 1"));
        returnBtn.setTooltip(new QuinzicalTooltip("Return to the Main Menu"));
        SpeedSlider.setTooltip(new QuinzicalTooltip("1 represents the default speed, 0.5 half the default speed, and 2 twice the default speed"));

        //sets the speed of the shown tts speed and loads in voices
        SpeedSlider.setValue(tts.getSpeed());
        voiceList.setItems(FXCollections.observableArrayList(tts.getVoices()));
        voiceList.getSelectionModel().select(tts.startVoice());
    }

}
