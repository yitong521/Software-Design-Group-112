import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Flashcard {
    private String word;
    private String translation;
    private boolean mastered;
    private int id;
    private int level;
    private UserOption userOption;

    // Constructor
    public Flashcard(String word, String translation, int id, int level) {
        this.word = word;
        this.translation = translation;
        this.id = id;
        this.level = level;
        this.mastered = false;
        this.userOption = UserOption.NONE; // Assuming UserOption is an enumeration
    }

    // Getters and Setters
    public String getWord() {
        return word;
    }

    public String getTranslation() {
        return translation;
    }

    public boolean isMastered() {
        return mastered;
    }

    public int getId() {
        return id;
    }

    public UserOption getUserOption() {
        return userOption;
    }

    public void setUserOption(UserOption userOption) {
        this.userOption = userOption;
    }

    // Operations
    public void showWord() {
        System.out.println("Word: " + word);
    }

    public void showTranslation() {
        System.out.println("Translation: " + translation);
    }
    public void markAsMastered() {
        mastered = true;
        System.out.println("Word marked as mastered: " + word);
    }
    public int getLevel() {
        return level;
    }

}
