package quinzical.setup;

import quinzical.models.dataModel.Category;

import quinzical.models.dataModel.Question;
import quinzical.models.dataModel.Reward;
import quinzical.models.GameModuleModel;
import quinzical.models.LeaderboardModel;
import quinzical.models.RewardModel;
import quinzical.models.UserModel;

import java.io.*;
import java.util.Map;

/**
 * Helper class for saving info of current program state into files.
 */
public class saveInfo {
    private static GameModuleModel _gameModel = GameModuleModel.getInstance();

    /**
     * Save the data of the gameModel, so that next time the program starts it can easily be restored to last time.
     */
    public static void saveGameData() {
        String gameDataPath = "./.userData/gameData/";
        File gameData = new File(gameDataPath);


        if (gameData.exists()) {
            String gameCategoryPath = "./.userData/gameData/currentCategories/";
            try {
                //clear the directory of older game data
                cleanDir(gameCategoryPath);

                //for each category save a file that has all the question details
                saveGameCategories(gameCategoryPath);

                //a game is in progress if this is called so the state must be set to true
                setGameState(true);

                //following section is for saving international questions into a file
                saveInternational();

                //write to the file the info on the current question number the user is up to for each category
                saveQuestionNumberData();

                //Following section is for saving which categories are completed file
                saveCategoryCompletetion();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Helper method for deleting all the files in a given path.
     * @param dir the pathway to clean up in string format
     */
    private static void cleanDir(String dir){
        File dirFile = new File(dir);
        File[] files = dirFile.listFiles();

        for(File category: files){
            category.delete();
        }
    }

    /**
     * Encapsulates the implementation of saving all the categories currently in a game into a file form.
     * @param gameCategoryPath - path of the folder to save categories to
     */
    private static void saveGameCategories(String gameCategoryPath) throws IOException {
        for (Category category : _gameModel.getCategories()) {
            String newCategorySavePath = gameCategoryPath + "/" + category.getName();
            File newCategorySave = new File(newCategorySavePath);
            if (newCategorySave.createNewFile()) {
                FileWriter writer = new FileWriter(newCategorySave);

                //write a line for each question details in a category
                for (Question question : category.getQuestions()) {
                    String line = question.display() + "\n";
                    writer.write(line);

                }
                writer.close();
            }
        }
    }

    /**
     * Encapsulates the saving of the international questions section into file form.
     */
    private static void saveInternational() throws IOException {
        String internationalPath = "./.userData/gameData/currentInternational/International";
        File internationalData = new File(internationalPath);

        FileWriter writerInternational = new FileWriter(internationalData);

        for (Question question : _gameModel.getInternationalCategory().getQuestions()){
            String line = question.display() + "\n";
            writerInternational.write(line);
        }
        writerInternational.close();
    }

    /**
     * Encapsulates the saving of the question number the user is up to for each category in file form.
     */
    private static void saveQuestionNumberData() throws IOException {
        String gameQuestionNumberPath = "./.userData/gameData/currentQuestions.txt";
        File gameQuestionData = new File(gameQuestionNumberPath);
        FileWriter writer = new FileWriter(gameQuestionNumberPath);
        Map<String, Integer> questionNumbers = _gameModel.getQuestionNumbers();
        for(String key: questionNumbers.keySet()){
            writer.write(key+"|"+questionNumbers.get(key)+"\n");
        }
        writer.close();
    }

    /**
     * Encapsulates the saving of which categories have been completed by the user in file form.
     */
    private static void saveCategoryCompletetion() throws IOException {
        String gameCategoryFinishedPath = "./.userData/gameData/currentCategoryCompleted.txt";
        File gameCategoryData = new File(gameCategoryFinishedPath);

        //write to the file the info on each category finished for user
        FileWriter categoryWriter = new FileWriter(gameCategoryFinishedPath);
        Map<String, Boolean> categoryFinished = _gameModel.getCategoryFinished();
        for(String key: categoryFinished.keySet()){
            categoryWriter.write(key+"|"+categoryFinished.get(key)+"\n");
        }
        categoryWriter.close();
    }

    /**
     * Saves the current game state if a game is in progress or not.
     * @param state the state of game to save
     */
    public static void setGameState(boolean state) {
        try {
            String gameStatePath = "./.userData/gameData/gameState.txt";
            File gameState = new File(gameStatePath);
            FileWriter writer = new FileWriter(gameState);

            String line = String.valueOf(state);
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current users data(score/name) to a file so it can easily be used next program start up.
     * @param model the userModel to save the data of.
     */
    public static void saveUser(UserModel model) {
        try {
            String userPath = "./.userData/currentUser.txt";
            File userFile = new File(userPath);
            FileWriter writer = new FileWriter(userFile);

            String line = model.getName()+"|"+model.getScore();
            writer.write(line);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current reward state rewards/boolean state to a file to be read later.
     * @param model the rewardModel to save the data of.
     */
    public static void saveRewardState(RewardModel model){
        try{
            String rewardPath = "./.userData/rewardState.txt";
            File rewardData = new File(rewardPath);


            //write to the file the info on each reward
            FileWriter writer = new FileWriter(rewardPath);
            Map<Reward, Boolean> reward = model.getRewards();
            for(Reward key: reward.keySet()){
                String output= key.getName()+"|"+key.getScore().toString()+"|"+key.getURL()+"|"+reward.get(key)+"\n";
                writer.write(output);
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Saves the current leaderboard users/score into file form for persistence.
     * @param model the leaderboardModel to save the data of.
     */
    public static void saveLeaderboards(LeaderboardModel model) {
        try{
            String leaderboardPath = "./.userData/highScore.txt";
            File leaderboardData = new File(leaderboardPath);

            //write to the file the info on each user/score in leaderboard
            FileWriter categoryWriter = new FileWriter(leaderboardPath);
            Map<String, Integer> leaderboard = model.getLeaderboard();
            for(String key: leaderboard.keySet()){
                categoryWriter.write(key+"|"+leaderboard.get(key)+"\n");
            }
            categoryWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
