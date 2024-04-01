package nl.vu.cs.softwaredesign.FlashcardPackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.vu.cs.softwaredesign.FlashcardPackage.Flashcard;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CreatCardsManually extends Application {

    private Label successLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Create Flashcards Manually");

        // Create labels and text fields for user input
        Label wordLabel = new Label("Word:");
        TextField wordField = new TextField();

        Label translationLabel = new Label("Translation:");
        TextField translationField = new TextField();

        Label levelLabel = new Label("Level:");
        TextField levelField = new TextField();

        // Create add button
        Button addButton = new Button("Add");
        addButton.setOnAction(e -> {
            String word = wordField.getText();
            String translation = translationField.getText();
            String levelText = levelField.getText();

            // Check if all fields are filled
            if (word.isEmpty() || translation.isEmpty() || levelText.isEmpty()) {
                // Show warning if any field is empty
                showAlert("Please fill in all fields.");
            } else {
                try {
                    int level = Integer.parseInt(levelText);

                    // Capitalize the first letter of the word
                    word = capitalizeFirstLetter(word);

                    // Check if the word already exists in progress.json
                    if (isWordInProgress(word)) {
                        showAlert("It is already in the learning database.");
                        return;
                    }

                    // Create new Flashcard with the correct ID
                    int newId = getNextIdForLevel("progress.json", level);
                    Flashcard newFlashcard = new Flashcard(word, translation, newId, level);

                    // Add new Flashcard to databases
                    if (addFlashcard(newFlashcard)) {
                        // Show success message
                        successLabel.setText(word + " successfully saved.");
                    }

                    // Clear text fields
                    wordField.clear();
                    translationField.clear();
                    levelField.clear();
                } catch (NumberFormatException ex) {
                    // Show warning if level field is not a number
                    showAlert("Please enter a valid level (a number).");
                }
            }
        });

        // Create label for success message
        successLabel = new Label();

        // Layout for create flashcards manually window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(wordLabel, wordField, translationLabel, translationField, levelLabel, levelField, addButton, successLabel);

        // Set the scene
        Scene scene = new Scene(layout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to capitalize the first letter of a word
    private String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    // Method to check if the word already exists in progress.json
    private boolean isWordInProgress(String word) {
        try (Reader reader = new FileReader("progress.json")) {
            Gson gson = new Gson();
            List<Flashcard> progressData = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            for (Flashcard flashcard : progressData) {
                if (flashcard.getWord().equals(word)) {
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to get the next ID for a given level
    private int getNextIdForLevel(String fileName, int level) {
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new Gson();
            List<Flashcard> flashcardList = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            // Find the maximum ID for the given level
            int maxId = 0;
            for (Flashcard flashcard : flashcardList) {
                if (flashcard.getLevel() == level && flashcard.getId() > maxId) {
                    maxId = flashcard.getId();
                }
            }

            // Return the next available ID
            return maxId + 1;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 1;
    }

    // Method to add Flashcard to databases
    private boolean addFlashcard(Flashcard flashcard) {
        // Add flashcard to progress.json
        boolean success = addFlashcardToDatabase("progress.json", flashcard);
        return success;
    }
    // Method to add Flashcard to a specific database file in vertical format
    private boolean addFlashcardToDatabase(String fileName, Flashcard flashcard) {
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Flashcard> flashcardList = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            // Find the maximum ID among all existing flashcards
            int maxId = 0;
            for (Flashcard card : flashcardList) {
                if (card.getId() > maxId) {
                    maxId = card.getId();
                }
            }

            // Set the ID of the new flashcard
            flashcard.setId(maxId + 1);

            // Add the new flashcard to the existing list
            flashcardList.add(flashcard);

            // Write the updated list back to the file
            try (FileWriter fileWriter = new FileWriter(fileName)) {
                gson.toJson(flashcardList, fileWriter);
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    // Method to show alert message
    private void showAlert(String message) {
        Stage alertStage = new Stage();
        alertStage.setTitle("Alert");
        VBox alertLayout = new VBox(10);
        alertLayout.setPadding(new Insets(20));
        alertLayout.getChildren().add(new Label(message));
        Scene alertScene = new Scene(alertLayout, 200, 100);
        alertStage.setScene(alertScene);
        alertStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
