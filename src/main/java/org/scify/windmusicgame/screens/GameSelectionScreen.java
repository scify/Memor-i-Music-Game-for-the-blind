package org.scify.windmusicgame.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.scify.windmusicgame.helperClasses.SceneHandler;

import java.io.IOException;

import static org.scify.windmusicgame.MainOptions.mHeight;
import static org.scify.windmusicgame.MainOptions.mWidth;

/**
 * Created by pisaris on 11/10/2016.
 */
public class GameSelectionScreen {

    protected SceneHandler sceneHandler;

    public GameSelectionScreen(SceneHandler shSceneHandler) {
        sceneHandler = shSceneHandler;
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/game_selection_screen.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene gameSelectionScene = new Scene(root, mWidth, mHeight);
        GameSelectionScreenController controller = fxmlLoader.getController();

        controller.setParameters(sceneHandler, gameSelectionScene);

        gameSelectionScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    sceneHandler.popScene();
                    break;
            }
        });
    }
}
