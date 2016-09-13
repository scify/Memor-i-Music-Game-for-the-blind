package org.scify.memori;

import javafx.application.Platform;
import org.scify.memori.interfaces.*;

public abstract class MemoriGame implements Game, Runnable {
    Rules rRules;
    UI uInterface;
    RenderingEngine reRenderer;

    TimeWatch watch;
    private HighScoreHandler highScore;

    public MemoriGame() {
        watch = TimeWatch.start();
        highScore = new HighScoreHandler();
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
        GameState finalGsCurrentState = gsCurrentState;
        Platform.runLater(() -> {
            //draw game state
            reRenderer.drawGameState(finalGsCurrentState);
            finalize();
        });

        System.err.println("GAME OVER");

    }

    @Override
    public void finalize() {
        System.err.println("finalize");
        Platform.runLater(() -> {
            //draw game state
            reRenderer.playGameOver();
        });

        highScore.updateHighScore(watch);
    }

}
