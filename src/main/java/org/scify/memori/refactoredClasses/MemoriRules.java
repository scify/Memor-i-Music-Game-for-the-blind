package org.scify.memori.refactoredClasses;

import org.scify.memori.interfaces.refactored.GameEvent;
import org.scify.memori.interfaces.refactored.GameState;
import org.scify.memori.interfaces.refactored.Rules;
import org.scify.memori.interfaces.refactored.UserAction;

public class MemoriRules implements Rules {

    @Override
    public GameState getInitialState() {
        return new MemoriGameState();
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction uaAction) {

        MemoriGameState gsCurrentState = (MemoriGameState)gsCurrent;
        gsCurrentState.resetEventsQueue();

        if (uaAction.getActionType().equals("flip")) {
            //((MemoriTerrain) (gsCurrentState.getTerrain())).toggleTile((int) uaAction.getCoords().getX(), (int) uaAction.getCoords().getY());
            gsCurrentState.getEventQueue().add(new GameEvent("flip", uaAction.getCoords()));
            gsCurrentState.nextTurn(); // TODO: Remove
        } else if(uaAction.getActionType().equals("movement")) {
            gsCurrentState.getEventQueue().add(new GameEvent("movement", uaAction.getCoords()));
        }
        else
            gsCurrentState.getEventQueue().add(new GameEvent("invalidAction", uaAction.getCoords()));
        return gsCurrentState;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return ((MemoriGameState)gsCurrent).areAllTilesWon();
    }
}
