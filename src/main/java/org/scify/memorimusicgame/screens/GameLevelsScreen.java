package org.scify.memorimusicgame.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.scify.memorimusicgame.games_options.GameWithLevelsOptions;
import org.scify.memorimusicgame.fx.FXSceneHandler;
import org.scify.memorimusicgame.helper.MemoriConfiguration;
import org.scify.memorimusicgame.helper.UTF8Control;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.scify.memorimusicgame.MainOptions.mHeight;
import static org.scify.memorimusicgame.MainOptions.mWidth;

public class GameLevelsScreen {

    protected FXSceneHandler sceneHandler;

    public GameLevelsScreen(FXSceneHandler shSceneHandler, GameWithLevelsOptions gameOptions) {
        sceneHandler = shSceneHandler;
        MemoriConfiguration configuration = new MemoriConfiguration();
        Locale locale = new Locale(configuration.getProjectProperty("APP_LANG"));
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/game_levels_screen.fxml"),
                ResourceBundle.getBundle("languages.strings", locale, new UTF8Control()));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene gameLevelsScene = new Scene(root, mWidth, mHeight);
        GameLevelsScreenController controller = loader.getController();

        controller.setParameters(sceneHandler, gameLevelsScene, gameOptions);

    }
}
