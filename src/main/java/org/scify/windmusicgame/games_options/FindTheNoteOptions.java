package org.scify.windmusicgame.games_options;

import org.scify.windmusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;

/**
 * Created by pisaris on 11/10/2016.
 */
public class FindTheNoteOptions extends GameWithLevelsOptions implements GameOptions{

    @Override
    public void initializeGameLevels() {
        gameLevelToDimensions.put(1, new Point2D.Double(2,3));
        gameLevelToDimensions.put(2, new Point2D.Double(2,4));
        gameLevelToDimensions.put(3, new Point2D.Double(3,4));
        gameLevelToDimensions.put(4, new Point2D.Double(4,4));
        gameLevelToDimensions.put(5, new Point2D.Double(4,5));
        gameLevelToDimensions.put(6, new Point2D.Double(4,6));
        gameLevelToDimensions.put(7, new Point2D.Double(5,6));
    }

    @Override
    public void initializeGameIntroductorySounds() {
        introductorySounds.put(1, "game2/level1IntroSound.wav");
        introductorySounds.put(2, "game2/level2IntroSound.wav");
        introductorySounds.put(3, "game2/level3IntroSound.wav");
        introductorySounds.put(4, "game2/level4IntroSound.wav");
        introductorySounds.put(5, "game2/level5IntroSound.wav");
        introductorySounds.put(6, "game2/level6IntroSound.wav");
        introductorySounds.put(7, "game2/level7IntroSound.wav");

        gameLevelSounds.put(1, "game_levels_screen_sounds/2x3.wav");
        gameLevelSounds.put(2, "game_levels_screen_sounds/2x4.wav");
        gameLevelSounds.put(3, "game_levels_screen_sounds/3x4.wav");
        gameLevelSounds.put(4, "game_levels_screen_sounds/4x4.wav");
        gameLevelSounds.put(5, "game_levels_screen_sounds/4x5.wav");
        gameLevelSounds.put(6, "game_levels_screen_sounds/4x6.wav");
        gameLevelSounds.put(7, "game_levels_screen_sounds/5x6.wav");
    }

    @Override
    public void initializeGameStoryLineSounds() {
        storyLineSounds.put(1, "game2/storyLine1.wav");
        storyLineSounds.put(2, "game2/storyLine2.wav");
        storyLineSounds.put(3, "game2/storyLine3.wav");
        storyLineSounds.put(4, "game2/storyLine4.wav");
        storyLineSounds.put(5, "game2/storyLine5.wav");
        storyLineSounds.put(6, "game2/storyLine6.wav");
        storyLineSounds.put(7, "game2/storyLine7.wav");
    }

    @Override
    public String getDBRespresenation() {
        return cardsDBRepresentation;
    }

    public FindTheNoteOptions() {
        scoresFile = "find_note_scores.properties";
        gameDescription = "ΒΡΕΙΤΕ ΤΗ ΝΟΤΑ";
        cardsDBRepresentation = "json_DB/find_the_note.json";
        gameDescriptionSound = "game_levels_screen_sounds/find_note_description.wav";
        initializeGameLevels();
        initializeGameIntroductorySounds();
        initializeGameStoryLineSounds();
    }
}
