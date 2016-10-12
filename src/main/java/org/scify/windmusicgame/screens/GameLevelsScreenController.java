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
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static javafx.scene.input.KeyCode.SPACE;

/**
 * Created by pisaris on 12/10/2016.
 */
public class GameLevelsScreenController {

    public VBox gameLevelsContainer;

    protected SceneHandler sceneHandler;

    protected FXAudioEngine audioEngine;

    protected GameOptions gameOptions;

    public void setParameters(SceneHandler sceneHandler, Scene gameSelectionScene, GameWithLevelsOptions gameOptions) {
        audioEngine = new FXAudioEngine();
        this.sceneHandler = sceneHandler;
        this.gameOptions = (GameOptions) gameOptions;
        sceneHandler.pushScene(gameSelectionScene);

        gameSelectionScene.lookup("#gameDescription").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                //TODO: play the game description sound
                //audioEngine.pauseAndPlaySound("main_screen/welcome.wav", false);
            }
        });
        gameLevelsContainer = (VBox) gameSelectionScene.lookup("#gameLevels");
        addGameLevelButtons(gameLevelsContainer, gameOptions);
    }

    private void addGameLevelButtons(VBox buttonsContainer, GameWithLevelsOptions gameOpts) {
        Map<Integer, Point2D> gameLevelsToDimensions = gameOpts.getGameLevelToDimensions();
        for (Map.Entry<Integer, Point2D> gameLevelToDimensions : gameLevelsToDimensions.entrySet()) {
            System.out.println(gameLevelToDimensions.getKey() + "/" + gameLevelToDimensions.getValue());
            Point2D levelDimensions = gameLevelToDimensions.getValue();
            Button gameLevelBtn = new Button();
            gameLevelBtn.setText((int)levelDimensions.getX() + "x" + (int)levelDimensions.getY());
            gameLevelBtn.getStyleClass().add("optionButton");
            gameLevelBtn.setId(gameLevelToDimensions.getKey().toString());
            gameLevelBtn.setOnKeyTyped(new EventHandler<KeyEvent>() {
                @Override
                public void handle(KeyEvent event) {
                    System.out.println(event.getCode());
                    if(event.getCode() == SPACE){
                        System.err.println(gameLevelToDimensions.getKey());
                        MainOptions.gameLevel = gameLevelToDimensions.getKey();
                        startNormalGame();
                    }
                }
            });
            buttonsContainer.getChildren().add(gameLevelBtn);
        }
    }

    private void startNormalGame() {
        audioEngine.pauseCurrentlyPlayingAudios();
        FXMemoriGame game = new FXMemoriGame(sceneHandler);
        game.initialize();

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
//                else
//                    loadNextLevelForNormalGame();

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
            sceneHandler.popScene();
        }
    }
}
