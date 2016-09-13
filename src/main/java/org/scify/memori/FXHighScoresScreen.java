package org.scify.memori;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import org.scify.memori.interfaces.HighScoresScreen;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static javafx.scene.input.KeyCode.SPACE;
import static org.scify.memori.MainOptions.mHeight;
import static org.scify.memori.MainOptions.mWidth;

public class FXHighScoresScreen implements HighScoresScreen {

    //TODO: create stage manager class
    static Stage mainWindow;
    static List<Scene> allScenes;
    /**
     * JavFX component to bind the scene with the .fxml and .css file
     */
    protected Parent root;

    /**
     * An Audio Engine object, able to play sounds
     */
    private FXAudioEngine fxAudioEngine;
    private Scene scoresScene;

    @FXML
    private Button threeTimesTwo;
    @FXML
    private Button fourTimesFour;
    @FXML
    private Button fourTimesThree;
    @FXML
    private Button twoTimesFour;
    @FXML
    private Button fiveTimesFour;
    @FXML
    private Button fourTimesSix;

    public FXHighScoresScreen(Stage mainWindow) {
        this.mainWindow = mainWindow;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/scores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        allScenes = new ArrayList<>();
        allScenes.add(mainWindow.getScene()); // Initialize with main scene

        //initialize the audio engine object
        fxAudioEngine = new FXAudioEngine();

        scoresScene = new Scene(root, mWidth, mHeight);
        pushScene(scoresScene);
    }

    @Override
    public void initialize() {

    }

    public FXHighScoresScreen() {

    }

    /**
     * Depending on the button clicked, the Main options (number of columns and rows) are initialized and a new game starts
     * @param evt
     */
    @FXML
    protected void parseHighScore(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            if (evt.getSource() == fourTimesFour) {
                System.out.println("score for: 4x4");
            } else if (evt.getSource() == threeTimesTwo) {
                System.out.println("score for: 2x3");
            } else if (evt.getSource() == fourTimesThree) {
                System.out.println("score for: 3x4");
            } else if(evt.getSource() == twoTimesFour) {
                System.out.println("score for: 2x4");
            } else if(evt.getSource() == fiveTimesFour) {
                System.out.println("score for: 5x4");
            } else if(evt.getSource() == fourTimesSix) {
                System.out.println("score for: 4x6");
            }
            //startNormalGame(evt, MainOptions.NUMBER_OF_COLUMNS, MainOptions.NUMBER_OF_ROWS);
        }
    }

    /**
     * Goes back to main screen
     * @param evt
     */
    @FXML
    protected void backToMainScreen(KeyEvent evt) {
        popScene();
    }

    public void pushScene(Scene sToPush) {
        allScenes.add(sToPush);
        // Set the added scene as active
        mainWindow.setScene(sToPush);
    }

    public Scene  popScene() {
        Scene sToPop = allScenes.get(allScenes.size() - 1); // Get last added
        allScenes.remove(allScenes.size() - 1); // and pop it
        // update active scene
        mainWindow.setScene(allScenes.get(allScenes.size() - 1));
        // return removed scene
        return sToPop;
    }
}
