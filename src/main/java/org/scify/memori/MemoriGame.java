
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

import javafx.application.Platform;
import org.scify.memori.interfaces.*;

public abstract class MemoriGame implements Game, Runnable {
    Rules rRules;
    UI uInterface;
    RenderingEngine reRenderer;

    public MemoriGame() {
    }

    @Override
    /**
     * Subclasses should initialize a UI
     */
    public void initialize() {
        rRules = new MemoriRules();
        // Example initialization
//        RenderingEngine fUI = new FXRenderingEngine();
//        uInterface = fUI;
//        reRenderer = fUI;
    }

    @Override
    public void run() {

        final GameState gsInitialState=rRules.getInitialState();
        reRenderer.drawGameState(gsInitialState); // Initialize UI layout
        // Run asyncronously
        GameState gsCurrentState = gsInitialState; // Init
        // For every cycle
        while(!rRules.isGameFinished(gsCurrentState)) {
            final GameState toHandle = gsCurrentState;
            // Ask to soon draw the state
            Platform.runLater(() -> {
                //draw game state
                reRenderer.drawGameState(toHandle);
            });
            // and keep on doing the loop in this thread
            //get next user action
            UserAction uaToHandle = uInterface.getNextUserAction(gsCurrentState.getCurrentPlayer());
            if (uaToHandle != null) {
                //apply it and determine the next state
                gsCurrentState = rRules.getNextState(gsCurrentState, uaToHandle);
            }

//            Thread.yield();
            try {
                Thread.sleep(50L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: Also allow next state getting, when no user action was provided
        }
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        MemoriGameState memoriGameState = (MemoriGameState)gsCurrentState;
        if(memoriGameState.gameFinished) {
            System.err.println("END GAME");
            SceneHandler.popScene();
        }

        System.err.println("GAME OVER");

    }

    @Override
    public void finalize() {
    }

}
