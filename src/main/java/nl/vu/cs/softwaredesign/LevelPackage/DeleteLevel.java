package nl.vu.cs.softwaredesign.LevelPackage;

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
import nl.vu.cs.softwaredesign.FlashcardPackage.Flashcardscomponent.Flashcard;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class DeleteLevel extends Application {

    private TextField levelInput;
    private Label resultLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Delete Level");

        // Create input field for entering level number
        levelInput = new TextField();
        levelInput.setPromptText("Enter level number");

        // Create button to search for level and delete if confirmed
        Button searchButton = new Button("Search");
        searchButton.setOnAction(e -> searchAndDeleteLevel());

        // Create label to display search result
        resultLabel = new Label();

        // Create button to confirm deletion
        Button confirmButton = new Button("Confirm to delete");
        confirmButton.setOnAction(e -> confirmDeleteLevel());

        // Create button to quit
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        // Create layout for the window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(levelInput, searchButton, resultLabel, confirmButton, quitButton);

        // Set the scene
        Scene scene = new Scene(layout, 300, 200);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void searchAndDeleteLevel() {
        int levelNumber = Integer.parseInt(levelInput.getText());

        List<Flashcard> flashcards = loadFlashcardsFromJSON("progress.json");
        StringBuilder words = new StringBuilder();
        boolean levelFound = false;

        for (Flashcard flashcard : flashcards) {
            if (flashcard.getLevel() == levelNumber) {
                words.append(flashcard.getWord()).append(", ");
                levelFound = true;
            }
        }

        if (levelFound) {
            resultLabel.setText("Here are the words of the level " + levelNumber + ": " + words.toString());
        } else {
            resultLabel.setText("No related level or the level is empty");
        }
    }

    private void confirmDeleteLevel() {
        int levelNumber = Integer.parseInt(levelInput.getText());

        List<Flashcard> flashcards = loadFlashcardsFromJSON("progress.json");
        flashcards.removeIf(flashcard -> flashcard.getLevel() == levelNumber);

        saveFlashcardsToJSON(flashcards, "progress.json");

        resultLabel.setText("Level " + levelNumber + " deleted successfully.");
    }

    private List<Flashcard> loadFlashcardsFromJSON(String fileName) {
        try (Reader reader = new FileReader(fileName)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<Flashcard>>() {}.getType());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void saveFlashcardsToJSON(List<Flashcard> flashcards, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            gson.toJson(flashcards, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
