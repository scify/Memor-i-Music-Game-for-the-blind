package org.scify.windmusicgame.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.interfaces.GameOptions;

import java.io.IOException;

import static org.scify.windmusicgame.MainOptions.mHeight;
import static org.scify.windmusicgame.MainOptions.mWidth;

/**
 * Created by pisaris on 12/10/2016.
 */
public class GameLevelsScreen {

    protected SceneHandler sceneHandler;

    public GameLevelsScreen(SceneHandler shSceneHandler, GameWithLevelsOptions gameOptions) {
        sceneHandler = shSceneHandler;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game_levels_screen.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene gameLevelsScene = new Scene(root, mWidth, mHeight);
        GameLevelsScreenController controller = fxmlLoader.getController();

        controller.setParameters(sceneHandler, gameLevelsScene, gameOptions);

        gameLevelsScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    sceneHandler.popScene();
                    break;
            }
        });
    }
}
