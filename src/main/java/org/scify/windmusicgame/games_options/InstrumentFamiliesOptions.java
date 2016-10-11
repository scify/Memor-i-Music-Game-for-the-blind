package org.scify.windmusicgame.games_options;

import org.scify.windmusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;

/**
 * Game Options class for the First game (associate all instruments with their families)
 * Created by pisaris on 11/10/2016.
 */
public class InstrumentFamiliesOptions extends GameWithLevelsOptions implements GameOptions {

    @Override
    public void initializeGameLevels() {
        gameLevelToDimensions.put(1, new Point2D.Double(2,3));
        gameLevelToDimensions.put(2, new Point2D.Double(2,4));
        gameLevelToDimensions.put(3, new Point2D.Double(3,4));
        gameLevelToDimensions.put(4, new Point2D.Double(4,4));
        gameLevelToDimensions.put(5, new Point2D.Double(4,5));
        gameLevelToDimensions.put(6, new Point2D.Double(4,6));
    }

    @Override
    public void initializeGameIntroductorySounds() {
        introductorySounds.put(1, "game1/level1IntroSound.wav");
        introductorySounds.put(2, "game1/level2IntroSound.wav");
        introductorySounds.put(3, "game1/level3IntroSound.wav");
        introductorySounds.put(4, "game1/level4IntroSound.wav");
        introductorySounds.put(5, "game1/level5IntroSound.wav");
        introductorySounds.put(6, "game1/level6IntroSound.wav");
    }

    @Override
    public void initializeGameStoryLineSounds() {
        storyLineSounds.put(1, "game1/storyLine1.wav");
        storyLineSounds.put(2, "game1/storyLine2.wav");
        storyLineSounds.put(3, "game1/storyLine3.wav");
        storyLineSounds.put(4, "game1/storyLine4.wav");
        storyLineSounds.put(5, "game1/storyLine5.wav");
        storyLineSounds.put(6, "game1/storyLine6.wav");
    }

    public InstrumentFamiliesOptions() {
        initializeGameLevels();
        initializeGameIntroductorySounds();
        initializeGameStoryLineSounds();
    }
}
