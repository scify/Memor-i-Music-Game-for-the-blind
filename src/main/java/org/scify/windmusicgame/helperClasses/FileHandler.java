
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

package org.scify.windmusicgame.helperClasses;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.scify.windmusicgame.MainOptions;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Responsible for interacting with files
 * Files used by this program are: a) high score file and b) the json representation of the DB holding all cards (animals)
 */
public class FileHandler {

    /**
     * The file that the high scores get stored
     */
    private String propertiesFile = "high_scores.properties";


    public ArrayList<JSONObject> getCardsFromJSONFile() {

        ArrayList<JSONObject> cardsList = new ArrayList<>();
        // cardsListTemp will hold the read cards from the current equivalence card set.
        // If we find a duplicate card, we discard the equivalence card set and start again.
        // else, we copy the cardsListTemp to the cardsList.
        ArrayList<JSONObject> cardsListTemp;
        int randomNumber;
        Scanner scanner = null;
        try {
            scanner = new Scanner( new InputStreamReader(getClass().getClassLoader().getResourceAsStream("json_DB/game_1.json")));
            String jsonStr = scanner.useDelimiter("\\A").next();

            JSONObject rootObject = new JSONObject(jsonStr); // Parse the JSON to a JSONObject
            JSONArray cardSets = getEquivalenceCardSets(rootObject);
            /*
              The number of cards we need depends on the level (number of rows and columns)
              divided by the number of the card tuple we want to form (2-card patterns, 3-card patterns, etc)
             */
            int numOfCards = (MainOptions.NUMBER_OF_COLUMNS * MainOptions.NUMBER_OF_ROWS);
            int cardCount = 0;
            while (cardCount < numOfCards) {
                cardsListTemp = new ArrayList<>();
                // produce a random number for the card sets (we want to select a random card set)
                randomNumber = random_int(0, cardSets.length());
                // select a random equivalence card set
                JSONArray randomCardSet = cardSets.getJSONArray(randomNumber);
                // equivalenceCardSetHashCode describes the current card set
                String equivalenceCardSetHashCode = randomString();
                // shuffle the selected card set so that we pick random cards
                randomCardSet = shuffleJsonArray(randomCardSet);
                Iterator it = randomCardSet.iterator();
                // categories will hold every category that has been already read so we only add one card from each category
                ArrayList categories = new ArrayList();
                while(it.hasNext()) {
                    JSONObject currCard = (JSONObject) it.next();
                    // if the current category has not been read before and the current card has not been already added
                    if(!categories.contains(currCard.get("category"))) {
                        // if the current card is set to be unique
                        if(currCard.get("unique").equals(true)) {
                            // if not unique (ie already exists)
                            if(cardsList.contains(currCard)) {
                                // reset the temporary cards list
                                cardsListTemp = new ArrayList<>();
                                //we need to break the loop so that we change equivalence card set
                                break;
                            }
                        }
                        currCard.put("equivalenceCardSetHashCode", equivalenceCardSetHashCode);
                        // add card
                        cardsListTemp.add(currCard);
                        // mark category as read
                        categories.add(categories.size(), currCard.get("category"));
                        cardCount ++;
                    }

                }
                cardsList.addAll(cardsListTemp);
            }

        } finally {
            scanner.close();
        }
        return cardsList;
    }

    public JSONArray getEquivalenceCardSets(JSONObject rootObject) {
        JSONArray cardSets = rootObject.getJSONArray("equivalence_card_sets"); // Get all JSONArray rows
        // shuffle the rows (we want the cards to be in a random order)
        cardSets = shuffleJsonArray(cardSets);
        return cardSets;
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

    private int random_int(int Min, int Max) {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    private String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
