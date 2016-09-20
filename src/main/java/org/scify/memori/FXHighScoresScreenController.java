package org.scify.memori;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import static javafx.scene.input.KeyCode.SPACE;

/**
 * Created by pisaris on 20/9/2016.
 */
public class FXHighScoresScreenController {

    /**
     * An Audio Engine object, able to play sounds
     */
    private HighScoreHandler highScoreHandler;
    private FXAudioEngine audioEngine;
    protected SceneHandler sceneHandler;

    @FXML
    private Button level1;
    @FXML
    private Button level2;
    @FXML
    private Button level3;
    @FXML
    private Button level4;
    @FXML
    private Button level5;
    @FXML
    private Button level6;
    @FXML
    private Button level7;

    public void setParameters(SceneHandler sHandler, Scene scoresScene) {
        //initialize the audio engine object
        audioEngine = new FXAudioEngine();
        highScoreHandler = new HighScoreHandler();
        sceneHandler = sHandler;
        sceneHandler.pushScene(scoresScene);
        scoresScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    sceneHandler.popScene();
                    break;
            }
        });

        scoresScene.lookup("#level1").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: change file name to level-like
                audioEngine.pauseAndPlaySound("two_times_three.mp3", false);
            }
        });

        scoresScene.lookup("#level2").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: change file name to level-like
                audioEngine.pauseAndPlaySound("two_times_four.mp3", false);
            }
        });

        scoresScene.lookup("#level3").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: change file name to level-like
                audioEngine.pauseAndPlaySound("three_times_four.mp3", false);
            }
        });

        scoresScene.lookup("#level4").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: change file name to level-like
                audioEngine.pauseAndPlaySound("four_times_four.mp3", false);
            }
        });

        scoresScene.lookup("#back").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: change file name to exit
                audioEngine.pauseAndPlaySound("four_times_four.mp3", false);
            }
        });
    }

    /**
     * Depending on the button clicked, the Main options (number of columns and rows) are initialized and a new game starts
     * @param evt
     */
    @FXML
    protected void parseHighScore(KeyEvent evt) {

        if (evt.getCode() == SPACE) {
            int level = 0;
            if (evt.getSource() == level1) {
                level = 1;
            } else if (evt.getSource() == level2) {
                level = 2;
            } else if (evt.getSource() == level3) {
                level = 3;
            } else if(evt.getSource() == level4) {
                level = 4;
            } else if(evt.getSource() == level5) {
                level = 5;
            } else if(evt.getSource() == level6) {
                level = 6;
            } else if(evt.getSource() == level7) {
                level = 7;
            }
            //startNormalGame(evt, MainOptions.NUMBER_OF_COLUMNS, MainOptions.NUMBER_OF_ROWS);
            System.err.println("high score: " + highScoreHandler.getHighScoreForLevel(level));
            String timestampStr = highScoreHandler.getHighScoreForLevel(level);
            String[] tokens = timestampStr.split(":");
            int minutes = Integer.parseInt(tokens[1]);
            int seconds = Integer.parseInt(tokens[2]);
            //TODO: handle singular or plural minutes and seconds
            if(minutes != 0)
                audioEngine.playNumSound(minutes);
            if(seconds != 0)
                audioEngine.playNumSound(seconds);
        }
    }

    /**
     * Goes back to main screen
     * @param evt
     */
    @FXML
    protected void backToMainScreen(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            sceneHandler.popScene();
        }
    }
}
