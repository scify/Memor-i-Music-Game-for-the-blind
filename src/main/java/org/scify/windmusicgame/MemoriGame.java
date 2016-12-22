
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

import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.interfaces.*;
import org.scify.windmusicgame.rules.MemoriRules;
import org.scify.windmusicgame.rules.TutorialRules;


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
    public UI uInterface;
    /**
     * Object rensponsible for UI rendering events (sounds, graphics etc)
     */
    public RenderingEngine reRenderer;

    GameOptions gameOptions;

    @Override
    /**
     * Subclasses should initialize a UI
     */
    public void initialize(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        if(MainOptions.TUTORIAL_MODE)
            rRules = new TutorialRules();
        else
            rRules = new MemoriRules();
        // Example initialization
//        RenderingEngine fUI = new FXRenderingEngine();
//        uInterface = fUI;
//        reRenderer = fUI;
    }

    @Override
    public Integer call() {
        final GameState gsInitialState = rRules.getInitialState(gameOptions);
        reRenderer.drawGameState(gsInitialState); // Initialize UI layout
        // Run asyncronously
        GameState gsCurrentState = gsInitialState; // Init
        // For every cycle
        while (!rRules.isGameFinished(gsCurrentState)) {
            final GameState toHandle = gsCurrentState;
            // Ask to draw the state
            reRenderer.drawGameState(toHandle);
            // and keep on doing the loop in this thread
            //get next user action
            UserAction uaToHandle = uInterface.getNextUserAction(gsCurrentState.getCurrentPlayer());

            //apply it and determine the next state
            gsCurrentState = rRules.getNextState(gsCurrentState, uaToHandle);

            try {
                Thread.sleep(50L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
        // the final game state will contain the user input relevant to the future of the game
        // the user can select to
        // a) end the game and return to the main screen
        // b) play the same level again
        // c) go to the next level
        MemoriGameState memoriGameState = (MemoriGameState) gsCurrentState;
        GameWithLevelsOptions gameWithLevelsOptions = (GameWithLevelsOptions) this.gameOptions;
        reRenderer.cancelCurrentRendering();
        if(memoriGameState.loadNextLevel) {
            if(MainOptions.gameLevel < gameWithLevelsOptions.getGameLevels().size()) {
                MainOptions.storyLineLevel++;
                return NEXT_LEVEL;
            }
            else
                return GAME_FINISHED;
        }
        else if(memoriGameState.replayLevel) {
            if(!MainOptions.TUTORIAL_MODE)
                MainOptions.storyLineLevel++;
            return SAME_LEVEL;
        }
        else
            return GAME_FINISHED;

    }

    @Override
    public void finalize() {
        System.err.println("FINALIZE");
    }

}
