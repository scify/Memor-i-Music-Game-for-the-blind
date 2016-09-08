package org.scify.memori.interfaces.refactored;

public interface Rules {
    GameState getInitialState();
    GameState getNextState(GameState gsCurrent, UserAction uaAction);
    boolean isGameFinished(GameState gsCurrent);
}
