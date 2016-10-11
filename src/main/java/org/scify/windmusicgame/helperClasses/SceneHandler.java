
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

import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles the scenes changing across the application.
 */
public class SceneHandler {
    public SceneHandler() {
        System.out.println("Initializing scene handler...");
    }

    /**
     * Main window that the application runs on (this application is a one-window application)
     */
    private Stage mainWindow;
    /**
     * History af scenes created and used in the application (ability to go back to a certain scene)
     */
    private List<Scene> allScenes = new ArrayList<>();

    public Stage getMainWindow() {
        return mainWindow;
    }

    public void setMainWindow(Stage mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * Set a given scene as the active one
     * @param sToPush the scene we want to make active
     */
    public void pushScene(Scene sToPush) {
        allScenes.add(sToPush);
        // Set the added scene as active

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainWindow.setScene(sToPush);
            }
        });
    }

    /**
     * Removes the last scene from the scenes list and sets the previous one as active
     * @return the last scene from the scenes list
     */
    public Scene  popScene() {
        Scene sToPop = allScenes.get(allScenes.size() - 1); // Get last added
        allScenes.remove(allScenes.size() - 1); // and pop it
        // update active scene IN FX ΤΗΕΜΕ
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                mainWindow.setScene(allScenes.get(allScenes.size() - 1));
            }
        });
        // WARNING: The above call is asynchronous, so we are not CERTAIN that
        // the mainWindow has already switched to the previous scene, at this point
        // in time.

        // return removed scene
        return sToPop;
    }

    /**
     * Removes the last scene from the scenes list
     */
    public void simplePopScene() {
        allScenes.remove(allScenes.size() - 1);
    }
}
