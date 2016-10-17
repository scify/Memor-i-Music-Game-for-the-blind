
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

package org.scify.windmusicgame;

/**
 * Class that implements all public game options. Has only static variables and methods.
 * Purpose of the class is to make all options relevant constants (current level, terrain size)
 * available through all classes.
 */
public class MainOptions {

    public static boolean TUTORIAL_MODE = false;
    public static int NUMBER_OF_COLUMNS = 4;
    public static int NUMBER_OF_ROWS = 4;
    public static int NUMBER_OF_OPEN_CARDS = 2;
    public static int gameLevel;
    public static double mHeight;
    public static double mWidth;
    public static int storyLineLevel = 1;
    public static String gameScoresFile;
}
