package nl.vu.cs.softwaredesign;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileReader;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class LearningModeController extends Application {
    private Flashcard[] flashcards;
    private int currentIndex;
    private Flashcard currentFlashcard;
    private Level[] levels;
    private Label levelLabel;
    private Label displayLabel;
    private Label remainingFlashcardsLabel;
    private boolean showWord = true;

    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load progress from progress.json
        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader("progress.json")) {
            // Parse JSON array
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            // Initialize flashcards array
            flashcards = new Flashcard[jsonArray.size()];

            // Populate flashcards array
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                String word = (String) jsonObject.get("word");
                String translation = (String) jsonObject.get("translation");
                boolean mastered = (boolean) jsonObject.get("mastered");
                int id = ((Long) jsonObject.get("id")).intValue();
                int level = ((Long) jsonObject.get("level")).intValue();
                flashcards[i] = new Flashcard(word, translation, id, level);
                flashcards[i].setMastered(mastered);
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Handle file loading error
        }

        // Find the lowest level and unmastered flashcard
        Flashcard lowestLevelUnmasteredFlashcard = null;
        int lowestLevel = Integer.MAX_VALUE;
        for (Flashcard flashcard : flashcards) {
            if (!flashcard.isMastered() && flashcard.getLevel() < lowestLevel) {
                lowestLevelUnmasteredFlashcard = flashcard;
                lowestLevel = flashcard.getLevel();
            }
        }
        if (lowestLevelUnmasteredFlashcard != null) {
            currentIndex = findIndexInFlashcards(lowestLevelUnmasteredFlashcard);
            currentFlashcard = lowestLevelUnmasteredFlashcard;
        } else {
            // If all flashcards are mastered, set currentIndex to 0
            currentIndex = 0;
            currentFlashcard = flashcards[currentIndex];
        }

        // Create levels
        levels = createLevels();

        // Set up UI components
        Button flipButton = new Button("Flip");
        Button pronunciationButton = new Button("Pronunciation");

        Button openRecorderButton = new Button("Open Recorder");
        Button markMasteredButton = new Button("Mark as Mastered");
        Button skipButton = new Button("Next Word");
        Button previousButton = new Button("Previous Word");
        Button returnButton = new Button("Return");
        Button clearResultsButton = new Button("Clear All Results");

        pronunciationButton.setOnAction(event -> {
            Pronunciation audioRecorder = new Pronunciation();

            if (showWord) {
                audioRecorder.str2voice(currentFlashcard.getWord());
            } else {
                audioRecorder.str2voice(currentFlashcard.getTranslation());
            }
        });



        openRecorderButton.setOnAction(event -> {
            Stage recorderStage = new Stage();
            Audio audioRecorder = new Audio();
            audioRecorder.start(recorderStage);});


        levelLabel = new Label();
        displayLabel = new Label();
        remainingFlashcardsLabel = new Label();

        // Update UI labels
        updateLevelLabel();
        updateRemainingFlashcardsLabel();

        displayLabel.setText(currentFlashcard.getWord());
        displayLabel.setFont(Font.font(100));
        displayLabel.setTextAlignment(TextAlignment.CENTER);

        HBox displayBox = new HBox();
        displayBox.setAlignment(Pos.CENTER);
        displayBox.setPadding(new Insets(0, 50, 0, 50));
        displayBox.getChildren().add(displayLabel);
        String buttonStyle = "-fx-background-color: #37ab95; " +
                "-fx-text-fill: white; " +
                "-fx-font-size: 14pt; " +
                "-fx-pref-width: 200px; " +
                "-fx-pref-height: 40px;";

        previousButton.setOnAction(e -> {
            if (currentIndex > 0) {
                // Get the current level
                int currentLevel = currentFlashcard.getLevel();
                // Get the list of unmastered flashcards for the current level
                ArrayList<Flashcard> unmasteredFlashcards = new ArrayList<>();
                for (Flashcard flashcard : levels[currentLevel - 1].getFlashcards()) {
                    if (!flashcard.isMastered()) {
                        unmasteredFlashcards.add(flashcard);
                    }
                }
                // Find the previous unmastered flashcard
                int previousIndex = currentIndex - 1;
                while (previousIndex >= 0) {
                    if (!flashcards[previousIndex].isMastered() && flashcards[previousIndex].getLevel() == currentLevel) {
                        currentFlashcard = flashcards[previousIndex];
                        currentIndex = previousIndex;
                        break;
                    }
                    previousIndex--;
                }
                updateLevelLabel();
                updateRemainingFlashcardsLabel();
                displayLabel.setText(currentFlashcard.getWord());
            }
        });

        clearResultsButton.setOnAction(e -> {
            try {
                // Clear all results
                clearAllResults();
                // Reload the UI with the updated flashcards and currentIndex
                start(primaryStage);
            } catch (Exception ex) {
                ex.printStackTrace();
                // Handle the exception or display an error message
            }
        });

        skipButton.setOnAction(e -> {
            if (currentIndex < flashcards.length - 1) {
                // If there are unmastered flashcards in the current level, find and display the next unmastered flashcard
                if (!isLevelMastered(currentFlashcard.getLevel())) {
                    ArrayList<Flashcard> currentLevelFlashcards = new ArrayList<>(levels[currentFlashcard.getLevel() - 1].getFlashcards());
                    int nextIndex = (currentIndex + 1) % currentLevelFlashcards.size();
                    while (nextIndex != currentIndex) {
                        Flashcard nextFlashcard = currentLevelFlashcards.get(nextIndex);
                        if (!nextFlashcard.isMastered()) {
                            currentFlashcard = nextFlashcard;
                            currentIndex = getFlashcardIndex(currentFlashcard);
                            break;
                        }
                        nextIndex = (nextIndex + 1) % currentLevelFlashcards.size();
                    }
                } else {
                    // Move to the next flashcard
                    currentFlashcard = flashcards[++currentIndex];
                }
            }
            updateLevelLabel();
            updateRemainingFlashcardsLabel();
            displayLabel.setText(currentFlashcard.getWord());

    });
        markMasteredButton.setOnAction(e -> {
            if (currentIndex < flashcards.length) {
                // Mark the current flashcard as mastered
                flashcards[currentIndex].markAsMastered();
                // Get the current level
                int currentLevel = flashcards[currentIndex].getLevel();
                // If there are unmastered flashcards in the current level, find and display the next unmastered flashcard
                if (!isLevelMastered(currentLevel)) {
                    ArrayList<Flashcard> currentLevelFlashcards = new ArrayList<>(levels[currentLevel - 1].getFlashcards());
                    int nextIndex = (currentIndex + 1) % currentLevelFlashcards.size();
                    while (nextIndex != currentIndex) {
                        Flashcard nextFlashcard = currentLevelFlashcards.get(nextIndex);
                        if (!nextFlashcard.isMastered()) {
                            currentFlashcard = nextFlashcard;
                            currentIndex = getFlashcardIndex(currentFlashcard);
                            break;
                        }
                        nextIndex = (nextIndex + 1) % currentLevelFlashcards.size();
                    }
                } else {
                    // Move to the next level's first unmastered flashcard
                    int nextLevel = currentLevel + 1;
                    while (nextLevel <= levels.length) {
                        ArrayList<Flashcard> nextLevelFlashcards = new ArrayList<>(levels[nextLevel - 1].getFlashcards());
                        int nextIndex = 0;
                        while (nextIndex < nextLevelFlashcards.size()) {
                            Flashcard nextFlashcard = nextLevelFlashcards.get(nextIndex);
                            if (!nextFlashcard.isMastered()) {
                                currentFlashcard = nextFlashcard;
                                currentIndex = getFlashcardIndex(currentFlashcard);
                                break;
                            }
                            nextIndex++;
                        }
                        if (currentIndex != nextIndex) {
                            break;
                        }
                        nextLevel++;
                    }
                }
                // Update UI labels
                updateLevelLabel();
                updateRemainingFlashcardsLabel();
                // Display the word of the current flashcard
                displayLabel.setText(currentFlashcard.getWord());
            }
        });




        returnButton.setOnAction(e -> {
        Persistence persistence = new Persistence();
        persistence.saveProgress(flashcards, currentIndex);
        // Return to the main page
        Stage stage = (Stage) returnButton.getScene().getWindow();
        stage.close(); // Close the current stage
        openMainPage(); // Open the main page
    });

        flipButton.setOnAction(e -> {
        if (showWord) {
            displayLabel.setText(currentFlashcard.getTranslation());
        } else {
            displayLabel.setText(currentFlashcard.getWord());
        }
        showWord = !showWord;
    });

        flipButton.setStyle(buttonStyle);
        markMasteredButton.setStyle(buttonStyle);
        pronunciationButton.setStyle(buttonStyle);
        openRecorderButton.setStyle(buttonStyle);
        skipButton.setStyle(buttonStyle);
        previousButton.setStyle(buttonStyle);
        returnButton.setStyle(buttonStyle);
        clearResultsButton.setStyle(buttonStyle);
    VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(levelLabel,displayLabel, flipButton, markMasteredButton, pronunciationButton ,openRecorderButton,skipButton, previousButton, returnButton, remainingFlashcardsLabel, clearResultsButton);

    // Set the scene
    Scene scene = new Scene(root, 600, 761);
        FileInputStream inp = new FileInputStream("src/main/java/nl/vu/cs/softwaredesign/resources/bg.jpg");
        Image im = new Image(inp);
        BackgroundImage bi = new BackgroundImage(im,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(bi);
        root.setBackground(bg);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Flashcard Study Mode");
        primaryStage.show();

}


