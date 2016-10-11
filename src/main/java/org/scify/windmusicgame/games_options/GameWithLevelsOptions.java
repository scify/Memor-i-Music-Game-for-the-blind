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
}
