package nl.vu.cs.softwaredesign.FlashcardPackage;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class DeleteFlashCards extends Application {

    private Label statusLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Delete Flashcards");

        // Create label and text field for user input
        Label wordLabel = new Label("Word to delete:");
        TextField wordField = new TextField();

        // Create delete button
        Button deleteButton = new Button("Delete");
        deleteButton.setOnAction(e -> {
            String wordToDelete = wordField.getText().trim();
            if (wordToDelete.isEmpty()) {
                showAlert("Please enter a word to delete.");
                return;
            }

            // Capitalize the first letter of the word
            wordToDelete = capitalizeFirstLetter(wordToDelete);

            // Check if the word exists in progress.json
            boolean deleted = deleteWordFromProgress(wordToDelete);
            if (deleted) {
                statusLabel.setText(wordToDelete + " is deleted.");
            } else {
                statusLabel.setText("The word you want to delete doesn't exist.");
            }
        });

        // Create status label
        statusLabel = new Label();

        // Create quit button
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout for delete flashcards window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(wordLabel, wordField, deleteButton, statusLabel, quitButton);

        // Set the scene
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to capitalize the first letter of a word
    private String capitalizeFirstLetter(String word) {
        if (word == null || word.isEmpty()) {
            return word;
        }
        return Character.toUpperCase(word.charAt(0)) + word.substring(1).toLowerCase();
    }

    // Method to delete a word from progress.json
    private boolean deleteWordFromProgress(String wordToDelete) {
        try (Reader reader = new FileReader("progress.json")) {
            Gson gson = new Gson();
            List<Flashcard> progressData = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            // Search for the word to delete
            for (Flashcard flashcard : progressData) {
                if (flashcard.getWord().equals(wordToDelete)) {
                    progressData.remove(flashcard); // Remove the flashcard
                    try (FileWriter writer = new FileWriter("progress.json")) {
                        gson.toJson(progressData, writer);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    return true;
                }
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
        Scene alertScene = new Scene(alertLayout, 300, 100);
        alertStage.setScene(alertScene);
        alertStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
