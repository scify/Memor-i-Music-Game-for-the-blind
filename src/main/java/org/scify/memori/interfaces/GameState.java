package org.scify.memori.interfaces;

import java.util.Queue;

public interface GameState {
    /**
     * Returns the current player (i.e. the one who can act in the game).
     *
     * @return The current {@link Player}
      */
    Player getCurrentPlayer();

    /**
     * Returns the current terrain.
     * @return The {@link Terrain}.
     */
    Terrain getTerrain();

    /**
     * Returns a list of game events that express reactions to user actions.
     * @return A list of GameEvent objects, expected to be handled by a {@link RenderingEngine}.
     */
    Queue<GameEvent> getEventQueue();

    void resetEventsQueue();
}
