
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


public class FXMemoriGame extends MemoriGame {
    protected SceneHandler sceneHandler;


    public FXMemoriGame(SceneHandler shSceneHandler) {

        this.sceneHandler = shSceneHandler;
    }

    @Override
    public void initialize() {
        super.initialize();

        FXRenderingEngine fUI = new FXRenderingEngine();
        uInterface = fUI;
        reRenderer = fUI;

        // Plus update current scene
        sceneHandler.pushScene(fUI.gameScene);

        fUI.gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    System.err.println("END GAME");
                    if(MainOptions.TUTORIAL_MODE)
                        MainOptions.TUTORIAL_MODE = false;
                    reRenderer.cancelCurrentRendering();
                    Thread.currentThread().interrupt();
                    sceneHandler.popScene();
                    break;
            }
        });
    }


}
