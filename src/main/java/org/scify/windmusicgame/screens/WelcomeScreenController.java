package org.scify.windmusicgame.screens;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyEvent;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.scify.windmusicgame.FXAudioEngine;
import org.scify.windmusicgame.helperClasses.SceneHandler;

import static javafx.scene.input.KeyCode.ESCAPE;
import static javafx.scene.input.KeyCode.SPACE;

/**
 * Controller class for the welcome screen
 * Created by pisaris on 11/10/2016.
 */
public class WelcomeScreenController {
    protected Stage primaryStage;
    protected Scene primaryScene;
    protected SceneHandler sceneHandler = new SceneHandler();
    protected FXAudioEngine audioEngine = new FXAudioEngine();

    public WelcomeScreenController() {
        System.err.println("Constructor running...");
    }

    public void setParameters(Stage primaryStage, Scene primaryScene) {
        this.primaryScene = primaryScene;
        this.primaryStage = primaryStage;

        primaryScene.getStylesheets().add("css/style.css");
        primaryStage.show();
        primaryStage.requestFocus();
        primaryStage.setFullScreen(true);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            public void handle(WindowEvent we) {
                System.out.println("Stage is closing");
                System.exit(0);
            }
        });

        Screen screen = Screen.getPrimary();
        Rectangle2D bounds = screen.getVisualBounds();

        primaryStage.setX(bounds.getMinX());
        primaryStage.setY(bounds.getMinY());
        primaryStage.setWidth(bounds.getWidth());
        primaryStage.setHeight(bounds.getHeight());

        primaryStage.getIcons().add(new Image("/img/windmusicgame_large.png"));
        sceneHandler.setMainWindow(primaryStage);
        sceneHandler.pushScene(primaryScene);

        primaryScene.lookup("#welcome").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/main_screen/welcome.mp3", false, true);
            }
        });

        primaryStage.show();

        primaryScene.lookup("#headphonesAdjustment").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/main_screen/headphones_adjustment.mp3", false, true);
            }
        });

        primaryScene.lookup("#gameSelection").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/main_screen/game_selection.mp3", false, true);
            }
        });

        primaryScene.lookup("#sponsors").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/main_screen/sponsor.mp3", false, true);
            }
        });

        primaryScene.lookup("#exit").focusedProperty().addListener((arg0, oldPropertyValue, newPropertyValue) -> {
            if (newPropertyValue) {
                audioEngine.pauseAndPlaySound("screens/main_screen/exit.mp3", false, true);
            }
        });
    }


    public void selectGame(KeyEvent keyEvent) {
        if (keyEvent.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            new GameSelectionScreen(sceneHandler);
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    @FXML
    protected void headphonesAdjustment(KeyEvent evt) {
        if (evt.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            audioEngine.playBalancedSound(-1.0, "screens/main_screen/left_headphone.mp3", true);
            audioEngine.playBalancedSound(1.0, "screens/main_screen/right_headphone.mp3", true);
        } else if (evt.getCode() == ESCAPE) {
            System.exit(0);
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
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }

    /**
     * Constructs an instance and redirects to the sponsors screen
     * @param keyEvent the keyboard event
     */
    public void goToSponsorsScreen(KeyEvent keyEvent) {
        if (keyEvent.getCode() == SPACE) {
            audioEngine.pauseCurrentlyPlayingAudios();
            new SponsorsScreen(sceneHandler, sceneHandler.getMainWindow());
        } else if (keyEvent.getCode() == ESCAPE) {
            System.exit(0);
        }
    }
}
