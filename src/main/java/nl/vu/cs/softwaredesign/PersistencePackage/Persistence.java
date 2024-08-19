package nl.vu.cs.softwaredesign.PersistencePackage;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import nl.vu.cs.softwaredesign.FlashcardPackage.Flashcard;
import nl.vu.cs.softwaredesign.LevelPackage.Level;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Persistence {
    private static final String LEVELS_FILE = "data/flashcards.json";

    // Load levels from JSON file
    public static List<Level> loadLevels() {
        List<Level> levels = new ArrayList<>();
        try (Reader reader = new FileReader(LEVELS_FILE)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Level>>(){}.getType();
            levels = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return levels;
    }

    // Save flashcards to JSON file
    public static void saveFlashcards(Set<Flashcard> flashcards, String filePath) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(filePath)) {
            gson.toJson(flashcards, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Load flashcards from JSON file
    public static List<Flashcard> loadFlashcards(String filePath) {
        List<Flashcard> flashcards = new ArrayList<>();
        try (Reader reader = new FileReader(filePath)) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Flashcard>>(){}.getType();
            flashcards = gson.fromJson(reader, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return flashcards;
    }

    // Returns the number of mastered flashcards
    public static int getFlashcardsMastered(List<Flashcard> flashcards) {
        int count = 0;
        for (Flashcard flashcard : flashcards) {
            if (flashcard.isMastered()) {
                count++;
            }
        }
        return count;
    }

    // Returns the number of completed levels
    public static int getLevelsCompleted(List<Level> levels) {
        int count = 0;
        for (Level level : levels) {
            if (level.isUnlocked() && level.countUnlockedFlashcards() == level.getFlashcards().size()) {
                count++;
            }
        }
        return count;
    }

    public void saveProgress(Flashcard[] flashcards, int currentIndex) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();

        String json = gson.toJson(flashcards);

        try (FileWriter fileWriter = new FileWriter("progress.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
