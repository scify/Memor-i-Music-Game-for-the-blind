package org.scify.windmusicgame.interfaces;

/**
 * Created by pisaris on 21/12/2016.
 */
public interface HighScoresHandler {
    /**
     * Reads the current high score for a given game level and the current game.
     * @param level the given game level (1,2,... etc)
     * @return the high score (can be null if no high score)
     */
    String readHighScoreForLevel(String level);

    /**
     * Sets a given high score for the current game level and the current game.
     * @param highScore the high score to be set.
     */
    void setHighScoreForLevel(String highScore);
}