private Level[] createLevels() {
    // Create levels
    Level level1 = new Level(1, "Level 1", "Basic greetings");
    Level level2 = new Level(2, "Level 2", "Intermediate phrases");
    Level level3 = new Level(3, "Level 3", "Advanced expressions");

    // Add flashcards to levels
    for (Flashcard flashcard : flashcards) {
        switch (flashcard.getLevel()) {
            case 1:
                level1.addFlashcard(flashcard);
                break;
            case 2:
                level2.addFlashcard(flashcard);
                break;
            case 3:
                level3.addFlashcard(flashcard);
                break;
            default:
                // Handle invalid level
                break;
        }
    }

    return new Level[]{level1, level2, level3};
}

private void updateLevelLabel() {
    if (currentFlashcard != null) {
        int currentLevel = currentFlashcard.getLevel();
        long remainingFlashcards = levels[currentLevel - 1].getFlashcards().stream().filter(f -> !f.isMastered()).count();
        levelLabel.setText("Current Level: " + levels[currentLevel - 1].getName() + ", Remaining Flashcards: " + remainingFlashcards);
    }
}

private void updateRemainingFlashcardsLabel() {
    if (currentFlashcard != null) {
        int currentLevel = currentFlashcard.getLevel();
        ArrayList<String> remainingFlashcards = new ArrayList<>();
        for (Flashcard flashcard : levels[currentLevel - 1].getFlashcards()) {
            if (!flashcard.isMastered()) {
                remainingFlashcards.add(flashcard.getWord());
            }
        }
        remainingFlashcardsLabel.setText("Remaining Flashcards: " + String.join(", ", remainingFlashcards));
    }
}

private int getFlashcardIndex(Flashcard flashcard) {
    for (int i = 0; i < flashcards.length; i++) {
        if (flashcards[i] == flashcard) {
            return i;
        }
    }
    return -1;
}

private boolean isLevelMastered(int level) {
    for (Flashcard flashcard : levels[level - 1].getFlashcards()) {
        if (!flashcard.isMastered()) {
            return false;
        }
    }
    return true;
}

private void openMainPage() {
    Main main = new Main();
    try {
        main.start(new Stage());
    } catch (Exception e) {
        e.printStackTrace();
    }
}

private void clearAllResults() {
    for (Flashcard flashcard : flashcards) {
        flashcard.setMastered(false);
    }
    // Save the updated progress
    Persistence persistence = new Persistence();
    persistence.saveProgress(flashcards, 0);
}

private int findIndexInFlashcards(Flashcard flashcard) {
    for (int i = 0; i < flashcards.length; i++) {
        if (flashcards[i] == flashcard) {
            return i;
        }
    }
    return -1;
}

public static void main(String[] args) {
    launch(args);
}
}
