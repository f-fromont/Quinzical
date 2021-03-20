package quinzical.tts;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TextToSpeechController {
    //We create private fields, the AudioPlayer to play the text and the thread to run the AudioPlayer on.
    private quinzical.tts.AudioPlayer tts;
    private Thread thread;
    private List<FestivalVoice> voiceList = new ArrayList<>();

    /**
     * Transform text to speech with the given Speech.
     * @param text text to transform
     */
    public void speak(String text) {
        //Generate Audio for the specified text and the current speed.
        tts = new quinzical.tts.AudioPlayer(text, getSpeed(),getVoice());
        //Create a new thread to play the speech.
        thread = new Thread(tts);
        //Set Daemon so that the thread will close when the GUI is closed.
        //thread.setDaemon(true);
        thread.start();
    }

    /**
     * Read in the speed text file and assign the speed to that value.
     */
    public double getSpeed() {
        String ttsPath = ".userData/ttsSpeed.txt";
        File ttsSpeed = new File(ttsPath);
        try {
            BufferedReader ttsReader = new BufferedReader(new FileReader(ttsSpeed));
            //Read in the first, and only line, which is the speed of the slider.
            String ttsSpeedValue = ttsReader.readLine();
            double speed = Double.parseDouble(ttsSpeedValue);
            ttsReader.close();
            return speed;
        } catch (IOException e) {
            e.printStackTrace();
        }
        //If there is an issue with reading in the ttsSpeed value we return the default value 1.
        return 1;
    }

    /**
     * Return the field thread that plays the AudioPlayer.
     */
    public Thread getThread() {
        return thread;
    }

    /**
     * Sets the speed by writing to the ttsSpeed.txt file, takes the input of the slider in the settings page.
     * @param speed
     */
    public void setSpeed(double speed) {
        String ttsPath = ".userData/ttsSpeed.txt";
        File ttsSpeed = new File(ttsPath);
        FileWriter writer;
        try {
            writer = new FileWriter(ttsPath);
            writer.write(Double.toString(speed));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Gets all the avaliable voices in the usr/share/festival/voices/english path, if the user does not have
     * festival installed in the path they cannot add new festival voices, however they can use the default ones, as
     * method will add the default values to the list if they haven't already been added.
     *
     * Returns the list of all voices avaliable for the user to use.
     */
    public List<FestivalVoice> getVoices() {
        String[] voiceNames;
        File file = new File("//usr/share/festival/voices/english");
        try {
            //check to see if voices exist in correct directory
            voiceNames = file.list();
            for (int i = 0; i < voiceNames.length; i++) {
                voiceList.add(new FestivalVoice(voiceNames[i]));
            }
        } catch (NullPointerException e){
            System.out.println("Can't find installed festival voices");
        }
        boolean defaultVoice = false;
        boolean nzVoice = false;
        for (FestivalVoice voice : voiceList) {
            if (voice.getName().equals("kal_diphone")) {
                defaultVoice = true;
            }
            if (voice.getName().equals("akl_nz_jdt_diphone")) {
                nzVoice = true;
            }
        }
        if (!defaultVoice) {
            voiceList.add(new FestivalVoice("kal_diphone"));
        }
        if (!nzVoice) {
            voiceList.add(new FestivalVoice("akl_nz_jdt_diphone"));
        }
        return voiceList;
    }

    /**
     * Gets the Voice to be played to the user.
     */
    public String getVoice() {
        String ttsPath = "src/quinzical/tts/SpeechParameters.scm";
        File ttsSpeed = new File(ttsPath);
        try {
            BufferedReader ttsReader = new BufferedReader(new FileReader(ttsSpeed));
            String ttsSpeedValue = ttsReader.readLine();
            return ttsSpeedValue;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "(voice_kal_diphone)";
    }

    /**
     * Sets the voice that the user selects by pressing the confirm button in the settings page.
     * @param voice
     */
    public void setVoice(String voice) {
        String path = "src/quinzical/tts/SpeechParameters.scm";
        try {
            FileWriter writer = new FileWriter(path);
            PrintWriter speedWriter = new PrintWriter(writer);
            speedWriter.println("(voice_"+voice+")");
            speedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Upon loading in the settings page, this method selects the voice the user last picked(either after exiting
     * the game or just leaving the scene) in the choicebox.
     */
    public int startVoice() {
        String voiceName = getVoice();
        for(FestivalVoice voice:voiceList) {
            if (voiceName.contains(voice.getName())) {
                return voiceList.indexOf(voice);
            }
        }
        //If there is an error of the file, return the first voice.
        return 0;
    }

    /**
     * Returns the current AudioPlayer.
     */
    public AudioPlayer getAudio() {
        return tts;
    }
}

