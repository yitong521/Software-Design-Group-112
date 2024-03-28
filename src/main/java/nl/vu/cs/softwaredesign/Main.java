package nl.vu.cs.softwaredesign;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;



public class Main extends Application {

    private Label levelCompletedLabel;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Flashcard App");

        // Create buttons
        Button studyFlashcardsButton = new Button("Learning Mode");
        Button printDataButton = new Button("Print Data");

        Button modifyLevelButton = new Button("Modify Level");
        Button viewBadgesButton = new Button("View Badges"); // New button
        Button exitButton = new Button("Quit");

        // Set button actions
        studyFlashcardsButton.setOnAction(e -> openFlashcardStudyMode(primaryStage));
        printDataButton.setOnAction(e -> openPrintController(primaryStage));

        modifyLevelButton.setOnAction(e -> openModifyLevel(primaryStage));
        viewBadgesButton.setOnAction(e -> openBadgeManager(primaryStage)); // Action for the new button
        exitButton.setOnAction(e -> primaryStage.close());

        // Create label to display completed levels
        levelCompletedLabel = new Label("Completed Levels: 0");

        // Layout for main page
        VBox mainLayout = new VBox(10);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(studyFlashcardsButton, printDataButton, modifyLevelButton, viewBadgesButton, exitButton, levelCompletedLabel); // Added the new label and button

        // Set the scene
        Scene scene = new Scene(mainLayout, 300, 350);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFlashcardStudyMode(Stage primaryStage) {
        // Launch Flashcard study mode
        FlashcardStudyMode flashcardStudyMode = new FlashcardStudyMode();
        try {
            flashcardStudyMode.start(primaryStage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPrintController(Stage primaryStage) {
        // Launch PrintController window
        PrintController printController = new PrintController();
        try {
            printController.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openModifyLevel(Stage primaryStage) {
        // Launch ModifyLevel window
        ModifyLevel modifyLevel = new ModifyLevel();
        try {
            modifyLevel.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openBadgeManager(Stage primaryStage) {
        // Launch BadgeManager window
        BadgeManagerUI badgeManagerUI = new BadgeManagerUI(primaryStage);
        badgeManagerUI.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    // Method to update the displayed completed levels
    public void updateCompletedLevels(int completedLevels) {
        levelCompletedLabel.setText("Completed Levels: " + completedLevels);
    }
}
