package quinzical.models.dataModel;

/**
 * Class that represents the data of a reward object with info pertaining to its details,
 * name - name of the reward
 * score - required score to unlock this reward
 * url - string location of where the image file for the reward is kept.
 */
public class Reward {

    private String name;
    private Integer score;
    private String url;

    /**
     * Construct a reward object with the following fields.
     * @param name the name of the medal/reward object.
     * @param score the score required to unlock the medal/reward.
     * @param url the url that paths to the image of the medal/reward.
     */
    public Reward(String name, Integer score, String url){
        this.name =name;
        this.score =score;
        this.url =url;
    }

    /**
     * Getter Methods.
     */
    public String getName(){
        return name;
    }
    public Integer getScore(){
        return score;
    }
    public String getURL(){
        return url;
    }
}
