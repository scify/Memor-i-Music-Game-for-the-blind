
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

import org.scify.memori.interfaces.*;


public abstract class MemoriGame implements Game<Integer> {
    /**
     * constant defining whether the game is finished
     */
    private static final Integer GAME_FINISHED = 1;
    /**
     * constant defining whether the game should continue to next level
     */
    private static final Integer NEXT_LEVEL = 2;
    /**
     * constant defining whether the game should continue to next level
     */
    private static final Integer SAME_LEVEL = 3;
    Rules rRules;
    /**
     * Object responsible for UI events (User actions)
     */
    UI uInterface;
    /**
     * Object rensponsible for UI rendering events (sounds, graphics etc)
     */
    RenderingEngine reRenderer;


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
    public Integer call() {
        final GameState gsInitialState = rRules.getInitialState();
        reRenderer.drawGameState(gsInitialState); // Initialize UI layout
        // Run asyncronously
        GameState gsCurrentState = gsInitialState; // Init
        // For every cycle
        while (!rRules.isGameFinished(gsCurrentState)) {
            final GameState toHandle = gsCurrentState;
            // Ask to soon draw the state
            //Platform.runLater(() -> reRenderer.drawGameState(toHandle));
            reRenderer.drawGameState(toHandle);
            // and keep on doing the loop in this thread
            //get next user action
            UserAction uaToHandle = uInterface.getNextUserAction(gsCurrentState.getCurrentPlayer());

                //apply it and determine the next state
                gsCurrentState = rRules.getNextState(gsCurrentState, uaToHandle);


//            Thread.yield();
            try {
                Thread.sleep(50L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: Also allow next state getting, when no user action was provided
        }
        System.err.println("GAME OVER");
        MemoriGameState memoriGameState = (MemoriGameState) gsCurrentState;
        MainOptions.storyLineLevel++;

        //TODO: should we store story line level in a file?
        if(memoriGameState.loadNextLevel)
            return NEXT_LEVEL;
        else if(memoriGameState.replayLevel)
            return SAME_LEVEL;
        else
            return GAME_FINISHED;
    }

    @Override
    public void finalize() {
        System.err.println("FINALIZE");
    }

}
