package nl.vu.cs.softwaredesign.LevelPackage;

import nl.vu.cs.softwaredesign.FlashcardPackage.Flashcardscomponent.Flashcard;

import java.util.ArrayList;

public class Level {
    private String name;
    private boolean unlocked;
    private ArrayList<Flashcard> flashcards;

    public Level(int id, String name, String description) {
        this.name = name;
        this.unlocked = false;
        this.flashcards = new ArrayList<>();
    }
    public String getName() {
        return name;
    }
    public boolean isUnlocked() {
        return unlocked;
    }
    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    public int countUnlockedFlashcards() {
        int count = 0;
        for (Flashcard flashcard : flashcards) {
            if (flashcard.isMastered()) {
                count++;
            }
        }
        return count;
    }
}