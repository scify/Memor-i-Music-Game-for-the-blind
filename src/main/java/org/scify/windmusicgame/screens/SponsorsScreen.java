
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
import javafx.stage.Stage;
import org.scify.windmusicgame.SceneHandler;
import org.scify.windmusicgame.interfaces.HighScoresScreen;

import java.io.IOException;

import static org.scify.windmusicgame.MainOptions.mHeight;
import static org.scify.windmusicgame.MainOptions.mWidth;

/**
 * JavaFX Screen constructor page
 */
public class SponsorsScreen implements HighScoresScreen {

    protected SceneHandler sceneHandler;

    public SponsorsScreen(SceneHandler shSceneHandler, Stage mainWindow) {
        this.sceneHandler = shSceneHandler;
        sceneHandler.setMainWindow(mainWindow);
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/sponsors.fxml"));
        Parent root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Scene sponsorsScene = new Scene(root, mWidth, mHeight);
        sceneHandler.pushScene(sponsorsScene);

        sponsorsScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    sceneHandler.popScene();
                    break;
            }
        });
    }

    @Override
    public void initialize() {

    }
}
