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
 * This Class describes the option variables for "Find the instrument" game
 * Created by pisaris on 11/10/2016.
 */
public class FindTheInstrumentOptions extends GameWithLevelsOptions implements GameOptions{


    @Override
    public void initializeGameLevels() {
        gameLevels = new ArrayList<>();
        gameLevels.add(
                new MemoriGameLevel(1, new Point2D.Double(2,3), "find_instrument/level_intro_sounds/level1IntroSound.mp3", "screens/game_levels_screen/2x3.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(2, new Point2D.Double(2,4), "find_instrument/level_intro_sounds/level2IntroSound.mp3", "screens/game_levels_screen/2x4.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(3, new Point2D.Double(3,4), "find_instrument/level_intro_sounds/level3IntroSound.mp3", "screens/game_levels_screen/3x4.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(4, new Point2D.Double(4,4), "find_instrument/level_intro_sounds/level4IntroSound.mp3", "screens/game_levels_screen/4x4.mp3", "find_the_instrument.json", "find_instrument/tutorial/introHelperSound4x4.mp3"));
        gameLevels.add(
                new MemoriGameLevel(5, new Point2D.Double(4,5), "find_instrument/level_intro_sounds/level5IntroSound.mp3", "screens/game_levels_screen/4x5.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(6, new Point2D.Double(4,6), "find_instrument/level_intro_sounds/level6IntroSound.mp3", "screens/game_levels_screen/4x6.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(7, new Point2D.Double(5,6), "find_instrument/level_intro_sounds/level7IntroSound.mp3", "screens/game_levels_screen/5x6.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(8, new Point2D.Double(5,8), "find_instrument/level_intro_sounds/level8IntroSound.mp3", "screens/game_levels_screen/5x8.mp3", "find_the_instrument.json"));
        gameLevels.add(
                new MemoriGameLevel(9, new Point2D.Double(6,8), "find_instrument/level_intro_sounds/level9IntroSound.mp3", "screens/game_levels_screen/6x8.mp3", "find_the_instrument.json"));
    }

    @Override
    public void initializeGameSounds() {
        //NOT USED YET
        storyLineSounds.put(1, "storyline_audios");
        storyLineSounds.put(2, "storyline_audios");
        storyLineSounds.put(3, "storyline_audios");
        storyLineSounds.put(4, "storyline_audios");
        storyLineSounds.put(5, "storyline_audios");
        storyLineSounds.put(6, "storyline_audios");
        storyLineSounds.put(7, "storyline_audios");
        storyLineSounds.put(8, "storyline_audios");
        storyLineSounds.put(9, "storyline_audios");

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

        cardsOpeningSound = "miscellaneous/open_door.wav";

        endGameSound = "find_instrument/storyline_audios/end_game_sound.mp3";

        funFactorSounds.add("find_instrument/fun_factor_sounds/1.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/2.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/3.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/4.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/5.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/7.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/8.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/9.mp3");
        funFactorSounds.add("find_instrument/fun_factor_sounds/10.mp3");
    }

    public FindTheInstrumentOptions() {
        super();

        scoresFile += "find_instrument_scores.properties";

        MemoriConfiguration configuration = new MemoriConfiguration();
        ResourceBundle labels = ResourceBundle.getBundle("languages.strings", new Locale(configuration.getProjectProperty("APP_LANG"), configuration.getProjectProperty("APP_LANG_LOCALE_CAPITAL")));
        gameDescription = labels.getString("find_instrument");
        gameDescriptionSound = "screens/game_levels_screen/find_instrument_description.mp3";
        tutorialSoundBase = "find_instrument/tutorial/";
        initializeGameLevels();
        initializeGameSounds();
    }
}
