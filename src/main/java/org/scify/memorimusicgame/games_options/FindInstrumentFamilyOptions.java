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

package org.scify.memorimusicgame.games_options;

import org.scify.memorimusicgame.MemoriGameLevel;
import org.scify.memorimusicgame.helper.MemoriConfiguration;
import org.scify.memorimusicgame.interfaces.GameOptions;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Locale;
import java.util.ResourceBundle;

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
                "find_instrument_family/level_intro_sounds/level1IntroSound.mp3",
                "screens/game_levels_screen/2x3.mp3",
                "find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(2,
                new Point2D.Double(2,4),
                "find_instrument_family/level_intro_sounds/level2IntroSound.mp3",
                "screens/game_levels_screen/2x4.mp3",
                "find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(3,
                new Point2D.Double(3,4),
                "find_instrument_family/level_intro_sounds/level3IntroSound.mp3",
                "screens/game_levels_screen/3x4.mp3",
                "find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(4,
                new Point2D.Double(4,4),
                "find_instrument_family/level_intro_sounds/level4IntroSound.mp3",
                "screens/game_levels_screen/4x4.mp3",
                "find_the_instrument_families.json", "find_instrument_family/tutorial/introHelperSound4x4.mp3"));
        gameLevels.add(new MemoriGameLevel(5,
                new Point2D.Double(4,5),
                "find_instrument_family/level_intro_sounds/level5IntroSound.mp3",
                "screens/game_levels_screen/4x5.mp3",
                "find_the_instrument_families.json"));
        gameLevels.add(new MemoriGameLevel(6,
                new Point2D.Double(4,6),
                "find_instrument_family/level_intro_sounds/level6IntroSound.mp3",
                "screens/game_levels_screen/4x6.mp3",
                "find_the_instrument_families.json"));
    }

    @Override
    public void initializeGameSounds() {
        //NOT USED YET
        storyLineSounds.put(1, "game1/storyLine1.mp3");
        storyLineSounds.put(2, "game1/storyLine2.mp3");
        storyLineSounds.put(3, "game1/storyLine3.mp3");
        storyLineSounds.put(4, "game1/storyLine4.mp3");
        storyLineSounds.put(5, "game1/storyLine5.mp3");
        storyLineSounds.put(6, "game1/storyLine6.mp3");

        endLevelStartingSounds.add("find_instrument_family/end_level_starting_sounds/sound1.mp3");
        endLevelStartingSounds.add("find_instrument_family/end_level_starting_sounds/sound2.mp3");
        endLevelStartingSounds.add("find_instrument_family/end_level_starting_sounds/sound3.mp3");
        endLevelStartingSounds.add("find_instrument_family/end_level_starting_sounds/sound4.mp3");
        endLevelStartingSounds.add("find_instrument_family/end_level_starting_sounds/sound5.mp3");

        endLevelEndingSounds.add("find_instrument_family/end_level_ending_sounds/sound1.mp3");
        endLevelEndingSounds.add("find_instrument_family/end_level_ending_sounds/sound2.mp3");
        endLevelEndingSounds.add("find_instrument_family/end_level_ending_sounds/sound3.mp3");
        endLevelEndingSounds.add("find_instrument_family/end_level_ending_sounds/sound4.mp3");
        endLevelEndingSounds.add("find_instrument_family/end_level_ending_sounds/sound5.mp3");
        endLevelEndingSounds.add("find_instrument_family/end_level_ending_sounds/sound6.mp3");

        cardsOpeningSound = "miscellaneous/Chair1.wav";

        endGameSound = "find_instrument_family/storyline_audios/end_game_sound.mp3";

        funFactorSounds.add("find_instrument_family/fun_factor_sounds/1.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/2.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/3.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/4.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/5.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/6.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/7.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/8.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/9.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/10.mp3");
    }


    public FindInstrumentFamilyOptions() {
        super();
        scoresFile += "find_instrument_families_scores.properties";
        MemoriConfiguration configuration = new MemoriConfiguration();
        ResourceBundle labels = ResourceBundle.getBundle("languages.strings", new Locale(configuration.getProjectProperty("APP_LANG"), configuration.getProjectProperty("APP_LANG_LOCALE_CAPITAL")));
        gameDescription = labels.getString("find_instrument_family");
        gameDescriptionSound = "screens/game_levels_screen/find_instrument_families_description.mp3";
        tutorialSoundBase = "find_instrument_family/tutorial/";
        initializeGameLevels();
        initializeGameSounds();
    }
}
