package org.scify.memorimusicgame.interfaces;

import java.awt.geom.Point2D;

/**
 * GameLevel describes the methods for a level
 * Created by pisaris on 18/10/2016.
 */
public interface GameLevel {
    Point2D getDimensions();
    String getIntroSound();
    String getIntroScreenSound();

}
