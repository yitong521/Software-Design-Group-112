package nl.vu.cs.softwaredesign.LevelPackage;


import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import nl.vu.cs.softwaredesign.FlashcardPackage.CreatCardsManually;
import nl.vu.cs.softwaredesign.FlashcardPackage.DeleteFlashCards;
import nl.vu.cs.softwaredesign.FlashcardPackage.EditFlashcards;


public class AdjustMode extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("Adjust Flashcard & Level");

        // Create button to open CreatCardsManually page
        Button manualCreateButton = new Button("Add Words Manually");
        manualCreateButton.setOnAction(e -> {
            // Open CreatCardsManually page
            CreatCardsManually creatCardsManually = new CreatCardsManually();
            try {
                creatCardsManually.start(new Stage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // Create button to open DeleteFlashCards page
        Button deleteButton = new Button("Delete FlashCards");
        deleteButton.setOnAction(e -> {
            // Open DeleteFlashCards page
            DeleteFlashCards deleteFlashCards = new DeleteFlashCards();
            try {
                deleteFlashCards.start(new Stage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // Create button to open EditFlashcards page
        Button editButton = new Button("Edit Flashcards");
        editButton.setOnAction(e -> {
            // Open EditFlashcards page
            EditFlashcards editFlashcards = new EditFlashcards();
            try {
                editFlashcards.start(new Stage());
            } catch (Exception exception) {
                exception.printStackTrace();
            }
        });

        // Create button to quit and return to main menu
        Button quitButton = new Button("Quit");
        quitButton.setOnAction(e -> primaryStage.close());

        // Layout for adjust mode window
        VBox adjustLayout = new VBox(10);
        adjustLayout.setPadding(new Insets(20));
        adjustLayout.getChildren().addAll( manualCreateButton, deleteButton, editButton, quitButton);

        // Set the scene
        Scene scene = new Scene(adjustLayout, 300, 250);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
