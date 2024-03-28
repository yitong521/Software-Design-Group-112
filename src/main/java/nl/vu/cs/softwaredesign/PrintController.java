package nl.vu.cs.softwaredesign;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.google.gson.JsonObject;
import java.text.SimpleDateFormat;
import java.util.*;


import java.io.*;
import java.lang.reflect.Type;


    public class PrintController extends Application {

        private VBox layout;

        @Override
        public void start(Stage primaryStage) throws Exception {
            primaryStage.setTitle("Print Data");

            // Load flashcards and levels
            List<Flashcard> flashcards = Persistence.loadFlashcards("data/flashcards.json");
            List<Level> levels = Persistence.loadLevels();

            // Get the number of mastered flashcards and completed levels
            final int[] masteredFlashcardsCount = {Persistence.getFlashcardsMastered(flashcards)};
            final int[] completedLevelsCount = {Persistence.getLevelsCompleted(levels)};

            // Read progress from progress.json
            int masteredFlashcardsFromProgress = readMasteredFlashcardsFromProgress();
            int highestLevelReached = readHighestLevelReachedFromProgress();

            // Update mastered flashcards count if available in progress.json
            if (masteredFlashcardsFromProgress > masteredFlashcardsCount[0]) {
                masteredFlashcardsCount[0] = masteredFlashcardsFromProgress;
            }

            // Update completed levels count if available in progress.json
            if (highestLevelReached > completedLevelsCount[0]) {
                completedLevelsCount[0] = highestLevelReached;
            }

            // Create labels to display data
            Label masteredFlashcardsLabel = new Label("Mastered Flashcards: " + masteredFlashcardsCount[0]);
            Label completedLevelsLabel = new Label("Completed Levels: " + completedLevelsCount[0]);

            // Create button to close the window and print data
            Button closeButton = new Button("Close");
            Button printButton = new Button("Print Data");
            Button importButton = new Button("Import New Words");
            closeButton.setOnAction(e -> primaryStage.close());
            printButton.setOnAction(e -> {
                try {
                    printData(masteredFlashcardsCount[0], completedLevelsCount[0]);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            importButton.setOnAction(e -> importData());

            // Layout for print data window
            layout = new VBox(10); // 初始化layout
            layout.setPadding(new Insets(20));
            layout.getChildren().addAll(masteredFlashcardsLabel, completedLevelsLabel, printButton, importButton, closeButton);

            // Set the scene
            Scene scene = new Scene(layout, 300, 200);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static int readMasteredFlashcardsFromProgress() {
            int masteredFlashcardsCount = 0;
            try (FileReader reader = new FileReader("progress.json")) {
                Gson gson = new Gson();
                List<Flashcard> flashcards = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());
                for (Flashcard flashcard : flashcards) {
                    if (flashcard.isMastered()) {
                        masteredFlashcardsCount++;
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return masteredFlashcardsCount;
        }

    private int readHighestLevelReachedFromProgress() {
        int highestLevelReached = 0;
        try (FileReader reader = new FileReader("progress.json")) {
            Gson gson = new Gson();
            List<Flashcard> flashcards = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());
            for (Flashcard flashcard : flashcards) {
                if (flashcard.isMastered() && flashcard.getLevel() > highestLevelReached) {
                    highestLevelReached = flashcard.getLevel();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return highestLevelReached;
    }

    private void printMasteredFlashcards() throws IOException {
        // Load flashcards from progress.json
        List<Flashcard> masteredFlashcards = new ArrayList<>();
        try (FileReader reader = new FileReader("progress.json")) {
            Gson gson = new Gson();
            List<Flashcard> flashcards = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());
            for (Flashcard flashcard : flashcards) {
                if (flashcard.isMastered()) {
                    masteredFlashcards.add(flashcard);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert mastered flashcards to JSON array
        Gson gson = new Gson();
        JsonArray jsonArray = new JsonArray();
        for (Flashcard flashcard : masteredFlashcards) {
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("word", flashcard.getWord());
            jsonObject.addProperty("translation", flashcard.getTranslation());
            jsonObject.addProperty("mastered", true);
            jsonObject.addProperty("id", flashcard.getId());
            jsonObject.addProperty("level", flashcard.getLevel());
            jsonArray.add(jsonObject);
        }

        // Write JSON array to file
        try (FileWriter fileWriter = new FileWriter("Output/mastered_flashcards.json")) {
            gson.toJson(jsonArray, fileWriter);
        }
    }

        private void printData(int masteredFlashcardsCount, int completedLevelsCount) throws IOException {
            // Print mastered flashcards
            printMasteredFlashcards();

            // Determine badge name based on completed levels
            String badgeName;
            if (completedLevelsCount >= 3) {
                badgeName = "Platinum";
            } else if (completedLevelsCount >= 2) {
                badgeName = "Gold";
            } else if (completedLevelsCount >= 1) {
                badgeName = "Silver";
            } else {
                badgeName = "Please continue learning!";
            }

            // Create JSON object for mastered flashcards count, completed levels count, badge name, and print date
            Gson gson = new Gson();
            JsonObject jsonObject = new JsonObject();
            jsonObject.addProperty("Mastered Flashcards", masteredFlashcardsCount);
            jsonObject.addProperty("Completed Levels", completedLevelsCount);
            jsonObject.addProperty("Badge Name", badgeName); // Add badge name to JSON object
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            jsonObject.addProperty("Print Date", sdf.format(new Date()));

            // Convert JSON object to string
            String json = gson.toJson(jsonObject);

            // Write JSON string to file
            try (FileWriter fileWriter = new FileWriter("Output/output.json")) {
                fileWriter.write(json);
            }
        }

        private void importData() {
        try (Reader reader = new FileReader("data/addData.json")) {
            Gson gson = new Gson();
            List<Flashcard> newFlashcards = gson.fromJson(reader, new TypeToken<List<Flashcard>>(){}.getType());

            // Set mastered status to false for new flashcards
            for (Flashcard flashcard : newFlashcards) {
                flashcard.setMastered(false);
            }

            // Assign IDs to new flashcards
            int maxId = getMaxId();
            for (Flashcard flashcard : newFlashcards) {
                flashcard.setId(++maxId);
            }

            // Load existing flashcards
            List<Flashcard> existingFlashcards = Persistence.loadFlashcards("progress.json");

            // Combine new flashcards with existing ones, removing duplicates
            Set<Flashcard> combinedFlashcards = new LinkedHashSet<>(existingFlashcards);
            combinedFlashcards.addAll(newFlashcards); // Add new flashcards
            List<Flashcard> sortedFlashcards = new ArrayList<>(combinedFlashcards); // Convert set back to list

            // Sort combined flashcards by level in descending order
            sortedFlashcards.sort(Comparator.comparingInt(Flashcard::getLevel).reversed());

            // Save combined and sorted flashcards back to file
            Persistence.saveFlashcards(new LinkedHashSet<>(sortedFlashcards), "progress.json");

            // Display success message
            displaySuccessMessage("Successfully imported");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void displaySuccessMessage(String message) {
        // Display success message using a new label
        Label successLabel = new Label(message);
        // Add successLabel to your UI layout
        layout.getChildren().add(successLabel);
    }

    private int getMaxId() {
        // Load flashcards from progress.json
        List<Flashcard> flashcards = Persistence.loadFlashcards("progress.json");
        int maxId = 0;
        for (Flashcard flashcard : flashcards) {
            if (flashcard.getId() > maxId) {
                maxId = flashcard.getId();
            }
        }
        return maxId;
    }

    private List<Flashcard> sortFlashcardsByLevelDescending(Set<Flashcard> flashcards) {
        List<Flashcard> sortedFlashcards = new ArrayList<>(flashcards);
        sortedFlashcards.sort((flashcard1, flashcard2) -> Integer.compare(flashcard2.getLevel(), flashcard1.getLevel()));
        return sortedFlashcards;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
