package quinzical.tts;

import java.io.*;

public class AudioPlayer extends javafx.concurrent.Task{
    private String text;
    private String speed;
    private String voiceName;
    private Process playSpeech;
    private String ttsPath = "src/quinzical/tts/SpeechParameters.scm";
    private File ttsSpeed = new File(ttsPath);
    public AudioPlayer(String Text,double Speed,String VoiceName) {
        text = Text;
        //Speed is inversed, as 2 is half the speed rather than double the speed.
        speed = Double.toString(1/Speed);
        voiceName = VoiceName;
    }

    /**
     * We output the audio in a seperate thread with run()
     */
    @Override
    protected Object call(){
        stopSpeaking();
        setSpeed();
        setText();
        createSpeech();
        playSpeech();
        deleteFiles();
        resetSchemeFile();
        return null;
    }

    /**
     * The method createSpeech() will create a wav file with the default speed of 1.
     */
    public void setSpeed(){
        String cmd = "(Parameter.set 'Duration_Stretch " + speed+")";
        try {
            FileWriter writer = new FileWriter(ttsSpeed,true);
            PrintWriter speedWriter = new PrintWriter(writer);
            speedWriter.println(cmd);
            speedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets the text in the file Text.txt, which contains the speech that will be played to the user.
     */
    public void setText() {
        try {
            FileWriter writer = new FileWriter(new File("src/quinzical/tts/Text.txt"));
            PrintWriter speedWriter = new PrintWriter(writer);
            speedWriter.println(text);
            speedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create a wav file of the speech to be played to the user using a bash command.
     */
    public void createSpeech() {
        String cmd = "text2wave -o src/quinzical/tts/speech.wav src/quinzical/tts/Text.txt -eval "+ ttsPath;
        ProcessBuilder buildSpeech = new ProcessBuilder("bash","-c", cmd);
        try {
            Process process = buildSpeech.start();
            process.waitFor();
            process.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Plays the speech to the user using a bash command.
     */
    public void playSpeech() {
        String cmd2 = "aplay src/quinzical/tts/speech.wav";
        ProcessBuilder speech = new ProcessBuilder("bash","-c",cmd2);
        try {
            playSpeech = speech.start();
            playSpeech.waitFor();
            playSpeech.destroy();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     *The method deleteFiles() will delete the wav files used to speak the text, after
     *  the clip has been played.
     */
    public void deleteFiles() {
        File original = new File("src/quinzical/tts/speech.wav");
        original.delete();
    }

    /**
     * Resets the scheme file by removing the speed value, but retaining the type of voice to be selected.
     */
    public void resetSchemeFile() {
        try {
            FileWriter writer = new FileWriter(ttsSpeed);
            PrintWriter speedWriter = new PrintWriter(writer);
            speedWriter.println(voiceName);
            speedWriter.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Return the current clip
     */
    public Process getClip() {
        return playSpeech;
    }

    /**
     * Stops the previous speaker if speech is currently playing.
     */
    public void stopSpeaking() {
        if (playSpeech != null) {
            this.getClip().destroy();
        }
    }


}