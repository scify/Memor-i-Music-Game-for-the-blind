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
import java.util.List;

/**
 * Game Options class for the First game (associate all instruments with their families)
 * This Class describes the option variables for "Find the instrument family" game
 * Created by pisaris on 11/10/2016.
 */
public class FindInstrumentFamilyOptions extends GameWithLevelsOptions implements GameOptions {

    @Override
    public void initializeGameLevels() {

        gameLevels = new ArrayList<>();
        gameLevels.add(new MemoriGameLevel(1,
                new Point2D.Double(2,3),
                "game1/level1IntroSound.mp3",
                "game_levels_screen_sounds/2x3.mp3",
                "json_DB/find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(2,
                new Point2D.Double(2,4),
                "game1/level2IntroSound.mp3",
                "game_levels_screen_sounds/2x4.mp3",
                "json_DB/find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(3,
                new Point2D.Double(3,4),
                "game1/level3IntroSound.mp3",
                "game_levels_screen_sounds/3x4.mp3",
                "json_DB/find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(4,
                new Point2D.Double(4,4),
                "game1/level4IntroSound.mp3",
                "game_levels_screen_sounds/4x4.mp3",
                "json_DB/find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(5,
                new Point2D.Double(4,5),
                "game1/level5IntroSound.mp3",
                "game_levels_screen_sounds/4x5.mp3",
                "json_DB/find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(6,
                new Point2D.Double(4,6),
                "game1/level6IntroSound.mp3",
                "game_levels_screen_sounds/4x6.mp3",
                "json_DB/find_the_instrument_families.json"));
    }

    @Override
    public void initializeGameSounds() {
        storyLineSounds.put(1, "game1/storyLine1.mp3");
        storyLineSounds.put(2, "game1/storyLine2.mp3");
        storyLineSounds.put(3, "game1/storyLine3.mp3");
        storyLineSounds.put(4, "game1/storyLine4.mp3");
        storyLineSounds.put(5, "game1/storyLine5.mp3");
        storyLineSounds.put(6, "game1/storyLine6.mp3");

        endLevelStartingSounds.add("find_instrument/end_level_starting_sounds/sound1.mp3");
        endLevelStartingSounds.add("find_instrument/end_level_starting_sounds/sound2.mp3");
        endLevelStartingSounds.add("find_instrument/end_level_starting_sounds/sound3.mp3");
        endLevelStartingSounds.add("find_instrument/end_level_starting_sounds/sound4.mp3");
        endLevelStartingSounds.add("find_instrument/end_level_starting_sounds/sound5.mp3");

        endLevelEndingSounds.add("find_instrument/end_level_ending_sounds/sound1.mp3");
        endLevelEndingSounds.add("find_instrument/end_level_ending_sounds/sound2.mp3");
        endLevelEndingSounds.add("find_instrument/end_level_ending_sounds/sound3.mp3");
        endLevelEndingSounds.add("find_instrument/end_level_ending_sounds/sound4.mp3");
        endLevelEndingSounds.add("find_instrument/end_level_ending_sounds/sound5.mp3");
        endLevelEndingSounds.add("find_instrument/end_level_ending_sounds/sound6.mp3");
    }


    public FindInstrumentFamilyOptions() {
        scoresFile = ".find_instrument_families_scores.properties";
        gameDescription = "ΒΡΕΙΤΕ ΤΟ ΕΙΔΟΣ ΤΟΥ ΟΡΓΑΝΟΥ";
        gameDescriptionSound = "game_levels_screen_sounds/find_instrument_families_description.mp3";
        initializeGameLevels();
        initializeGameSounds();
    }
}
