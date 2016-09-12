package org.scify.memori.interfaces;

public interface Rules {
    GameState getInitialState();
    GameState getNextState(GameState gsCurrent, UserAction uaAction);
    boolean isGameFinished(GameState gsCurrent);
}
