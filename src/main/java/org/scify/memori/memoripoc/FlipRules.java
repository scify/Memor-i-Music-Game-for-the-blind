package org.scify.memori.memoripoc;

import org.scify.memori.interfaces.GameState;
import org.scify.memori.interfaces.Ruleset;
import org.scify.memori.interfaces.UserAction;

/**
 * Created by pisaris on 5/9/2016.
 */
public class FlipRules implements Ruleset {

    @Override
    public GameState getInitialState() {
        return new FlipGameState();
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction uaAction) {
        FlipGameState fgsCurrentState = (FlipGameState)gsCurrent;
        if (uaAction.getActionType().equals("flip")) {
            ((FlipTerrain) (fgsCurrentState.getTerrain())).toggleTile((int) uaAction.getCoords().getX(), (int) uaAction.getCoords().getY());
            fgsCurrentState.nextTurn();
        }
        else
            ((FlipGameState) gsCurrent).getEvents().add("Invalid action");
        return fgsCurrentState;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return ((FlipGameState)gsCurrent).iCurrentTurn > 2;
    }
}
