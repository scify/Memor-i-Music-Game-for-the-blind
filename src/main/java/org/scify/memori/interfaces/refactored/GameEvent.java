package org.scify.memori.interfaces.refactored;

/**
 * Represents a game event (vs. user event), i.e. an event that was generated internally by the game and needs
 * to be handled (e.g. by the UI, or by another rule).
 *
 * Created by pisaris on 6/9/2016.
 */
public class GameEvent {
    /**
     * A string representation of the event type.
     */
    public String type;

    /**
     * A generic object, encapsulating all information related to the user event (meant to be type-cast by
     * implementations).
     */
    public Object parameters;

    public GameEvent(String type, Object parameters) {
        this.type = type;
        this.parameters = parameters;
    }
}
