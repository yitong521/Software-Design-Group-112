package nl.vu.cs.softwaredesign;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class LevelPageController extends Application {

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Modify Level");

        // Create button to call CreateLevel
        Button createLevelButton = new Button("Create Level");
        Button adjustFlashcardLevelButton = new Button("Adjust Flashcard");
        Button deleteLevelButton = new Button("Delete Level");

        createLevelButton.setOnAction(e -> {
            CreateLevel createLevel = new CreateLevel();
            createLevel.start(new Stage());
        });

        adjustFlashcardLevelButton.setOnAction(e -> openAdjustMode(primaryStage));

        deleteLevelButton.setOnAction(e -> {
            DeleteLevel deleteLevel = new DeleteLevel();
            deleteLevel.start(new Stage());
        });

        // Create quit button
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        // Create layout for the window
        VBox layout = new VBox(10);
        layout.getChildren().addAll(createLevelButton, adjustFlashcardLevelButton, deleteLevelButton, quitButton);

        // Set the scene
        Scene scene = new Scene(layout, 200, 150);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openAdjustMode(Stage primaryStage) {
        // Launch AdjustMode window
        AdjustMode adjustMode = new AdjustMode();
        try {
            adjustMode.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
