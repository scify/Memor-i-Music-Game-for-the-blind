
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

package org.scify.windmusicgame.screens;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.helperClasses.FileHandler;
import org.scify.windmusicgame.helperClasses.SceneHandler;
import org.scify.windmusicgame.helperClasses.UTF8Control;
import org.scify.windmusicgame.interfaces.GameOptions;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.scify.windmusicgame.MainOptions.mHeight;
import static org.scify.windmusicgame.MainOptions.mWidth;

/**
 * JavaFX Screen constructor page
 */
public class FXHighScoresScreen implements org.scify.windmusicgame.interfaces.HighScoresScreen {

    protected SceneHandler sceneHandler;

    protected GameOptions gameOptions;

    public FXHighScoresScreen(SceneHandler shSceneHandler, GameOptions gameOptions) {
        this.sceneHandler = shSceneHandler;
        this.gameOptions = gameOptions;
        FileHandler fileHandler = new FileHandler();
        Locale locale = new Locale(fileHandler.getProjectProperty("APP_LANG"));

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/scores.fxml"),
                ResourceBundle.getBundle("languages.strings", locale, new UTF8Control()));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene scoresScene = new Scene(root, mWidth, mHeight);

        FXHighScoresScreenController controller = loader.getController();
        controller.setParameters(sceneHandler, scoresScene, (GameWithLevelsOptions) this.gameOptions);
    }

    @Override
    public void initialize() {

    }
}
