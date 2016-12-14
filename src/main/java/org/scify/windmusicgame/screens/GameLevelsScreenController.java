package org.scify.windmusicgame.screens;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import org.scify.windmusicgame.FXAudioEngine;
import org.scify.windmusicgame.FXMemoriGame;
import org.scify.windmusicgame.MainOptions;
import org.scify.windmusicgame.MemoriGameLevel;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

/**
 * Created by pisaris on 12/10/2016.
 */
public class GameLevelsScreenController {

    public VBox gameLevelsContainer;
    public Button gameDescription;

    protected SceneHandler sceneHandler;

    protected FXAudioEngine audioEngine;

    protected GameWithLevelsOptions gameOptions;

    public void setParameters(SceneHandler sceneHandler, Scene gameSelectionScene, GameWithLevelsOptions gameOptions) {
        audioEngine = new FXAudioEngine();
        this.sceneHandler = sceneHandler;
        this.gameOptions = gameOptions;
        sceneHandler.pushScene(gameSelectionScene);

        gameSelectionScene.lookup("#gameDescription").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound(gameOptions.getGameDescriptionSound(), false, true);
            }
        });

        gameSelectionScene.lookup("#tutorial").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_levels_screen/tutorial.mp3", false, true);
            }
        });

        gameSelectionScene.lookup("#scores").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_levels_screen/my_scores.mp3", false, true);
            }
        });

        gameSelectionScene.lookup("#back").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/game_levels_screen/back.mp3", false, true);
            }
        });

        gameSelectionScene.lookup("#exit").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("miscellaneous/exit.mp3", false, true);
            }
        });

        gameLevelsContainer = (VBox) gameSelectionScene.lookup("#gameLevels");
        gameDescription = (Button) gameSelectionScene.lookup("#gameDescription");
        gameDescription.setText(gameOptions.getGameDescription());
        addGameLevelButtons(gameLevelsContainer, gameOptions);
    }

    private void addGameLevelButtons(VBox buttonsContainer, GameWithLevelsOptions gameOpts) {
        List<MemoriGameLevel> allLevels = gameOpts.getGameLevels();

        for(MemoriGameLevel gameLevel: allLevels) {
            Point2D levelDimensions = gameLevel.getDimensions();

            Button gameLevelBtn = new Button();
            gameLevelBtn.setText((int)levelDimensions.getX() + "x" + (int)levelDimensions.getY());
            gameLevelBtn.getStyleClass().add("optionButton");
            gameLevelBtn.setId(gameLevel.getDimensions().toString());

            gameLevelBtn.setOnKeyPressed(event -> {
                if(event.getCode() == SPACE){

                    MainOptions.gameLevel = gameLevel.getLevelCode();
                    MainOptions.NUMBER_OF_ROWS = (int)levelDimensions.getX();
                    MainOptions.NUMBER_OF_COLUMNS = (int)levelDimensions.getY();
                    MainOptions.gameScoresFile = this.gameOptions.scoresFile;
                    Thread thread = new Thread(() -> startNormalGame());
                    thread.start();
                } else if(event.getCode() == ESCAPE) {
                    exitScreen();
                }
            });

            gameLevelBtn.focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
                if (newPropertyValue) {
                    audioEngine.pauseAndPlaySound(gameLevel.getIntroScreenSound(), false, true);
                }
            });

            buttonsContainer.getChildren().add(gameLevelBtn);
        }
    }

    private void startNormalGame() {
        audioEngine.pauseCurrentlyPlayingAudios();
        FXMemoriGame game = new FXMemoriGame(sceneHandler);
        game.initialize((GameOptions) gameOptions);

        // Run game in separate thread
        ExecutorService es  = Executors.newFixedThreadPool(1);
        Future<Integer> future = es.submit(game);
        es.shutdown();

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

    }

    @FXML
    private void startTutorial(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            MainOptions.gameLevel = gameOptions.getGameLevels().get(0).getLevelCode();
            MainOptions.NUMBER_OF_ROWS = (int) gameOptions.getGameLevels().get(0).getDimensions().getX();
            MainOptions.NUMBER_OF_COLUMNS = (int) gameOptions.getGameLevels().get(0).getDimensions().getY();
            MainOptions.gameScoresFile = this.gameOptions.scoresFile;
            MainOptions.TUTORIAL_MODE = true;
            Thread thread = new Thread(() -> startNormalGame());
            thread.start();
        } else if(evt.getCode() == ESCAPE) {
            exitScreen();
        }
    }


    private void loadNextLevelForNormalGame() {

        if(MainOptions.gameLevel < gameOptions.getGameLevels().size()) {
            MemoriGameLevel nextLevel = gameOptions.getGameLevels().get(MainOptions.gameLevel);
            MainOptions.gameLevel++;
            Point2D nextLevelDimensions = nextLevel.getDimensions();
            System.err.println("next level: " + nextLevelDimensions.getX() + ", " + nextLevelDimensions.getY());
            if (nextLevelDimensions != null) {
                MainOptions.NUMBER_OF_ROWS = (int) nextLevelDimensions.getX();
                MainOptions.NUMBER_OF_COLUMNS = (int) nextLevelDimensions.getY();
                startNormalGame();
            } else {
                exitScreen();
            }
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
        }
    }

    /**
     * Goes back to main screen
     * @param evt the keyboard event
     */
    @FXML
    protected void backToMainScreen(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            exitScreen();
        }
    }

    public void myScores(KeyEvent keyEvent) {
        audioEngine.pauseCurrentlyPlayingAudios();
        if (keyEvent.getCode() == SPACE) {
            new FXHighScoresScreen(sceneHandler, (GameOptions) this.gameOptions);
        } else if(keyEvent.getCode() == ESCAPE) {
            exitScreen();
        }
    }

    protected void exitScreen() {
        audioEngine.pauseCurrentlyPlayingAudios();
        sceneHandler.popScene();
    }
}
