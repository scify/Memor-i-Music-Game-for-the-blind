package org.scify.windmusicgame.screens;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.scify.windmusicgame.FXAudioEngine;
import org.scify.windmusicgame.helperClasses.SceneHandler;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

/**
 * Controller class for the welcome screen
 * Created by pisaris on 11/10/2016.
 */
public class WelcomeScreenController {
    protected Stage primaryStage;
    protected Scene primaryScene;
    protected SceneHandler sceneHandler = new SceneHandler();
    protected FXAudioEngine audioEngine = new FXAudioEngine();

    public WelcomeScreenController() {
        System.err.println("Constructor running...");
    }

    public void setParameters(Stage primaryStage, Scene primaryScene) {
        this.primaryScene = primaryScene;
        this.primaryStage = primaryStage;

        primaryScene.getStylesheets().add("css/style.css");
        primaryStage.show();
        primaryStage.requestFocus();
        primaryStage.setFullScreen(true);
        sceneHandler.setMainWindow(primaryStage);
        sceneHandler.pushScene(primaryScene);

        primaryScene.lookup("#welcome").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/welcome.wav", false);
            }
        });

        primaryStage.show();

        primaryScene.lookup("#headphonesAdjustment").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/headphones_adjustment.wav", false);
            }
        });

        primaryScene.lookup("#gameSelection").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/game_selection.wav", false);
            }
        });

        primaryScene.lookup("#sponsors").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/sponsor.wav", false);
            }
        });

        primaryScene.lookup("#exit").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/exit.wav", false);
            }
        });
    }


    public void selectGame(KeyEvent keyEvent) {
        if (keyEvent.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            new GameSelectionScreen(sceneHandler);
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    @FXML
    protected void headphonesAdjustment(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            audioEngine.playBalancedSound(-1.0, "main_screen/left_headphone.wav");
            audioEngine.playBalancedSound(1.0, "main_screen/right_headphone.wav");
        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    /**
     * Quits game
     * @param keyEvent the keyboard event
     */
    public void exitGame(KeyEvent keyEvent) {
        audioEngine.pauseCurrentlyPlayingAudios();
        if (keyEvent.getCode() == SPACE) {
            System.exit(0);
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    /**
     * Constructs an instance and redirects to the sponsors screen
     * @param keyEvent the keyboard event
     */
    public void goToSponsorsScreen(KeyEvent keyEvent) {
        if (keyEvent.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            new SponsorsScreen(sceneHandler, sceneHandler.getMainWindow());
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }
}
