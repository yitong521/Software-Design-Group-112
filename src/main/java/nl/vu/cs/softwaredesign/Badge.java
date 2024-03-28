package nl.vu.cs.softwaredesign;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Badge {
    private String name;
    private Image icon;

    public Badge(String name, String iconPath) {
        this.name = name;
        this.icon = new Image(iconPath);
    }

    // Method to generate badge based on the number of completed levels
    public static Badge generateBadge(int totalLevelsCompleted) {
        String badgeName;
        String iconPath;

        if (totalLevelsCompleted >= 3) {
            badgeName = "Platinum";
            iconPath = "resources/platinum_icon.png"; // Adjust the path according to your icon's location
        } else if (totalLevelsCompleted >= 2) {
            badgeName = "Gold";
            iconPath = "resources/gold_icon.png";
        } else if (totalLevelsCompleted >= 1) {
            badgeName = "Silver";
            iconPath = "resources/silver_icon.png";
        } else {
            return null; // No badge earned yet
        }

        return new Badge(badgeName, iconPath);
    }

    // Method to display badge icon in a popup window
    public ImageView getBadgeIcon() {
        ImageView imageView = new ImageView(icon);
        imageView.setFitWidth(50); // Set desired width
        imageView.setFitHeight(50); // Set desired height
        return imageView;
    }

    public static int getCompletedLevelsCount() {
        return PrintController.readMasteredFlashcardsFromProgress();
    }

    // Getter for badge name
    public String getName() {
        return name;
    }
}