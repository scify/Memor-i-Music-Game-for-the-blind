package org.scify.windmusicgame.games_options;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Class describing a Game that is level-based
 * Created by pisaris on 11/10/2016.
 */
public class GameWithLevelsOptions {

    /**
     * gameDescription is presented at the game levels screen
     */
    protected String gameDescription;

    /**
     * gameDescription sound is presented at the game levels screen
     */
    protected String gameDescriptionSound;
    /**
     * Each game level is associated with a set of dimensions
     */
    protected Map<Integer, Point2D> gameLevelToDimensions = new HashMap<>();

    /**
     * Every time we play a game we follow the story line
     */
    protected Map<Integer, String> storyLineSounds = new HashMap<>();

    /**
     * Each game level has an introductory sound associated with it
     */
    protected Map<Integer, String> introductorySounds = new HashMap<>();

    /**
     * Each game level has an description sound associated with it
     */
    protected Map<Integer, String> gameLevelSounds = new HashMap<>();

    public Map<Integer, String> getGameLevelSounds() {
        return gameLevelSounds;
    }

    /**
     * The DB representation that the game Cards are stored in (in our case, a JSON file).
     */
    protected String cardsDBRepresentation;
    public String scoresFile;

    public String getGameDescription() {
        return gameDescription;
    }

    public Map<Integer, Point2D> getGameLevelToDimensions() {
        return gameLevelToDimensions;
    }

    public Map<Integer, String> getStoryLineSounds() {
        return storyLineSounds;
    }

    public Map<Integer, String> getIntroductorySounds() {
        return introductorySounds;
    }

    public String getGameDescriptionSound() {
        return gameDescriptionSound;
    }
}
