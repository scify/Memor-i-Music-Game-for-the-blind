
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
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static javafx.scene.input.KeyCode.SPACE;

public class MainScreenController implements Initializable {
    @FXML
    private Button tutorial;
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
    @FXML
    private Button fiveTimesSix;

    public MainScreenController() {
        System.err.println("Constructor running...");
    }

    protected Stage primaryStage;
    protected Scene primaryScene;
    protected SceneHandler sceneHandler = new SceneHandler();
    protected FXAudioEngine audioEngine = new FXAudioEngine();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void setParameters(Stage primaryStage, Scene primaryScene) {
        this.primaryScene = primaryScene;
        this.primaryStage = primaryStage;

        primaryScene.getStylesheets().add("css/style.css");
        primaryStage.show();
        primaryStage.requestFocus();
        //primaryStage.setFullScreen(true);
        primaryScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    System.err.println("END");
                    primaryStage.close();
                    break;
            }
        });
        sceneHandler.setMainWindow(primaryStage);
        sceneHandler.pushScene(primaryScene);

        primaryStage.show();

        primaryScene.lookup("#tutorial").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.playSound("tutorial.mp3");
            }
        });


        primaryScene.lookup("#threeTimesTwo").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.playSound("two_times_three.mp3");
            }
        });

        primaryScene.lookup("#twoTimesFour").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.playSound("two_times_four.mp3");
            }
        });

        primaryScene.lookup("#fourTimesThree").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.playSound("three_times_four.mp3");
            }
        });

        primaryScene.lookup("#fourTimesFour").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.playSound("four_times_four.mp3");
            }
        });

        primaryScene.lookup("#exit").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.playSound("four_times_four.mp3");
            }
        });

    }

    /**
     * Quits game
     * @param evt
     */
    @FXML
    protected void exitGame(KeyEvent evt) {
        System.exit(0);
    }

    /**
     * Depending on the button clicked, the Main options (number of columns and rows) are initialized and a new game starts
     * @param evt
     */
    @FXML
    protected void initializeGameOptions(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            if (evt.getSource() == fourTimesFour) {
                MainOptions.NUMBER_OF_COLUMNS = 4;
                MainOptions.NUMBER_OF_ROWS = 4;
            } else if (evt.getSource() == threeTimesTwo) {
                MainOptions.NUMBER_OF_COLUMNS = 3;
                MainOptions.NUMBER_OF_ROWS = 2;
            } else if (evt.getSource() == tutorial) {
                System.out.println("tutorial");
                MainOptions.TUTORIAL_MODE = true;
                MainOptions.NUMBER_OF_COLUMNS = 3;
                MainOptions.NUMBER_OF_ROWS = 2;
            } else if (evt.getSource() == fourTimesThree) {
                MainOptions.NUMBER_OF_COLUMNS = 4;
                MainOptions.NUMBER_OF_ROWS = 3;
            } else if(evt.getSource() == twoTimesFour) {
                MainOptions.NUMBER_OF_COLUMNS = 4;
                MainOptions.NUMBER_OF_ROWS = 2;
            } else if(evt.getSource() == fiveTimesFour) {
                MainOptions.NUMBER_OF_COLUMNS = 4;
                MainOptions.NUMBER_OF_ROWS = 5;
            } else if(evt.getSource() == fourTimesSix) {
                MainOptions.NUMBER_OF_COLUMNS = 6;
                MainOptions.NUMBER_OF_ROWS = 4;
            } else if(evt.getSource() == fiveTimesSix) {
                MainOptions.NUMBER_OF_COLUMNS = 5;
                MainOptions.NUMBER_OF_ROWS = 6;
            }
            //TODO: Ask ggianna
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    startNormalGame(MainOptions.NUMBER_OF_COLUMNS, MainOptions.NUMBER_OF_ROWS);
                }

            });

            thread.start();


        }
    }

    @FXML

    protected void myScores(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            FXHighScoresScreen highScoresScreen = new FXHighScoresScreen(sceneHandler, sceneHandler.getMainWindow());
        }
    }


    @FXML
    protected void headphonesAdjustment(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            audioEngine.playBalancedSound(-1.0, "leftHeadphone.mp3");
            audioEngine.playBalancedSound(1.0, "rightHeadphone.mp3");
        }
    }


    private void startNormalGame(int numOfCols, int numOfRows) {
        MainOptions.gameLevel = numOfRows + "x" + numOfCols;
        FXMemoriGame game = new FXMemoriGame(sceneHandler);
        game.initialize();
        // Run game in separate thread
        ExecutorService es  = Executors.newFixedThreadPool(1);
        Future<Integer> future = es.submit(game);
        es.shutdown();
        // While the game has not finished
        // sleep
//        while(!future.isDone()) {
//            try {
//                Thread.sleep(100L);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }

        try {
            Integer result = future.get();
            if(result == 1) {
                System.err.println("QUITING TO MAIN SCREEN");
                sceneHandler.popScene();
            } else if(result == 2) {
                sceneHandler.simplePopScene();
                startNormalGame(numOfCols++, numOfRows++);
            }
            System.out.println(result);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        // Get the result and act accordingly

//        try {
//            es.awaitTermination(1, TimeUnit.DAYS);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        game.finalize();

    }
}
