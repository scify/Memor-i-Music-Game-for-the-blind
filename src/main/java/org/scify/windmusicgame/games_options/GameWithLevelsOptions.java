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

import java.awt.geom.Point2D;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class describing a Game that is level-based
 * Created by pisaris on 11/10/2016.
 */
public class GameWithLevelsOptions {

    /**
     * Get the list of levels for each game
     * @return the list of levels for each game
     */
    public List<MemoriGameLevel> getGameLevels() {
        return gameLevels;
    }

    /**
     * Set of game levels
     */
    protected List<MemoriGameLevel> gameLevels;

    /**
     * gameDescription is presented at the game levels screen
     */
    protected String gameDescription;

    /**
     * gameDescription sound is presented at the game levels screen
     */
    protected String gameDescriptionSound;
    /**
     * Every time we play a game we follow the story line
     */
    protected Map<Integer, String> storyLineSounds = new HashMap<>();
    /**
     * Every time we play a game we follow the story line
     */
    protected List<String> endLevelStartingSounds = new ArrayList<>();

    public String getCardsOpeningSound() {
        return cardsOpeningSound;
    }

    protected String cardsOpeningSound;

    public String getTutorialSoundBase() {
        return tutorialSoundBase;
    }

    /**
     * Each game tutorial has it's sound clips in a separate directory

     */
    protected String tutorialSoundBase;

    public List<String> getEndLevelStartingSounds() {
        return endLevelStartingSounds;
    }

    public List<String> getEndLevelEndingSounds() {
        return endLevelEndingSounds;
    }

    /**
     * Every time we play a game we follow the story line
     */
    protected List<String> endLevelEndingSounds = new ArrayList<>();

    public String scoresFile;

    public GameWithLevelsOptions() {
        String userDir;
        if ((System.getProperty("os.name")).toUpperCase().contains("WINDOWS")) {
            userDir = System.getenv("AppData");
        } else {
            userDir = System.getProperty("user.dir");
        }
        this.scoresFile = userDir + File.separator;
        System.err.println(userDir);
    }

    public String getGameDescription() {
        return gameDescription;
    }


    public Map<Integer, String> getStoryLineSounds() {
        return storyLineSounds;
    }

    public String getGameDescriptionSound() {
        return gameDescriptionSound;
    }
}
