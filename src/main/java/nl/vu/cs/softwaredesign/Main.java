import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label; // Import Label class
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    private Flashcard[] flashcards;
    private int currentIndex;
    private Flashcard currentFlashcard;
    private Level[] levels; // Add an array of Level objects
    private Label levelLabel; // Add a Label for displaying current level

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Create Flashcards for testing
        Flashcard flashcard1 = new Flashcard("Hello", "你好", 1, 1);
        Flashcard flashcard2 = new Flashcard("Goodbye", "再见", 2, 1);
        Flashcard flashcard3 = new Flashcard("Thank you", "谢谢", 3, 1);
        // Level 2 flashcards
        Flashcard flashcard4 = new Flashcard("Good morning", "早上好", 4, 2);
        Flashcard flashcard5 = new Flashcard("Good night", "晚安", 5, 2);
        Flashcard flashcard6 = new Flashcard("Please", "请", 6, 2);
        // Level 3 flashcards
        Flashcard flashcard7 = new Flashcard("Excuse me", "对不起", 7, 3);
        Flashcard flashcard8 = new Flashcard("I'm sorry", "我很抱歉", 8, 3);
        Flashcard flashcard9 = new Flashcard("You're welcome", "不客气", 9, 3);

        flashcards = new Flashcard[]{flashcard1, flashcard2, flashcard3, flashcard4, flashcard5, flashcard6, flashcard7, flashcard8, flashcard9};

        // Create levels
        Level level1 = new Level(1, "Level 1", "Basic greetings");
        Level level2 = new Level(2, "Level 2", "Intermediate phrases");
        Level level3 = new Level(3, "Level 3", "Advanced expressions");

        // Add flashcards to levels
        level1.addFlashcard(flashcard1);
        level1.addFlashcard(flashcard2);
        level1.addFlashcard(flashcard3);
        level2.addFlashcard(flashcard4);
        level2.addFlashcard(flashcard5);
        level2.addFlashcard(flashcard6);
        level3.addFlashcard(flashcard7);
        level3.addFlashcard(flashcard8);
        level3.addFlashcard(flashcard9);

        levels = new Level[]{level1, level2, level3}; // Populate levels array

        // Set up UI components
        Button showWordButton = new Button("Show Word");
        Button showTranslationButton = new Button("Show Translation");
        Button markMasteredButton = new Button("Mark as Mastered");
        levelLabel = new Label(); // Initialize levelLabel

        markMasteredButton.setOnAction(e -> {
            if (currentIndex < flashcards.length) {
                flashcards[currentIndex].markAsMastered();
                // Check if all flashcards in the current level are mastered
                boolean allMastered = true;
                int currentLevel = flashcards[currentIndex].getLevel();
                for (int i = 0; i < flashcards.length; i++) {
                    if (flashcards[i].getLevel() == currentLevel && !flashcards[i].isMastered()) {
                        allMastered = false;
                        break;
                    }
                }
                // If all flashcards in the current level are mastered, move to the next level
                if (allMastered) {
                    // Move to the next level
                    int nextLevel = currentLevel + 1;
                    boolean levelFound = false;
                    for (int i = currentIndex + 1; i < flashcards.length; i++) {
                        if (flashcards[i].getLevel() == nextLevel) {
                            currentIndex = i;
                            currentFlashcard = flashcards[currentIndex];
                            levelFound = true;
                            break;
                        }
                    }
                    // If no more flashcards in the next level, reset to the first flashcard
                    if (!levelFound) {
                        // Find the first flashcard of the next level
                        for (int i = 0; i < flashcards.length; i++) {
                            if (flashcards[i].getLevel() == nextLevel) {
                                currentIndex = i;
                                currentFlashcard = flashcards[currentIndex];
                                break;
                            }
                        }
                    }
                } else {
                    // Move to the next flashcard
                    currentIndex++;
                    if (currentIndex >= flashcards.length) {
                        currentIndex = 0;
                        currentFlashcard = flashcards[currentIndex];
                    } else {
                        currentFlashcard = flashcards[currentIndex];
                    }
                }
                updateLevelLabel(); // Update level label after each action
            }
        });

        showWordButton.setOnAction(e -> {
            if (currentFlashcard != null) {
                currentFlashcard.showWord();
            }
        });

        showTranslationButton.setOnAction(e -> {
            if (currentFlashcard != null) {
                currentFlashcard.showTranslation();
            }
        });

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(levelLabel, showWordButton, showTranslationButton, markMasteredButton);

        // Set the scene
        Scene scene = new Scene(root, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flashcard App");
        primaryStage.show();

        // Set initial flashcard
        currentIndex = 0;
        currentFlashcard = flashcards[currentIndex];
        updateLevelLabel(); // Update level label on startup
    }

    private void updateLevelLabel() {
        // Update level label to display current level and remaining flashcards
        if (currentFlashcard != null) {
            int currentLevel = currentFlashcard.getLevel();
            long remainingFlashcards = levels[currentLevel - 1].getFlashcards().stream().filter(f -> !f.isMastered()).count();
            levelLabel.setText("Current Level: " + levels[currentLevel - 1].getName() + ", Remaining Flashcards: " + remainingFlashcards);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
