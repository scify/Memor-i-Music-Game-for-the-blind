
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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.scify.memori.interfaces.HighScoresScreen;

import java.io.IOException;

import static javafx.scene.input.KeyCode.SPACE;
import static org.scify.memori.MainOptions.mHeight;
import static org.scify.memori.MainOptions.mWidth;

public class FXHighScoresScreen implements HighScoresScreen {

    private HighScoreHandler highScoreHandler;
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
        SceneHandler.mainWindow = mainWindow;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/scores.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }

        //initialize the audio engine object
        fxAudioEngine = new FXAudioEngine();

        scoresScene = new Scene(root, mWidth, mHeight);
        SceneHandler.pushScene(scoresScene);
        scoresScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    SceneHandler.popScene();
                    break;
            }
        });


    }

    @Override
    public void initialize() {

    }

    public FXHighScoresScreen() {
        highScoreHandler = new HighScoreHandler();
    }

    /**
     * Depending on the button clicked, the Main options (number of columns and rows) are initialized and a new game starts
     * @param evt
     */
    @FXML
    protected void parseHighScore(KeyEvent evt) {

        if (evt.getCode() == SPACE) {
            String level = "";
            if (evt.getSource() == fourTimesFour) {
                System.out.println("score for: 4x4");
                level = "4x4";
            } else if (evt.getSource() == threeTimesTwo) {
                System.out.println("score for: 2x3");
                level = "2x3";
            } else if (evt.getSource() == fourTimesThree) {
                System.out.println("score for: 3x4");
                level = "3x4";
            } else if(evt.getSource() == twoTimesFour) {
                System.out.println("score for: 2x4");
                level = "2x4";
            } else if(evt.getSource() == fiveTimesFour) {
                System.out.println("score for: 5x4");
                level = "5x4";
            } else if(evt.getSource() == fourTimesSix) {
                System.out.println("score for: 4x6");
                level = "4x6";
            }
            //startNormalGame(evt, MainOptions.NUMBER_OF_COLUMNS, MainOptions.NUMBER_OF_ROWS);
            System.err.println("high score: " + highScoreHandler.getHighScoreForLevel(level));
        }
    }

    /**
     * Goes back to main screen
     * @param evt
     */
    @FXML
    protected void backToMainScreen(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            SceneHandler.popScene();
        }
    }


}
