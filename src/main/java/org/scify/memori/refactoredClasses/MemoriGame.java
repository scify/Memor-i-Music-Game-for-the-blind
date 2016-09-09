package org.scify.memori.refactoredClasses;

import javafx.application.Platform;
import org.scify.memori.interfaces.refactored.*;

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

            // and keep on doing the loop in this thread
            //get next user action
            UserAction uaToHandle = uInterface.getNextUserAction(gsCurrentState.getCurrentPlayer());
            if (uaToHandle != null) {
                //apply it and determine the next state
                gsCurrentState = rRules.getNextState(gsCurrentState, uaToHandle);
            }
            // Ask to soon draw the state
            Platform.runLater(() -> {
                //draw game state
                reRenderer.drawGameState(toHandle);
            });

//            Thread.yield();
            try {
                Thread.sleep(50L); // Allow repainting
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // TODO: Also allow next state getting, when no user action was provided
        }
        GameState finalGsCurrentState = gsCurrentState;
        Platform.runLater(() -> {
            //draw game state
            reRenderer.drawGameState(finalGsCurrentState);
        });
        System.err.println("GAME OVER");
    }

    @Override
    public void finalize() {

    }

}
