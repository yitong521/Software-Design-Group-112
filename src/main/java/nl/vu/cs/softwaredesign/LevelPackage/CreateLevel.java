package nl.vu.cs.softwaredesign.LevelPackage;

import com.google.gson.Gson;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.vu.cs.softwaredesign.FlashcardPackage.Flashcard;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

public class CreateLevel extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Create Level");

        // Create label for displaying current level
        Label levelLabel = new Label("Already created the Level: ");
        updateLevelLabel(levelLabel);

        // Create quit button to close the window
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        // Create layout for the window
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(levelLabel, quitButton);

        // Set the scene
        Scene scene = new Scene(layout, 300, 250);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void updateLevelLabel(Label levelLabel) {
        // Calculate the current level based on existing flashcards
        int maxLevel = calculateMaxLevel();

        // Update current level label
        levelLabel.setText("Already created the new level:" + (maxLevel + 1));
    }

    private int calculateMaxLevel() {
        int maxLevel = 0;
        try (Reader reader = new FileReader("progress.json")) {
            Gson gson = new Gson();
            List<Flashcard> flashcards = gson.fromJson(reader, new com.google.gson.reflect.TypeToken<List<Flashcard>>() {}.getType());
            for (Flashcard flashcard : flashcards) {
                if (flashcard.getLevel() > maxLevel) {
                    maxLevel = flashcard.getLevel();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return maxLevel;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
