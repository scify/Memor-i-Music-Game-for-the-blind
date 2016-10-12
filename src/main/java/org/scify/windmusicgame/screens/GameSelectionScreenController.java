package org.scify.windmusicgame.screens;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import org.scify.windmusicgame.FXAudioEngine;
import org.scify.windmusicgame.games_options.FindTheInstrumentOptions;
import org.scify.windmusicgame.games_options.FindTheNoteOptions;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.games_options.InstrumentFamiliesOptions;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.interfaces.GameOptions;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

/**
 * Created by pisaris on 11/10/2016.
 */
public class GameSelectionScreenController {
    public Button findInstrumentFamiliesGame;
    public Button findInstrumentGame;
    public Button findNoteGame;
    protected SceneHandler sceneHandler;

    protected FXAudioEngine audioEngine;

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
        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    public void initializeGame(KeyEvent keyEvent) {
        if (keyEvent.getCode() == SPACE) {
            GameWithLevelsOptions gameOptions = null;
            if (keyEvent.getSource() == findInstrumentFamiliesGame) {
                gameOptions = new InstrumentFamiliesOptions();
            } else if (keyEvent.getSource() == findInstrumentGame) {
                gameOptions = new FindTheInstrumentOptions();
            } else if (keyEvent.getSource() == findNoteGame) {
                gameOptions = new FindTheNoteOptions();
            }
            System.err.println(gameOptions);

            new GameLevelsScreen(sceneHandler, gameOptions);
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }

    }
}
