import java.util.ArrayList;

public class Level {
    private int id;
    private String name;
    private String description;
    private boolean unlocked;
    private ArrayList<Flashcard> flashcards;

    public Level(int id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.unlocked = false;
        this.flashcards = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        this.unlocked = true;
    }

    public ArrayList<Flashcard> getFlashcards() {
        return flashcards;
    }

    public void addFlashcard(Flashcard flashcard) {
        flashcards.add(flashcard);
    }

    public void displayFlashcards() {
        for (Flashcard flashcard : flashcards) {
            System.out.println("Flashcard: " + flashcard.getWord() + " - " + flashcard.getTranslation());
        }
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
