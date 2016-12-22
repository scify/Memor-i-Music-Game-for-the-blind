package org.scify.windmusicgame.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.fx.FXSceneHandler;
import org.scify.windmusicgame.helper.MemoriConfiguration;
import org.scify.windmusicgame.helper.UTF8Control;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.scify.windmusicgame.MainOptions.mHeight;
import static org.scify.windmusicgame.MainOptions.mWidth;

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
