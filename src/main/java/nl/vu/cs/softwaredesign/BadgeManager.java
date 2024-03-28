package nl.vu.cs.softwaredesign;


import java.util.ArrayList;
import java.util.List;

public class BadgeManager {
    private static List<Badge> earnedBadges = new ArrayList<>();

    // Method to generate and add a badge based on the number of completed levels
    public static void generateBadgeAndUpdate(int totalLevelsCompleted) {
        Badge badge = Badge.generateBadge(totalLevelsCompleted);
        if (badge != null) {
            earnedBadges.add(badge);
        }
    }

    // Method to get the total number of badges achieved
    public static int getTotalBadgesAchieved() {
        return earnedBadges.size();
    }

    // Method to get all earned badges
    public static List<Badge> getEarnedBadges() {
        return earnedBadges;
    }
}
