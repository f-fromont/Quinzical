package quinzical.models;

import quinzical.setup.saveInfo;

import java.util.*;

/**
 * Model containing data and logic handling for leaderboard system.
 */
public class LeaderboardModel {
    private static LeaderboardModel model = new LeaderboardModel();
    private Map<String, Integer> userScoreMap;

    /**
     * Private constructor for singleton pattern.
     */
    private LeaderboardModel(){};

    /**
     * Returns single instance of leaderboard model.
     */
    public static LeaderboardModel getInstance(){
        return model;
    }

    /**
     * Initialises the the leaderboard model with the map of users/scores read in from files.
     */
    public void initLeaderboardModel(Map<String,Integer> loadedMap){
        userScoreMap =loadedMap;
    }

    /**
     * Takes the unordered map of the leaderboard users and orders it before returning as a list of strings
     * that can be displayed on a screen/view.
     */
    public List<String> convertToStringView(){
        List<String> orderedUsers = new ArrayList<String>();
        List<Map.Entry<String, Integer>> userList = new LinkedList<>(userScoreMap.entrySet());

        //custom sort for a descending order from highest score user to lowest
        Collections.sort(userList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> o1, Map.Entry<String, Integer> o2) {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        int i=1;
        //creates a string for each user with 1)Position, 2) username 3)score
        for (Map.Entry<String, Integer> entry: userList){
            String userScorePair = i+".| "+entry.getKey()+" | Score: "+entry.getValue();
            orderedUsers.add(userScorePair);
            i++;
        }
        return orderedUsers;
    }

    /**
     * Adds a new user to the highscore board whenever a user finishes a game.
     * @param user the new user to be added to the leaderboard.
     */
    public void addNewUser(UserModel user){
        userScoreMap.put(user.getName(), user.getScore());
    }

    /**
     * Call to save the current state into file.
     */
    public void saveLeaderboard(){
        saveInfo.saveLeaderboards(this);
    }

    /**
     * Returns the leaderboard information of users and their scores
     */
    public Map<String, Integer> getLeaderboard(){
        return userScoreMap;
    }
}
