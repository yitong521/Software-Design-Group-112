package nl.vu.cs.softwaredesign.controller;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.vu.cs.softwaredesign.PersistencePackage.Badge;

public class AchievementsPageController {

    private static AchievementsPageController instance;

    private Stage stage;
    private VBox layout;

    // Private constructor to prevent external instantiation
    private AchievementsPageController() {
        stage = new Stage();
        stage.setTitle("Badge Manager");
        layout = new VBox(10);
        layout.setPadding(new Insets(20));

        // Display completed levels
        int completedLevels = Badge.getCompletedLevelsCount();
        Label completedLevelsLabel = new Label("Total mastered words: " + completedLevels);
        layout.getChildren().add(completedLevelsLabel);

        // Determine the badge based on completed levels
        String badgeName;
        if (completedLevels >= 3) {
            badgeName = "Platinum";
        } else if (completedLevels >= 2) {
            badgeName = "Gold";
        } else if (completedLevels >= 1) {
            badgeName = "Silver";
        } else {
            badgeName = "Please continue learning!";
        }

        // Display badge name
        Label badgeLabel = new Label("Badge: " + badgeName);
        layout.getChildren().add(badgeLabel);

        Scene scene = new Scene(layout);
        stage.setScene(scene);
    }

    // Static method to get the singleton instance
    public static AchievementsPageController getInstance() {
        if (instance == null) {
            instance = new AchievementsPageController();
        }
        return instance;
    }

    // Method to show the UI
    public void show() {
        stage.show();
    }
}
