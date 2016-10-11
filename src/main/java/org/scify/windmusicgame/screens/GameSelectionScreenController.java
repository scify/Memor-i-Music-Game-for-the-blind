package org.scify.windmusicgame.screens;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import org.scify.windmusicgame.FXAudioEngine;
import org.scify.windmusicgame.helperClasses.SceneHandler;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

/**
 * Created by pisaris on 11/10/2016.
 */
public class GameSelectionScreenController {
    protected SceneHandler sceneHandler;

    protected FXAudioEngine audioEngine;

    public GameSelectionScreenController() {
        System.err.println("Constructor running...");
        System.err.println("SELF:" + this.toString() + " from " + Thread.currentThread().toString());
    }

    public void setParameters(SceneHandler sceneHandler, Scene gameSelectionScene) {
        audioEngine = new FXAudioEngine();
        this.sceneHandler = sceneHandler;
        sceneHandler.pushScene(gameSelectionScene);

        gameSelectionScene.lookup("#welcome").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: uncomment
                //audioEngine.pauseAndPlaySound("main_screen/welcome.wav", false);
            }
        });
    }

    /**
     * Constructs an instance and redirects to the sponsors screen
     * @param keyEvent the keyboard event
     */
    public void goToSponsorsScreen(KeyEvent keyEvent) {
        System.err.println("SELF:" + this.toString() + " from " + Thread.currentThread().toString());
        if (keyEvent.getCode() == SPACE) {
            new SponsorsScreen(sceneHandler, sceneHandler.getMainWindow());
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    /**
     * Goes back to main screen
     * @param evt the keyboard event
     */
    @FXML
    protected void backToMainScreen(KeyEvent evt) {
        System.err.println("SELF:" + this.toString() + " from " + Thread.currentThread().toString());
        if (evt.getCode() == SPACE) {
            sceneHandler.popScene();
        }
    }

    public void initializeGame(KeyEvent keyEvent) {
    }
}
