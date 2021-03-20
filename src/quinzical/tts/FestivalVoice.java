package quinzical.tts;

public class FestivalVoice {
    private String displayName;
    private String name;

    /**
     * Creates a FestivalVoice object that takes in a string and sets the name of this object(As a string).
     */
    public FestivalVoice(String Name) {
        name = Name;
        displayName = Name;
        defaultNames();
    }

    /**
     * This method overrides the two default voices display Name to strings more easily understandable to the
     * user.
     */
    public void defaultNames() {
        if (name.equals("akl_nz_jdt_diphone")) {
            displayName = "NZ Voice";
        }
        if (name.equals("kal_diphone")) {
            displayName = "Default Voice";
        }
    }

    /**
     * Getter method.
     */
    public String getName() {
        return name;
    }

    /**
     * Specific overridden toString method for correct displaying of categories in choice box.
     */
    @Override
    public String toString() {
        return displayName;
    }

}

