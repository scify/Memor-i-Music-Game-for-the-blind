/**
 * Copyright 2016 SciFY.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scify.windmusicgame.screens;

import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.scify.windmusicgame.FXAudioEngine;
import org.scify.windmusicgame.HighScoreHandler;
import org.scify.windmusicgame.MainOptions;
import org.scify.windmusicgame.MemoriGameLevel;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static javafx.scene.input.KeyCode.SPACE;

public class FXHighScoresScreenController {

    /**
     * An Audio Engine object, able to play sounds
     */
    private HighScoreHandler highScoreHandler;
    private FXAudioEngine audioEngine;
    protected SceneHandler sceneHandler;
    protected GameWithLevelsOptions gameOptions;

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

    public void setParameters(SceneHandler sHandler, Scene scoresScene, GameWithLevelsOptions gameOptions) {
        //initialize the audio engine object
        audioEngine = new FXAudioEngine();
        this.gameOptions = gameOptions;
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

        scoresScene.lookup("#back").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("back.wav", false);
            }
        });

        addGameLevelButtons((VBox) scoresScene.lookup("#gameLevels"), this.gameOptions);
    }

    private void addGameLevelButtons(VBox buttonsContainer, GameWithLevelsOptions gameOpts) {
        List<MemoriGameLevel> allLevels = gameOpts.getGameLevels();

        for(MemoriGameLevel gameLevel: allLevels) {
            Point2D levelDimensions = gameLevel.getDimensions();

            Button gameLevelBtn = new Button();
            gameLevelBtn.setText((int) levelDimensions.getX() + "x" + (int) levelDimensions.getY());
            gameLevelBtn.getStyleClass().add("optionButton");
            gameLevelBtn.setId(gameLevel.getDimensions().toString());

            gameLevelBtn.setOnKeyPressed(event -> {
                if (event.getCode() == SPACE) {
                    MainOptions.gameScoresFile = this.gameOptions.scoresFile;
                    parseHighScore(gameLevel.getLevelCode());
                }
            });

            gameLevelBtn.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (newPropertyValue) {
                    audioEngine.pauseAndPlaySound(gameLevel.getIntroScreenSound(), false);
                }
            });

            buttonsContainer.getChildren().add(gameLevelBtn);
        }
    }

    /**
     * Fetch the score for a given game level
     * @param gameLevel the game level
     */
    @FXML
    protected void parseHighScore(int gameLevel) {

        System.err.println("high score: " + highScoreHandler.getHighScoreForLevel(gameLevel));
        String timestampStr = highScoreHandler.getHighScoreForLevel(gameLevel);
        // if there is a score in this level (ie if score is nt null)
        // play relevant audio clips
        // else play informative audio clip prompting to play the score
        if (timestampStr != null) {
            String[] tokens = timestampStr.split(":");
            int minutes = Integer.parseInt(tokens[1]);
            int seconds = Integer.parseInt(tokens[2]);
            if (minutes != 0) {
                audioEngine.playNumSound(minutes);
                System.out.println("minutes: " + minutes);
                if (minutes > 1)
                    audioEngine.pauseAndPlaySound("game_effects/minutes.wav", true);
                else
                    audioEngine.pauseAndPlaySound("game_effects/minute.wav", true);
            }
            if (minutes != 0 && seconds != 0)
                audioEngine.pauseAndPlaySound("game_effects/and.wav", true);
            if (seconds != 0) {
                audioEngine.playNumSound(seconds);
                if (seconds > 1)
                    audioEngine.pauseAndPlaySound("game_effects/seconds.wav", true);
                else
                    audioEngine.pauseAndPlaySound("game_effects/second.wav", true);
            }
        } else {
            audioEngine.pauseAndPlaySound("no_score.wav", false);
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
