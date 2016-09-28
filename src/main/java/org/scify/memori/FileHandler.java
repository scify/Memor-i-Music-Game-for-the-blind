
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

package org.scify.memori;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.util.*;

/**
 * Responsible for interacting with files
 * Files used by this program are: a) high score file and b) the json representation of the DB holding all cards (animals)
 */
public class FileHandler {

    /**
     * The file that represents the DB
     */
    private String propertiesFile = "high_scores.properties";

    public Map<String, ArrayList<String>> readCardsFromJSONFile() {
        //  each DB "row" will be represented as a Map of String (id) to a Map of Strings (the card attributes, like sound and image)
        Map<String, ArrayList<String>> map = new HashMap<>();
        Scanner scanner = null;
        try {
            scanner = new Scanner( new InputStreamReader(getClass().getClassLoader().getResourceAsStream("json_DB/cards.json")));
            String jsonStr = scanner.useDelimiter("\\A").next();

            JSONObject rootObject = new JSONObject(jsonStr); // Parse the JSON to a JSONObject
            JSONArray rows = rootObject.getJSONArray("cards"); // Get all JSONArray rows
            // shuffle the rows (we want the cards to be in a random order)
            rows = shuffleJsonArray(rows);
            ArrayList<String> tempMap;
            /**
             * The number of cards we need depends on the level (number of rows and columns)
             * divided by the number of the card tuple we want to form (2-card patterns, 3-card patterns, etc)
             */
            int numOfCards = (MainOptions.NUMBER_OF_COLUMNS * MainOptions.NUMBER_OF_ROWS) / MainOptions.NUMBER_OF_OPEN_CARDS;
            for(int i = 0; i < numOfCards; i++) { // Loop over each each row
                JSONObject cardObj = rows.getJSONObject(i); // Get row object
                tempMap = new ArrayList<>();
                JSONObject cardAttrs = cardObj.getJSONObject("attrs");
                tempMap.add(0, cardAttrs.getString("img"));
                tempMap.add(1, cardAttrs.getString("sounds"));
                map.put(cardObj.getString("cardType"), tempMap);
            }

        } finally {
            scanner.close();
        }
        return map;
    }

    public static JSONArray shuffleJsonArray (JSONArray array) throws JSONException {
        // Implementing Fisher–Yates shuffle
        Random rnd = new Random();
        for (int i = array.length() - 1; i >= 0; i--)
        {
            int j = rnd.nextInt(i + 1);
            // Simple swap
            Object object = array.get(j);
            array.put(j, array.get(i));
            array.put(i, object);
        }
        return array;
    }


    public String readHighScoreForCurrentLevel() {
        String highScore = "";
        Properties prop = new Properties();

        File scoresFile = new File(propertiesFile);
        try {
            scoresFile.createNewFile();
            FileInputStream in = new FileInputStream(scoresFile);
            prop.load(in);
            highScore = prop.getProperty(String.valueOf(MainOptions.gameLevel));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScore;
    }

    public String readHighScoreForLevel(String level) {
        String highScore = "";
        Properties prop = new Properties();

        File scoresFile = new File(propertiesFile);
        try {
            scoresFile.createNewFile();
            FileInputStream in = new FileInputStream(scoresFile);
            prop.load(in);
            highScore = prop.getProperty(String.valueOf(level));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScore;
    }

    public void setHighScoreForLevel (String highScore) {

        Properties props = new Properties();

        File scoresFile = new File(propertiesFile);
        try {
            scoresFile.createNewFile();
            FileOutputStream out = new FileOutputStream(scoresFile);
            props.setProperty(String.valueOf(MainOptions.gameLevel), highScore);
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
