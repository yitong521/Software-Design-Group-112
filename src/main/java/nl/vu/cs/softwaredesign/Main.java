package nl.vu.cs.softwaredesign;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;

import javafx.scene.image.Image;
import nl.vu.cs.softwaredesign.controller.AchievementsPageController;
import nl.vu.cs.softwaredesign.controller.LearningModeController;
import nl.vu.cs.softwaredesign.controller.LevelPageController;
import nl.vu.cs.softwaredesign.controller.PrintController;


import java.io.*;

public class Main extends Application {

    private Label levelCompletedLabel;

    @Override
    public void start(Stage primaryStage) throws FileNotFoundException {
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


        Label welcomeLabel = new Label("Greetings, learner! \nWelcome to our Flashcard App!");
        welcomeLabel.setStyle("-fx-font-family: 'Arial Black'; -fx-font-size: 20pt;");

        // Layout for main page

        VBox mainLayout = new VBox(20);
        mainLayout.setPadding(new Insets(20));
        mainLayout.getChildren().addAll(welcomeLabel,studyFlashcardsButton,printDataButton, modifyLevelButton, viewBadgesButton, exitButton); // Added the new label and button


        // Set button styles
        studyFlashcardsButton.setStyle("-fx-background-color: #4CAF50; -fx-text-fill: white; -fx-font-size: 14pt;");
        printDataButton.setStyle("-fx-background-color: #008CBA; -fx-text-fill: white; -fx-font-size: 14pt;");
        modifyLevelButton.setStyle("-fx-background-color: #23b9e3; -fx-text-fill: white; -fx-font-size: 14pt;");
        viewBadgesButton.setStyle("-fx-background-color: #aba6a6; -fx-text-fill: white; -fx-font-size: 14pt;");
        exitButton.setStyle("-fx-background-color: #555555; -fx-text-fill: white; -fx-font-size: 14pt;");

        VBox.setMargin(studyFlashcardsButton, new Insets(70, 0, 0, 0));
        VBox.setMargin(printDataButton, new Insets(10, 0, 0, 0));
        VBox.setMargin(modifyLevelButton, new Insets(10, 0, 0, 0));
        VBox.setMargin(viewBadgesButton, new Insets(10, 0, 0, 0));
        VBox.setMargin(exitButton, new Insets(10, 0, 0, 0));


        // Set the scene
        Scene scene = new Scene(mainLayout, 600, 761);
        FileInputStream inp = new FileInputStream("src/main/java/nl/vu/cs/softwaredesign/resources/bg.jpg");
        Image im = new Image(inp);
        BackgroundImage bi = new BackgroundImage(im,
                BackgroundRepeat.NO_REPEAT,
                BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.DEFAULT,
                BackgroundSize.DEFAULT);
        Background bg = new Background(bi);
        mainLayout.setBackground(bg);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void openFlashcardStudyMode(Stage primaryStage) {
        // Launch Flashcard study mode
        LearningModeController flashcardStudyMode = new LearningModeController();
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
        LevelPageController modifyLevel = new LevelPageController();
        try {
            modifyLevel.start(new Stage());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openBadgeManager(Stage primaryStage) {
        // Launch BadgeManager window
        AchievementsPageController badgeManagerUI = AchievementsPageController.getInstance();
        badgeManagerUI.show();
    }


    public static void main(String[] args) {
        launch(args);
    }

}
