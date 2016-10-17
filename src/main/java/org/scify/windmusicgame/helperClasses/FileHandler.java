
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
import org.scify.windmusicgame.interfaces.GameOptions;

import java.io.*;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.*;

/**
 * Responsible for interacting with files
 * Files used by this program are: a) high score file and b) the json representation of the DB holding all cards (animals)
 */
public class FileHandler {


    public ArrayList<JSONObject> getCardsFromJSONFile(GameOptions gameOptions) {

        ArrayList<JSONObject> cardsList = new ArrayList<>();
        // cardsListTemp will hold the read cards from the current equivalence card set.
        // If we find a duplicate card, we discard the equivalence card set and start again.
        // else, we copy the cardsListTemp to the cardsList.
        ArrayList<JSONObject> cardsListTemp;
        int randomNumber;
        Scanner scanner = null;
        int cardCount = 0;
        try {
            scanner = new Scanner( new InputStreamReader(getClass().getClassLoader().getResourceAsStream(gameOptions.getDBRespresenation())));
            String jsonStr = scanner.useDelimiter("\\A").next();

            JSONObject rootObject = new JSONObject(jsonStr); // Parse the JSON to a JSONObject
            JSONArray cardSets = getEquivalenceCardSets(rootObject);

            cardSets = assignHashCodesToCardsSets(cardSets);

            /*
              The number of cards we need depends on the level (number of rows and columns)
              divided by the number of the card tuple we want to form (2-card patterns, 3-card patterns, etc)
             */
            int numOfCards = (MainOptions.NUMBER_OF_COLUMNS * MainOptions.NUMBER_OF_ROWS);
            System.out.println("num of cards: " + numOfCards);

            while (cardCount < numOfCards) {
                cardsListTemp = new ArrayList<>();
                // produce a random number for the card sets (we want to select a random card set)
                randomNumber = random_int(0, cardSets.length());
                // select a random equivalence card set
                JSONArray randomCardSet = cardSets.getJSONArray(randomNumber);
                // equivalenceCardSetHashCode describes the current card set
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
                        // add card
                        cardsListTemp.add(currCard);
                        // mark category as read
                        categories.add(currCard.get("category"));

                    }

                }
                cardsList.addAll(cardsListTemp);
                cardCount += cardsListTemp.size();
            }

        } finally {
            scanner.close();
        }
        return cardsList;
    }

    /**
     * given a JSONArray of card objects, assign a unique hash code (for the equivalence set) to each card.
     * @param cardSets the set of cards
     * @return the set of cards with the hash codes
     */
    private JSONArray assignHashCodesToCardsSets(JSONArray cardSets) {
        Iterator it = cardSets.iterator();
        while(it.hasNext()) {
            String equivalenceCardSetHashCode = randomString();
            JSONArray currSet = (JSONArray) it.next();
            Iterator itCards = currSet.iterator();
            while(itCards.hasNext()) {
                JSONObject currCard = (JSONObject) itCards.next();
                currCard.put("equivalenceCardSetHashCode", equivalenceCardSetHashCode);
            }
        }
        return cardSets;
    }

    /**
     * Given a set of equivalence sets, get the shuffled set.
     * @param rootObject the set of equivalence sets
     * @return the shuffled set
     */
    public JSONArray getEquivalenceCardSets(JSONObject rootObject) {
        JSONArray cardSets = rootObject.getJSONArray("equivalence_card_sets"); // Get all JSONArray rows
        // shuffle the rows (we want the cards to be in a random order)
        cardSets = shuffleJsonArray(cardSets);
        return cardSets;
    }

    /**
     * Shuffle a JSONArray and return it.
     * @param array the initial JSONArray
     * @return the shuffled JSONArray
     * @throws JSONException
     */
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

    /**
     * Reads the current high score for the current game level and the current game.
     * @return the high score (can be null if no high score)
     */
    public String readHighScoreForCurrentLevel() {
        String highScore = "";
        Properties prop = new Properties();

        File scoresFile = new File(MainOptions.gameScoresFile);
        try {
            if(!scoresFile.exists())
                scoresFile.createNewFile();
            FileInputStream in = new FileInputStream(scoresFile);
            prop.load(in);
            highScore = prop.getProperty(String.valueOf(MainOptions.gameLevel));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScore;
    }

    /**
     * Reads the current high score for a given game level and the current game.
     * @param level the given game level (1,2,... etc)
     * @return the high score (can be null if no high score)
     */
    public String readHighScoreForLevel(String level) {
        String highScore = "";
        Properties prop = new Properties();

        File scoresFile = new File(MainOptions.gameScoresFile);
        try {
            if(!scoresFile.exists())
                scoresFile.createNewFile();
            FileInputStream in = new FileInputStream(scoresFile);
            prop.load(in);
            highScore = prop.getProperty(String.valueOf(level));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return highScore;
    }

    /**
     * Sets a given high score for the current game level and the current game.
     * @param highScore the high score to be set.
     */
    public void setHighScoreForLevel (String highScore) {

        Properties props = new Properties();

        File scoresFile = new File(MainOptions.gameScoresFile);
        try {
            if(!scoresFile.exists())
                scoresFile.createNewFile();
            FileOutputStream out = new FileOutputStream(scoresFile);
            props.setProperty(String.valueOf(MainOptions.gameLevel), highScore);
            props.store(out, null);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Produce a random integer in [Min - Max) set
     * @param Min the minimum number
     * @param Max the maximum number
     * @return a random integer in [Min - Max)
     */
    private int random_int(int Min, int Max) {
        return (int) (Math.random()*(Max-Min))+Min;
    }

    /**
     * Produces a random String.
     * @return a random String object
     */
    private String randomString() {
        SecureRandom random = new SecureRandom();
        return new BigInteger(130, random).toString(32);
    }
}
