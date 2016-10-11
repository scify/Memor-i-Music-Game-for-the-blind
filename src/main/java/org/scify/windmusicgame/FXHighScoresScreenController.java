/**
 * Copyright 2016 SciFY.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scify.memori;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;

import static javafx.scene.input.KeyCode.SPACE;

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
                audioEngine.pauseAndPlaySound("main_screen/level1.wav", false);
            }
        });

        scoresScene.lookup("#level2").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level2.wav", false);
            }
        });

        scoresScene.lookup("#level3").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level3.wav", false);
            }
        });

        scoresScene.lookup("#level4").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level4.wav", false);
            }
        });

        scoresScene.lookup("#level5").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level5.wav", false);
            }
        });

        scoresScene.lookup("#level6").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level6.wav", false);
            }
        });

        scoresScene.lookup("#level7").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level7.wav", false);
            }
        });

        scoresScene.lookup("#back").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("back.wav", false);
            }
        });
    }

    /**
     * Depending on the button clicked, the Main options (number of columns and rows) are initialized and a new game starts
     * @param evt the keyboard event
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
            System.err.println("high score: " + highScoreHandler.getHighScoreForLevel(level));
            String timestampStr = highScoreHandler.getHighScoreForLevel(level);
            // if there is a score in this level (ie if score is nt null)
            // play relevant audio clips
            // else play informative audio clip prompting to play the score
            if(timestampStr != null) {
                String[] tokens = timestampStr.split(":");
                int minutes = Integer.parseInt(tokens[1]);
                int seconds = Integer.parseInt(tokens[2]);
                if (minutes != 0) {
                    audioEngine.playNumSound(minutes);
                    System.out.println("minutes: " + minutes);
                    if(minutes > 1)
                        audioEngine.pauseAndPlaySound("game_effects/minutes.wav", true);
                    else
                        audioEngine.pauseAndPlaySound("game_effects/minute.wav", true);
                }
                if(minutes != 0 && seconds != 0)
                    audioEngine.pauseAndPlaySound("game_effects/and.wav", true);
                if (seconds != 0) {
                    audioEngine.playNumSound(seconds);
                    if(seconds > 1)
                        audioEngine.pauseAndPlaySound("game_effects/seconds.wav", true);
                    else
                        audioEngine.pauseAndPlaySound("game_effects/second.wav", true);
                }
            } else {
                audioEngine.pauseAndPlaySound("no_score.wav", false);
            }
        }
    }

    /**
     * Goes back to main screen
     * @param evt the keyboard event
     */
    @FXML
    protected void backToMainScreen(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            sceneHandler.popScene();
        }
    }
}
