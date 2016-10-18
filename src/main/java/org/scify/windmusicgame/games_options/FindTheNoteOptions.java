/**
 * Copyright 2016 SciFY.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scify.windmusicgame.games_options;

import org.scify.windmusicgame.MemoriGameLevel;
import org.scify.windmusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;
import java.util.ArrayList;

/**
 * This Class describes the option variables for "Find the note" game
 * Created by pisaris on 11/10/2016.
 */
public class FindTheNoteOptions extends GameWithLevelsOptions implements GameOptions{

    @Override
    public void initializeGameLevels() {

        gameLevels = new ArrayList<>();
        gameLevels.add(new MemoriGameLevel(1, new Point2D.Double(2,3), "game1/level1IntroSound.wav", "game_levels_screen_sounds/2x3.wav", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(2, new Point2D.Double(2,4), "game1/level2IntroSound.wav", "game_levels_screen_sounds/2x4.wav", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(3, new Point2D.Double(3,4), "game1/level3IntroSound.wav", "game_levels_screen_sounds/3x4.wav", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(4, new Point2D.Double(4,4), "game1/level4IntroSound.wav", "game_levels_screen_sounds/4x4.wav", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(5, new Point2D.Double(4,5), "game1/level5IntroSound.wav", "game_levels_screen_sounds/4x5.wav", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(6, new Point2D.Double(4,6), "game1/level6IntroSound.wav", "game_levels_screen_sounds/4x6.wav", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(7, new Point2D.Double(5,6), "game1/level7IntroSound.wav", "game_levels_screen_sounds/5x6.wav", "json_DB/find_the_note_easy.json"));

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

    public FindTheNoteOptions() {
        scoresFile = ".find_note_scores.properties";
        gameDescription = "ΒΡΕΙΤΕ ΤΗ ΝΟΤΑ";
        gameDescriptionSound = "game_levels_screen_sounds/find_note_description.wav";
        initializeGameLevels();
        initializeGameStoryLineSounds();
    }
}
