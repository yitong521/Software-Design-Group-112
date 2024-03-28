package nl.vu.cs.softwaredesign;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

public class Audio extends Application {

    private TargetDataLine targetLine;
    private File outputFile;

    @Override
    public void start(Stage primaryStage) {
        Button startButton = new Button("Start Recording");
        Button stopButton = new Button("Stop Recording");
        Button playButton = new Button("Play Recording");

        VBox root = new VBox(startButton, stopButton, playButton);
        Scene scene = new Scene(root, 200, 150);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Audio Recorder");
        primaryStage.show();

        startButton.setOnAction(event -> startRecording());
        stopButton.setOnAction(event -> stopRecording());
        playButton.setOnAction(event -> playRecording());
    }

    private void startRecording() {
        try {
            AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, 44100, 16, 2, 4, 44100, false);
            DataLine.Info dataInfo = new DataLine.Info(TargetDataLine.class, audioFormat);
            if (!AudioSystem.isLineSupported(dataInfo)) {
                System.out.println("Not supported");
                return;
            }

            targetLine = (TargetDataLine) AudioSystem.getLine(dataInfo);
            targetLine.open();

            outputFile = new File("record.wav");

            targetLine.start();
            System.out.println("Recording started...");

            // Start a new thread to continuously capture audio data
            Thread captureThread = new Thread(() -> {
                try {
                    AudioInputStream recordingStream = new AudioInputStream(targetLine);
                    AudioSystem.write(recordingStream, AudioFileFormat.Type.WAVE, outputFile);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            });
            captureThread.start();
        } catch (Exception e) {
            System.out.println(e);
        }
    }

    private void stopRecording() {
        if (targetLine != null && targetLine.isOpen()) {
            targetLine.stop();
            targetLine.close();
            System.out.println("Recording stopped");
        }
    }

    private void playRecording() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(outputFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
        } catch (Exception ex) {
            System.out.println("Error playing recording: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
