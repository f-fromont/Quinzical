package quinzical.models;

import quinzical.setup.saveInfo;

/**
 * Model that handles all the data processing and changes to do with the user.
 */
public class UserModel {
    private static UserModel _model = new UserModel();
    private int _score = 0;
    private String _userName="user";

    /**
     * Private constructor for singleton pattern
     */
    private UserModel(){};

    /**
     * Returns the single instance of the UserModel.
     * @return UserModel instance
     */
    public static UserModel getInstance(){ return _model; }

    /**
     * Sets the name of the current user.
     * @param userName the name of user
     */
    public void setName(String userName){
        _userName = userName;
    }

    /**
     * Reset the user's score back to 0 for a new game.
     */
    public void restartGame() {
        _score = 0;
    }

    /**
     * Saves the users current score to a file.
     */
    public void saveUser() {
        saveInfo.saveUser(this);
    }

    /**
     * Updates the score of the current user.
     * @param value the value to change the score by
     */
    public void updateScore(int value){
        _score+=value;
    }

    /**
     * Getter methods.
     */
    public int getScore(){
        return _score;
    }
    public String getName(){
        return _userName;
    }
}
