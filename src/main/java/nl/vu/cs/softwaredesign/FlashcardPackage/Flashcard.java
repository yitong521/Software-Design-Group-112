package nl.vu.cs.softwaredesign.FlashcardPackage;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.util.Duration;

public class Flashcard {
    private String word;
    private String translation;
    private boolean mastered;
    private int id;
    private int level;

    // Constructor
    public Flashcard(String word, String translation, int id, int level) {
        this.word = word;
        this.translation = translation;
        this.id = id;
        this.level = level;
        this.mastered = false;
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
    public void setId(int id) {
        this.id = id;
    }
    public void setTranslation(String translation) {
        this.translation = translation;
    }
    public void setLevel(int level) {
        this.level = level;
    }

    public void setWord(String word) {
        this.word = word;
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
    public void setMastered(boolean mastered) {
        this.mastered = mastered;
    }

    public int getLevel() {
        return level;
    }

}