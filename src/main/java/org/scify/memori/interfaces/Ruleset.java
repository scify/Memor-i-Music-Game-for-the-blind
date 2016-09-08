package org.scify.memori.interfaces;

/**
 * Created by pisaris on 5/9/2016.
 */
public interface Ruleset {
    GameState getInitialState();
    GameState getNextState(GameState gsCurrent, UserAction uaAction);
    boolean isGameFinished(GameState gsCurrent);
}
