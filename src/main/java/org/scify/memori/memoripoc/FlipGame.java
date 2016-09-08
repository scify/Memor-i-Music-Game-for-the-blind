package org.scify.memori.memoripoc;

import org.scify.memori.interfaces.*;

/**
 * Created by pisaris on 5/9/2016.
 */
public class FlipGame implements Game {
    Ruleset rRules;
    UserInterface uInterface;
    RenderingEngine reRenderer;

    public FlipGame() {
    }

    @Override
    public void initialize() {
        rRules = new FlipRules();
        FlipConsoleUI fUI = new FlipConsoleUI();
        uInterface = fUI;
        reRenderer = fUI;
    }

    @Override
    public void run() {
        GameState gsCurrentState=rRules. getInitialState();
        while(!rRules.isGameFinished(gsCurrentState)){
            //draw game state
            reRenderer.drawGameState(gsCurrentState);
            //get next user action
            UserAction uaToHandle = uInterface.getNextUserAction(gsCurrentState.getCurrentPlayer());
            //IGNORING FOR NOW: is user action is valid
            //apply it and determine the next state
            gsCurrentState = rRules.getNextState(gsCurrentState, uaToHandle);
        }

    }

    @Override
    public void finalize() {

    }

    public static void main(String[] saArgs) {
        FlipGame fg = new FlipGame();
        fg.initialize();
        fg.run();
        fg.finalize();

        System.out.printf("Game is now OVER, sucker!");
    }
}
