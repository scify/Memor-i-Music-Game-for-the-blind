
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

public class FileHandler {


    public Map<String, ArrayList<String>> readCardsFromJSONFile() {
        Map<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
        Scanner scanner = null;
        try {
            scanner = new Scanner( new InputStreamReader(getClass().getClassLoader().getResourceAsStream("json_DB/cards.json")));
            String jsonStr = scanner.useDelimiter("\\A").next();

            JSONObject rootObject = new JSONObject(jsonStr); // Parse the JSON to a JSONObject
            JSONArray rows = rootObject.getJSONArray("cards"); // Get all JSONArray rows
            rows = shuffleJsonArray(rows);
            rows = shuffleJsonArray(rows);
            ArrayList<String> tempMap;
            for(int i = 0; i < (MainOptions.NUMBER_OF_COLUMNS * MainOptions.NUMBER_OF_ROWS) / 2; i++) { // Loop over each each row
                JSONObject cardObj = rows.getJSONObject(i); // Get row object
                tempMap = new ArrayList<>();
                JSONObject cardAttrs = cardObj.getJSONObject("attrs");
                tempMap.add(0, cardAttrs.getString("img"));
                tempMap.add(1, cardAttrs.getString("sounds"));
                map.put(cardObj.getString("cardType"), tempMap);
            }

        } finally {
            //close the scanner
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


    public static String readHighScoreForCurrentLevel() {
        String highScore = "";
        Properties prop = new Properties();
        InputStream input = null;

        try {

            input = new FileInputStream("config.properties");
            prop.load(input);
            highScore = prop.getProperty(String.valueOf(MainOptions.gameLevel));

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return highScore;
    }

    public static String readHighScoreForLevel(String level) {
        String highScore = "";
        Properties prop = new Properties();
        InputStream input = null;
        try {

            input = new FileInputStream("config.properties");
            prop.load(input);
            highScore = prop.getProperty(level);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        return highScore;
    }

    public static void setHighScoreForLevel (String highScore) {
        OutputStream output = null;

        try {
            FileInputStream in = new FileInputStream("config.properties");
            Properties props = new Properties();
            props.load(in);
            in.close();

            output = new FileOutputStream("config.properties");

            // set the properties value
            props.setProperty(String.valueOf(MainOptions.gameLevel), highScore);

            // save properties to project root folder
            props.store(output, null);

        } catch (IOException io) {
            io.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
