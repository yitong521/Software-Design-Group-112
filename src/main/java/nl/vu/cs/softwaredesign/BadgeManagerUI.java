package nl.vu.cs.softwaredesign;

import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BadgeManagerUI {

    private Stage stage;

    public BadgeManagerUI(Stage primaryStage) {
        this.stage = new Stage();
        this.stage.setTitle("Badge Manager");
        VBox layout = new VBox(10);
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
        stage.initOwner(primaryStage);
    }

    public void show() {
        stage.show();
    }
}