package quinzical.setup;

import java.io.*;


/**
 * Helper class that handles setting up all the program directory when it starts up so files can be saved/read correctly.
 */
public class ProgramSetup {

    /**
     * Sets up the directory/file structure, called at the start of the program.
     */
    public static void setupStructure() {
        //create a new userData directory
        String dataPath = "./.userData/";
        File userDataDir= new File(dataPath);
        boolean isDirMade = userDataDir.mkdir();
        //if the directory was made successfully then start creating all the internal directories/files
        if(isDirMade) {
           try {
                //create a file to hold the speed of the tts
               setupTtsSpeed();
               //create a file to hold the details of the last user
               setupUserScore();
               //create a file to hold the highscore of program
               setupHighScore();
               //create a file to hold the reward data
               setupRewardState();
               //create a file to hold the gameData of a program instance
               setupGameModelData();
               //create a directory to hold the categories
               if(setupCategoriesDir()){
                   //create categories/question files
                   String questionPath = "./Quinzical.txt";
                   readQuestionData(questionPath, "./.userData/categories/");
               }
               if(setupInternationalDir()){
                   String internationalPath = "./Quinzical-International.txt";
                   readQuestionData(internationalPath, "./.userData/international/");
               }

               else{
                   System.out.println("Error in creating new category dir");
               }
           } catch (IOException e){
               e.printStackTrace();
           }
        }else {
        }
        setUpTemp();
    }

    /**
     * The method setUpTemp() will create a folder called temp if one does not exist.
     * This folder is used to store wav files from the tts.
     */
    private static void setUpTemp()  {
        String dataPath = "./temp/";
        File userDataDir= new File(dataPath);
        userDataDir.mkdir();
    }
    /**
     * The method setupTtsSpeed() will check whether a ttsSpeed.txt file is in .userData, if not
     * it will create one and set the speed to the default speed,1.
     */
    private static void setupTtsSpeed() throws IOException {
        String ttsPath = ".userData/ttsSpeed.txt";
        File ttsSpeed = new File(ttsPath);
        if(ttsSpeed.createNewFile()) {
            FileWriter writer = new FileWriter(ttsPath);
            writer.write("1");
            writer.close();
        }
    }

    /**
     * Setup a file with default values for a users score.
     */
    private static void setupUserScore() throws IOException {
        String scorePath = "./.userData/currentUser.txt";
        File scoreData= new File(scorePath);
        if(scoreData.createNewFile()) {
            FileWriter writer = new FileWriter(scorePath);
            writer.write("User|0");
            writer.close();
        }
    }

    /**
     * Setup a file for holding user highscores.
     */
    private static void setupHighScore() throws IOException {
        String highScorePath = "./.userData/highScore.txt";
        File highScoreData= new File(highScorePath);
        if(highScoreData.createNewFile()) {
        }
    }

    /**
     * Setup a file for holding what rewards the user has unlocked.
     */
    private static void setupRewardState() throws IOException{
        String rewardStatePath = "./.userData/rewardState.txt";
        File rewardData = new File(rewardStatePath);
        if(rewardData.createNewFile()){
            FileWriter writer = new FileWriter(rewardStatePath);
            writer.write("Participation|0|/quinzical/resources/medals/participation.png|false\n");
            writer.write("Bronze|1500|/quinzical/resources/medals/bronze.png|false\n");
            writer.write("Silver|5000|/quinzical/resources/medals/silver.png|false\n");
            writer.write("Gold|7500|/quinzical/resources/medals/gold.png|false\n");
            writer.write("Diamond|9000|/quinzical/resources/medals/diamonds.png|false\n");
            writer.close();
        }
    }

    /**
     * Setup the internal directory that holds the gameModel data for a program instance if a game was in progress
     * so that it can be read at the next program start up and the game could be resumed.
     */
    private static void setupGameModelData() throws IOException {
        String gameDataPath = "./.userData/gameData/";
        File gameData = new File(gameDataPath);
        if(gameData.mkdir()) {

            //creates a folder for the current categories to be held in
            String gameCategoryPath = "./.userData/gameData/currentCategories/";
            File gameCategoryData = new File(gameCategoryPath);
            if(gameCategoryData.mkdir()) {
            }

            //creates a folder for the current internationalQuestion
            String internationalPath = "./.userData/gameData/currentInternational";
            File internationalData = new File(internationalPath);
            if(internationalData.mkdir()){}

            //creates file for holding the currentQuestionNumber the user is up to for a category
            String gameQuestionNumberPath = "./.userData/gameData/currentQuestions.txt";
            File gameQuestionData = new File(gameQuestionNumberPath);
            if(gameQuestionData.createNewFile()){}

            //creates file for holding the currentCategoryFinished the user has for all categories
            String gameCategoryFinishedPath ="./.userData/gameData/currentCategoryCompleted.txt";
            File gameCategoryFinishedData = new File(gameCategoryFinishedPath);
            if(gameCategoryFinishedData.createNewFile()){}

            //creates file for holding if a game was in progress or not last time
            String gameStatePath = "./.userData/gameData/gameState.txt";
            File gameStateData = new File(gameStatePath);
            if(gameStateData.createNewFile()){}
        }
    }

    /**
     * Set up folder for all the current categories loaded for the program, as oppposed to an in progress game.
     */
    private static boolean setupCategoriesDir() {
        String categoryPath = "./.userData/categories/";
        File categoryDir= new File(categoryPath);
        return categoryDir.mkdir();
    }

    /**
     * Set up folder for all the international categories loaded for the program.
     */
    private static boolean setupInternationalDir(){
        String categoryPath = "./.userData/international/";
        File categoryDir= new File(categoryPath);
        return categoryDir.mkdir();
    }

    /**
     * Read the question data stored in a text file kept in the working directory of the program,
     * and create categories/questions in a form that is easy to use by the program.
     */
    private static void readQuestionData(String questionPath, String newCategoryPath) {
        File questionData = new File(questionPath);

        if(questionData.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(questionData);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //read the category file, and begin seperating into different lines
                String line;
                String newFilePath;
                File newCategory=null;

                while ((line = bufferedReader.readLine()) != null) {
                    line.trim();

                    if(!line.isEmpty()) {
                        line += "|true|0";//add a detail to questions that will be used to determine if question has been asked
                        String[] details = line.split("[|]+");

                        if (details.length==3) {
                            newFilePath = newCategoryPath + details[0];
                            newCategory = new File(newFilePath);
                        } else {
                            FileWriter writer = new FileWriter(newCategory,true);
                            line += "\n";
                            writer.write(line);//write the details into the new file
                            writer.close();
                        }
                    }
                }
                
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else {
            System.out.println("Error Quinzical.txt file not present in working directory, please add it in");
        }
    }
}
