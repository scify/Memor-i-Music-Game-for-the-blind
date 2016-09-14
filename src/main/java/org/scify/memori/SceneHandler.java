
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

package org.scify.memori;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SceneHandler {

    static Stage mainWindow;
    static List<Scene> allScenes = new ArrayList<>();

    public static void pushScene(Scene sToPush) {
        allScenes.add(sToPush);
        // Set the added scene as active
        mainWindow.setScene(sToPush);
    }

    public static Scene  popScene() {
        Scene sToPop = allScenes.get(allScenes.size() - 1); // Get last added
        allScenes.remove(allScenes.size() - 1); // and pop it
        // update active scene
        mainWindow.setScene(allScenes.get(allScenes.size() - 1));
        // return removed scene
        return sToPop;
    }
}
