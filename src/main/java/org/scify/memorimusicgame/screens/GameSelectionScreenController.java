package org.scify.memorimusicgame.screens;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import org.scify.memorimusicgame.fx.FXAudioEngine;
import org.scify.memorimusicgame.MainOptions;
import org.scify.memorimusicgame.fx.FXRenderingEngine;
import org.scify.memorimusicgame.games_options.FindTheInstrumentOptions;
import org.scify.memorimusicgame.games_options.FindTheNoteOptions;
import org.scify.memorimusicgame.games_options.GameWithLevelsOptions;
import org.scify.memorimusicgame.games_options.FindInstrumentFamilyOptions;
import org.scify.memorimusicgame.fx.FXSceneHandler;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

/**
 * Created by pisaris on 11/10/2016.
 */
public class GameSelectionScreenController {
    public Button findInstrumentFamiliesGame;
    public Button findInstrumentGame;
    public Button findNoteGame;
    protected FXSceneHandler sceneHandler;

    protected FXAudioEngine audioEngine;

    public void setParameters(FXSceneHandler sceneHandler, Scene gameSelectionScene) {
        audioEngine = new FXAudioEngine();
        this.sceneHandler = sceneHandler;
        FXRenderingEngine.setGamecoverIcon(gameSelectionScene, "gameCoverImgContainer");
        sceneHandler.pushScene(gameSelectionScene);

        gameSelectionScene.lookup("#welcome").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_selection_screen/welcome.mp3", false);
            }
        });

        gameSelectionScene.lookup("#back").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_selection_screen/back.mp3", false);
            }
        });

        gameSelectionScene.lookup("#findInstrumentFamiliesGame").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_selection_screen/find_instrument_family.mp3", false);
            }
        });

        gameSelectionScene.lookup("#findInstrumentGame").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_selection_screen/find_instrument.mp3", false);
            }
        });

//        gameSelectionScene.lookup("#findNoteGame").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
//            if (newPropertyValue) {
//                audioEngine.pauseAndPlaySound("game_selection_screen/find_note.mp3", false);
//            }
//        });

        gameSelectionScene.lookup("#sponsors").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/main_screen/sponsor.mp3", false);
            }
        });
    }

    /**
     * Constructs an instance and redirects to the sponsors screen
     * @param keyEvent the keyboard event
     */
    public void playSponsorMessage(KeyEvent keyEvent) {
        if(keyEvent.getCode() == SPACE) {
            audioEngine.pauseAndPlaySound("screens/sponsor_screen/sponsor_message.mp3", false);
        }
        else if(keyEvent.getCode() == ESCAPE) {
            exitScreen();
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
            exitScreen();
        } else if (evt.getCode() == ESCAPE) {
            exitScreen();
        }
    }

    public void initializeGame(KeyEvent keyEvent) {
        if (keyEvent.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            GameWithLevelsOptions gameOptions = null;
            if (keyEvent.getSource() == findInstrumentFamiliesGame) {
                gameOptions = new FindInstrumentFamilyOptions();
                MainOptions.gameClassName = "FIND_INSTRUMENT_FAMILY";
            } else if (keyEvent.getSource() == findInstrumentGame) {
                gameOptions = new FindTheInstrumentOptions();
                MainOptions.gameClassName = "FIND_INSTRUMENT";
            } else if (keyEvent.getSource() == findNoteGame) {
                gameOptions = new FindTheNoteOptions();
                MainOptions.gameClassName = "FIND_NOTE";
            }
            System.err.println(gameOptions);

            new GameLevelsScreen(sceneHandler, gameOptions);
        } else if (keyEvent.getCode() == ESCAPE) {
            exitScreen();
        }

    }

    protected void exitScreen() {
        audioEngine.pauseCurrentlyPlayingAudios();
        sceneHandler.popScene();
    }
}
