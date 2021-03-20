package quinzical.models.dataModel;


import java.util.ArrayList;
import java.util.List;

/**
 * Question object holds all the data needed for a question to be in an easy to use form for the Quinzical program.
 */
public class Question {
    private int value =0;
    private String question;
    private String questionType;
    private List<String> answers = new ArrayList<String>();
    private boolean isAvailable;
    private Category category;

    /**
     * Create a question object in a specific category.
     * @param question the actual string of the question to be asked.
     * @param answer the correct answer for the question.
     * @param available boolean that represents if the question has been asked or not and if it is available.
     * @param category the category the question is from.
     */
    public Question(String question,String questionType, String answer, boolean available, int value, Category category) {
        this.question=question;
        this.questionType=questionType;
        this.readAnswers(answer);
        this.isAvailable=available;
        this.value =value;
        this.category =category;
    }

    /**
     * Used to read an answer string in a split it up into multiple strings if there are
     * multiple possible answers the user can type.
     * @param answer the entire answer string line, each individual acceptable is separated by '/'.
     */
    private void readAnswers(String answer){
        String[] answers = answer.split("[/]+");
        for(String individualAnswer: answers){
            this.answers.add(individualAnswer);
        }
    }


    /**
     * Return whether the question is available or not.
     */
    public boolean isAvailable() {
        return isAvailable;
    }

    /**
     * Checks whether a given answer is correct or not, case-insensitive.
     * @param response the users answer.
     */
    public boolean checkAnswerCorrect(String response) {
        this.isAvailable = false;
        for(String possibleAnswer: answers){
            if(checkIndivdualAnswer(response,possibleAnswer)){
                return true;
            }
        }
        return false;
    }

    /**
     * Checks an individual user response against a particular answer from all the possible.
     * @param response the users response.
     * @param answer a particular answer that is correct.
     */
    private boolean checkIndivdualAnswer(String response,String answer) {
        String correctAnswer= answer.trim().toLowerCase();
        String userAnswer = response.trim().toLowerCase();
        //first check if the answers match and the user is correct
        if(correctAnswer.equals(userAnswer)) {
            return true;
        }
        //check if the answers are the same length, if not then return false
        if(correctAnswer.length()!=userAnswer.length()){
            return false;
        }

        //check if the user has entered an answer that doesnt contain a macron when it should at a vowel location
        int i = 0;
        boolean sameCharacter = true;
        while(i<correctAnswer.length() && sameCharacter){
            char answerChar = correctAnswer.charAt(i);
            char userChar = userAnswer.charAt(i);
            //if the answer expects a macron and the user has not entered a macron but the correct letter, consider it the same character
            switch(answerChar){
                case 'ā':
                    sameCharacter= userChar=='a';
                    break;
                case 'ē':
                    sameCharacter= userChar=='e';
                    break;
                case 'ī':
                    sameCharacter= userChar=='i';
                    break;
                case 'ō':
                    sameCharacter= userChar=='o';
                    break;
                case 'ū':
                    sameCharacter= userChar=='u';
                    break;
                default :
                    sameCharacter= userChar==answerChar;
                    break;
            }
            //if the characters in a loop do not match at all then return false
            if(!sameCharacter){
                return false;
            }
            i++;
        }
        return true;
    }

    /**
     * Checks if a given question line is valid and can be displayed.
     */
    public boolean checkValidQuestion(){
        if(question==null || questionType==null || answers ==null){
            return false;
        }
        return true;
    }

    /**
     * Set the value of a specific question.
     * @param value the value to set the question too.
     */
    public void setQuestionValue(int value){
        this.value =value;
    }

    /**
     * Resets a Question so it is available to be asked again.
     */
    public void resetQuestion(){
        isAvailable=true;
    }

    /**
     * Displays question data in String form.
     */
    public String display() {
        String output =question+"|"+questionType+"|"+this.getAnswer()+"|"+isAvailable+"|"+ value;
        return output;
    }

    //Getter Methods

    public Category getCategory() {
        return category;
    }
    public String getQuestion() {
        return question;
    }
    public int getValue() {
        return value;
    }
    public String getStringValue() {
        Integer stringValue = value;
        return stringValue.toString();
    }
    /**
     * Return the correct answer(s) for the question.
     */
    public String getAnswer() {
        String output = answers.get(0);
        for(int i = 1; i< answers.size(); i++){
            output += "/"+ answers.get(i);
        }
        return output;
    }
}