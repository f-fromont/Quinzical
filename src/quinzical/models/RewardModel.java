package quinzical.models;

import quinzical.models.dataModel.Reward;
import quinzical.setup.saveInfo;

import java.util.*;

/**
 * Class pertaining to all the logic and handling of rewards for a program instance.
 */
public class RewardModel {
    private static RewardModel model = new RewardModel();
    private List<Reward> orderedRewards;
    private Map<Reward, Boolean> userRewards;

    /**
     * Private constructor for singleton pattern.
     */
    private RewardModel(){};

    /**
     * Returns single instance of reward model.
     */
    public static RewardModel getInstance(){
        return model;
    }

    /**
     * Initialises the the reward model with the map of rewards and booleans if unlocked.
     * @param loadedMap the map of rewards/boolean states read from the files.
     */
    public void initRewardModel(Map<Reward,Boolean> loadedMap){
        userRewards =loadedMap;
        orderedList();
    }

    /**
     * Orders the rewards in a list based on the score of each medal/reward.
     */
    private void orderedList(){
        List<Reward> orderedRewards = new LinkedList<Reward>(userRewards.keySet());

        //custom sort for an ascending order from highest score user to lowest
        orderedRewards.sort(new Comparator<Reward>() {
            @Override
            public int compare(Reward o1, Reward o2) {
                return  o1.getScore().compareTo(o2.getScore());
            }
        });
        this.orderedRewards =orderedRewards;

    }

    /**
     * Calculates what reward the player deserves for that game.
     * @param score the users score in finishing a game
     * @return The reward the user has unlocked
     */
    public Reward calculateRewardMedal(int score){
        Reward medalWon= orderedRewards.get(0);
        for(Reward reward: orderedRewards){
            if(score>=reward.getScore()){
                medalWon=reward;
            }
        }
        userRewards.put(medalWon, true);
        saveRewards();

        return medalWon;
    }

    /**
     * Save the current state into file.
     */
    public void saveRewards(){
        saveInfo.saveRewardState(this);
    }

    //Getter method
    public Map<Reward, Boolean> getRewards(){
        return userRewards;
    }
}

