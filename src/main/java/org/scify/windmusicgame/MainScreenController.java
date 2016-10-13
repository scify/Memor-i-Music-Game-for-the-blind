
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

package org.scify.windmusicgame;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.screens.SponsorsScreen;

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

public class MainScreenController implements Initializable {
    @FXML
    private Button tutorial;
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

    private Map<Integer, Point2D> gameLevelToDimensions = new HashMap<>();

    public MainScreenController() {
        System.err.println("Constructor running...");
    }

    protected Stage primaryStage;
    protected Scene primaryScene;
    protected SceneHandler sceneHandler = new SceneHandler();
    protected FXAudioEngine audioEngine = new FXAudioEngine();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        gameLevelToDimensions.put(1, new Point2D.Double(2,3));
        gameLevelToDimensions.put(2, new Point2D.Double(2,4));
        gameLevelToDimensions.put(3, new Point2D.Double(3,4));
        gameLevelToDimensions.put(4, new Point2D.Double(4,4));
        gameLevelToDimensions.put(5, new Point2D.Double(5,4));
        gameLevelToDimensions.put(6, new Point2D.Double(4,6));
        gameLevelToDimensions.put(7, new Point2D.Double(5,6));
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

        primaryScene.lookup("#tutorial").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/tutorial.wav", false);
            }
        });


        primaryScene.lookup("#level1").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level1.wav", false);
            }
        });

        primaryScene.lookup("#level2").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level2.wav", false);
            }
        });

        primaryScene.lookup("#level3").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level3.wav", false);
            }
        });

        primaryScene.lookup("#level4").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level4.wav", false);
            }
        });

        primaryScene.lookup("#level5").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level5.wav", false);
            }
        });

        primaryScene.lookup("#level6").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level6.wav", false);
            }
        });

        primaryScene.lookup("#level7").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/level7.wav", false);
            }
        });

        primaryScene.lookup("#myScores").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/my_scores.wav", false);
            }
        });

        primaryScene.lookup("#exit").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/exit.wav", false);
            }
        });

        primaryScene.lookup("#sponsors").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("main_screen/sponsors.wav", false);
            }
        });

    }

    /**
     * Quits game
     * @param evt the keyboard event
     */
    @FXML
    protected void exitGame(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            System.exit(0);
        }
    }

    /**
     * Depending on the button clicked, the Main options (number of columns and rows) are initialized and a new game starts
     * @param evt the keyboard event
     */
    @FXML
    protected void initializeGameOptions(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            if (evt.getSource() == tutorial) {
                MainOptions.TUTORIAL_MODE = true;
                MainOptions.gameLevel = 1;
            } else if (evt.getSource() == level1) {
                MainOptions.gameLevel = 1;
            } else if (evt.getSource() == level2) {
                MainOptions.gameLevel = 2;
            } else if (evt.getSource() == level3) {
                MainOptions.gameLevel = 3;
            } else if(evt.getSource() == level4) {
                MainOptions.gameLevel = 4;
            } else if(evt.getSource() == level5) {
                MainOptions.gameLevel = 5;
            } else if(evt.getSource() == level6) {
                MainOptions.gameLevel = 6;
            } else if(evt.getSource() == level7) {
                MainOptions.gameLevel = 7;
            }
            MainOptions.NUMBER_OF_ROWS = (int) gameLevelToDimensions.get(MainOptions.gameLevel).getX();
            MainOptions.NUMBER_OF_COLUMNS = (int) gameLevelToDimensions.get(MainOptions.gameLevel).getY();
            Thread thread = new Thread(new Runnable() {

                @Override
                public void run() {
                    startNormalGame();
                }

            });

            thread.start();


        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    @FXML
    protected void myScores(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            FXHighScoresScreen highScoresScreen = new FXHighScoresScreen(sceneHandler, sceneHandler.getMainWindow());
        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    @FXML
    protected void goToSponsorsPage(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            SponsorsScreen highScoresScreen = new SponsorsScreen(sceneHandler, sceneHandler.getMainWindow());
        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
        }
    }


    @FXML
    protected void headphonesAdjustment(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            audioEngine.playBalancedSound(-1.0, "main_screen/left_headphone.wav");
            audioEngine.playBalancedSound(1.0, "main_screen/right_headphone.wav");
        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    private void startNormalGame() {
        audioEngine.pauseCurrentlyPlayingAudios();
        FXMemoriGame game = new FXMemoriGame(sceneHandler);
        //game.initialize();

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
            //quit to main screen
            if(result == 1) {
                System.err.println("QUITING TO MAIN SCREEN");
                if(MainOptions.TUTORIAL_MODE)
                    MainOptions.TUTORIAL_MODE = false;
                sceneHandler.popScene();
            } else if(result == 2) // load next level
                 {
                sceneHandler.simplePopScene();
                if(MainOptions.TUTORIAL_MODE) {
                    //if the last game was in tutorial mode, load the first normal game
                    MainOptions.TUTORIAL_MODE = false;
                    startNormalGame();
                }
                else
                    loadNextLevelForNormalGame();

            } else if(result == 3) //play same level again
            {
                sceneHandler.simplePopScene();
                startNormalGame();
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

    private void loadNextLevelForNormalGame() {
        MainOptions.gameLevel++;
        Point2D nextLevelDimensions = gameLevelToDimensions.get(MainOptions.gameLevel);
        System.err.println("next level: " + nextLevelDimensions.getX() + ", " + nextLevelDimensions.getY());
        if(nextLevelDimensions != null) {
            MainOptions.NUMBER_OF_ROWS = (int) nextLevelDimensions.getX();
            MainOptions.NUMBER_OF_COLUMNS = (int) nextLevelDimensions.getY();
            startNormalGame();
        } else {
            sceneHandler.popScene();
        }
    }
}
