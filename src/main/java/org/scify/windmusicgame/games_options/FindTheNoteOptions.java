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
        gameLevels.add(new MemoriGameLevel(1, new Point2D.Double(2,3), "find_note/level_intro_sounds/level1IntroSound.mp3", "game_levels_screen_sounds/2x3.mp3", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(2, new Point2D.Double(2,4), "find_note/level_intro_sounds/level2IntroSound.mp3", "game_levels_screen_sounds/2x4.mp3", "json_DB/find_the_note_easy.json"));
        gameLevels.add(new MemoriGameLevel(3, new Point2D.Double(3,4), "find_note/level_intro_sounds/level3IntroSound.mp3", "game_levels_screen_sounds/3x4.mp3", "json_DB/find_the_note_medium.json"));
        gameLevels.add(new MemoriGameLevel(4, new Point2D.Double(4,4), "find_note/level_intro_sounds/level4IntroSound.mp3", "game_levels_screen_sounds/4x4.mp3", "json_DB/find_the_note_medium.json"));
        gameLevels.add(new MemoriGameLevel(5, new Point2D.Double(4,5), "find_note/level_intro_sounds/level5IntroSound.mp3", "game_levels_screen_sounds/4x5.mp3", "json_DB/find_the_note_hard.json", "find_note/introHelperSound4x5.mp3"));
        gameLevels.add(new MemoriGameLevel(6, new Point2D.Double(4,6), "find_note/level_intro_sounds/level6IntroSound.mp3", "game_levels_screen_sounds/4x6.mp3", "json_DB/find_the_note_extreme.json"));
        gameLevels.add(new MemoriGameLevel(7, new Point2D.Double(5,6), "find_note/level_intro_sounds/level7IntroSound.mp3", "game_levels_screen_sounds/5x6.mp3", "json_DB/find_the_note_extreme.json"));

    }

    @Override
    public void initializeGameSounds() {
        storyLineSounds.put(1, "game2/storyLine1.mp3");
        storyLineSounds.put(2, "game2/storyLine2.mp3");
        storyLineSounds.put(3, "game2/storyLine3.mp3");
        storyLineSounds.put(4, "game2/storyLine4.mp3");
        storyLineSounds.put(5, "game2/storyLine5.mp3");
        storyLineSounds.put(6, "game2/storyLine6.mp3");
        storyLineSounds.put(7, "game2/storyLine7.mp3");

        endLevelStartingSounds.add("find_note/end_level_starting_sounds/sound1.mp3");
        endLevelStartingSounds.add("find_note/end_level_starting_sounds/sound2.mp3");
        endLevelStartingSounds.add("find_note/end_level_starting_sounds/sound3.mp3");
        endLevelStartingSounds.add("find_note/end_level_starting_sounds/sound4.mp3");
        endLevelStartingSounds.add("find_note/end_level_starting_sounds/sound5.mp3");

        endLevelEndingSounds.add("find_note/end_level_ending_sounds/sound1.mp3");
        endLevelEndingSounds.add("find_note/end_level_ending_sounds/sound2.mp3");
        endLevelEndingSounds.add("find_note/end_level_ending_sounds/sound3.mp3");
        endLevelEndingSounds.add("find_note/end_level_ending_sounds/sound4.mp3");
        endLevelEndingSounds.add("find_note/end_level_ending_sounds/sound5.mp3");
        endLevelEndingSounds.add("find_note/end_level_ending_sounds/sound6.mp3");

        cardsOpeningSound = "game_effects/open_door.wav";

        endGameSound = "find_note/storyline_audios/end_game_sound.mp3";

        funFactorSounds.add("find_instrument_family/fun_factor_sounds/2.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/4.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/5.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/7.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/10.mp3");
        funFactorSounds.add("find_instrument_family/fun_factor_sounds/11.mp3");
    }

    public FindTheNoteOptions() {
        super();
        scoresFile += "find_note_scores.properties";
        gameDescription = "ΒΡΕΙΤΕ ΤΗ ΝΟΤΑ";
        gameDescriptionSound = "game_levels_screen_sounds/find_note_description.mp3";
        tutorialSoundBase = "find_note/tutorial/";
        initializeGameLevels();
        initializeGameSounds();
    }
}
