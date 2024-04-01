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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.vu.cs.softwaredesign.FlashcardPackage.Flashcard;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class EditFlashcards extends Application {

    private TextField wordField;
    private TextField translationField;
    private TextField levelField;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Edit Flashcards");

        // Create labels and text fields for user input
        Label wordLabel = new Label("Word:");
        wordField = new TextField();

        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchFlashcard());

        // Layout for word search
        HBox searchLayout = new HBox(10);
        searchLayout.getChildren().addAll(wordLabel, wordField, searchButton);

        // Create labels and text fields for displaying/editing flashcard information
        Label translationLabel = new Label("Translation:");
        translationField = new TextField();
        translationField.setEditable(false);

        // Create level field
        Label levelLabel = new Label("Level:");
        levelField = new TextField();
        levelField.setEditable(false);

        // Layout for flashcard information
        VBox flashcardLayout = new VBox(10);
        flashcardLayout.getChildren().addAll(translationLabel, translationField, levelLabel, levelField);

        // Create button to save edits
        Button saveButton = new Button("Save Edit");
        saveButton.setOnAction(e -> saveEdit());

        // Create label for displaying search result
        resultLabel = new Label();

        // Create button to quit window
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout for edit flashcards window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(searchLayout, flashcardLayout, saveButton, resultLabel, quitButton);

        // Set the scene
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void searchFlashcard() {
        String word = wordField.getText().trim();
        if (word.isEmpty()) {
            resultLabel.setText("Please enter a word to search.");
            return;
        }

        // Convert word to capitalized form
        word = capitalizeFirstLetter(word);

        // Search for the word in progress.json
        Flashcard flashcard = findFlashcard(word);
        if (flashcard != null) {
            translationField.setText(flashcard.getTranslation());
            levelField.setText(String.valueOf(flashcard.getLevel()));
            translationField.setEditable(true);
            levelField.setEditable(true);
            resultLabel.setText("Flashcard found.");
        } else {
            translationField.setText("");
            levelField.setText("");
            translationField.setEditable(false);
            resultLabel.setText("Word not found in flashcards.");
        }
    }

    private Flashcard findFlashcard(String word) {
        try (Reader reader = new FileReader("progress.json")) {
            Gson gson = new Gson();
            List<Flashcard> flashcards = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            for (Flashcard flashcard : flashcards) {
                if (flashcard.getWord().equalsIgnoreCase(word)) {
                    return flashcard;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveEdit() {
        String word = wordField.getText().trim();
        if (word.isEmpty()) {
            resultLabel.setText("Please search for a word first.");
            return;
        }

        String newTranslation = translationField.getText().trim();
        String newLevelText = levelField.getText().trim();

        if (newTranslation.isEmpty() && newLevelText.isEmpty()) {
            resultLabel.setText("No changes made.");
            return;
        }

        // Convert word to capitalized form
        word = capitalizeFirstLetter(word);

        // Find the flashcard to edit
        Flashcard flashcard = findFlashcard(word);
        if (flashcard == null) {
            resultLabel.setText("Word not found in flashcards.");
            return;
        }

        // Update translation if changed
        if (!newTranslation.isEmpty()) {
            flashcard.setTranslation(newTranslation);
        }

        // Update level if changed
        if (!newLevelText.isEmpty()) {
            try {
                int newLevel = Integer.parseInt(newLevelText);
                flashcard.setLevel(newLevel);

            } catch (NumberFormatException e) {
                resultLabel.setText("Invalid level format. Please enter a valid number.");
                return;
            }
        }

        // Update flashcard in databases
        updateFlashcardInDatabase(flashcard);

        resultLabel.setText("Flashcard updated successfully.");
    }

    private void updateFlashcardInDatabase(Flashcard flashcard) {
        // Update flashcard in progress.json
        updateFlashcardInFile(flashcard, "progress.json");
    }

    private void updateFlashcardInFile(Flashcard flashcard, String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            List<Flashcard> flashcards = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            boolean updated = false;
            for (Flashcard card : flashcards) {
                if (card.getWord().equalsIgnoreCase(flashcard.getWord())) {
                    card.setTranslation(flashcard.getTranslation());
                    card.setLevel(flashcard.getLevel());
                    updated = true;
                    break;
                }
            }

            if (updated) {
                try (FileWriter writer = new FileWriter(fileName)) {
                    gson.toJson(flashcards, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
