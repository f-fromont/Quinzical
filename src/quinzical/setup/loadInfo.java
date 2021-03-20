package quinzical.setup;

import quinzical.models.dataModel.Category;

import quinzical.models.dataModel.Reward;
import quinzical.models.UserModel;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class that manages reading in of files in userData directory for new program instance.
 */
public class loadInfo {

    /**
     * Load the files containing the category information of a given path.
     * @param categoriesPath the pathway that leads to files containing category information to be loaded
     */
    public static List<Category> loadCategories(String categoriesPath) {
        List<Category> categories = new ArrayList<Category>();

        File categoryDir = new File(categoriesPath);
        File[] categoryFiles = categoryDir.listFiles();

        //for each of the files within the directory create a category for
        for (File category : categoryFiles) {
            Category newCategory = new Category(category.getName());

            //try and read the information in the category file to create a categoru from
            try {
                FileInputStream fileInputStream = new FileInputStream(category);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                //read the question files and combine the information across the lines
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    newCategory.addQuestion(line);//add a question for each line in the file
                }
                categories.add(newCategory);
                
                bufferedReader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return categories;
    }

    /**
     * Load the data pertaining to if last time the program was run a game was in progress or not.
     */
    public static boolean loadLastGameState() {
        String gameDataPath = "./.userData/gameData/";

        String gameStatePath = gameDataPath + "gameState.txt";
        File gameState = new File(gameStatePath);

        //as long as the file exists it will read the file and check if it is true or false
        if (gameState.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(gameState);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;
                boolean isCurrentGame = false;
                if ((line = bufferedReader.readLine()) != null) {
                    isCurrentGame = Boolean.parseBoolean(line);
                }
            
                bufferedReader.close();
                return isCurrentGame;
                
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    /**
     * Load the data of what question the user was up to for each category when the program was run last and
     * a game was in progress.
     */
    public static Map<String, Integer> loadQuestionNumber(){
        String gameQuestionNumberPath = "./.userData/gameData/currentQuestions.txt";
        File gameQuestionData = new File(gameQuestionNumberPath);

        Map<String, Integer> questionNumbers = new HashMap<String, Integer>();

        //read the data in from the file contianing the info on the last game in progress save
        if (gameQuestionData.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(gameQuestionData);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] details = line.split("[|]");
                    questionNumbers.put(details[0],Integer.parseInt(details[1]));
                }
                
                bufferedReader.close();
                return questionNumbers;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Load the data of what categories the user had completed when the program was run last and
     * a game was in progress.
     */
    public static Map<String, Boolean> loadCategoryCompletion(){
        String gameCategoryCompletedPath = "./.userData/gameData/currentCategoryCompleted.txt";
        File gameCategoryData = new File(gameCategoryCompletedPath);

        Map<String, Boolean> questionNumbers = new HashMap<String, Boolean>();

        //read the data in from the file contianing the info on the last game in progress save
        if (gameCategoryData.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(gameCategoryData);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] details = line.split("[|]");
                    questionNumbers.put(details[0],Boolean.parseBoolean(details[1]));
                }

                bufferedReader.close();
                return questionNumbers;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * Load the info saved on the last user and set the values for the current user model
     * @param userModel the userModel we would like to set the details for from the file
     */
    public static void loadLastUser(UserModel userModel){
        String userPath = "./.userData/currentUser.txt";
        File userFile = new File(userPath);
        try {
            FileInputStream fileInputStream = new FileInputStream(userFile);
            InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            //read the user file and update userModel
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] details = line.split("[|]");


                if(details.length>1){
                    userModel.setName(details[0]);

                    int score = Integer.parseInt(details[1]);
                    userModel.updateScore(score);
                }
            }
            
            bufferedReader.close();



        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the info on what rewards the user has unlocked.
     */
    public static Map<Reward, Boolean> loadRewards(){
        String rewardPath = "./.userData/rewardState.txt";
        File rewardData = new File(rewardPath);

        Map<Reward, Boolean> rewardMap = new HashMap<Reward, Boolean>();

        //read the data in from the file containing the info on the rewards
        if (rewardData.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(rewardData);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] details = line.split("[|]");
                    Reward newReward = new Reward(details[0],Integer.parseInt(details[1]),details[2]);
                    rewardMap.put(newReward,Boolean.parseBoolean(details[3]));

                }

                bufferedReader.close();
                return rewardMap;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return rewardMap;
    }



    /**
     * Load in the leaderboard data to display the leaderboards of different users.
     */
    public static Map<String, Integer> loadLeaderboard(){
        String leaderboardPath = "./.userData/highScore.txt";
        File leaderboardData = new File(leaderboardPath);

        Map<String, Integer> leaderboard = new HashMap<String, Integer>();

        //read the data in from the file containing the info on the leaderboards
        if (leaderboardData.exists()) {
            try {
                FileInputStream fileInputStream = new FileInputStream(leaderboardData);
                InputStreamReader inputStreamReader = new InputStreamReader(fileInputStream, "UTF-8");
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

                String line;

                while ((line = bufferedReader.readLine()) != null) {
                    String[] details = line.split("[|]");
                    leaderboard.put(details[0],Integer.parseInt(details[1]));

                }

                bufferedReader.close();
                return leaderboard;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return leaderboard;
    }
}
