package org.scify.memorimusicgame.card;

import org.json.JSONArray;
import org.json.JSONObject;
import org.scify.memorimusicgame.MainOptions;
import org.scify.memorimusicgame.MemoriGameLevel;
import org.scify.memorimusicgame.games_options.GameWithLevelsOptions;
import org.scify.memorimusicgame.helper.JSONFileHandler;
import org.scify.memorimusicgame.helper.MemoriConfiguration;
import org.scify.memorimusicgame.interfaces.CardDBHandler;
import org.scify.memorimusicgame.utils.ResourceLocator;

import java.io.InputStreamReader;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

public class CardDBHandlerJSON implements CardDBHandler {

    public JSONFileHandler jsonFileHandler;
    private String dbFile;

    public CardDBHandlerJSON(GameWithLevelsOptions gameWithLevelsOptions) {
        jsonFileHandler = new JSONFileHandler();
        MemoriGameLevel gameLevel = gameWithLevelsOptions.getGameLevels().get(MainOptions.gameLevel - 1);
        ResourceLocator resourceLocator = new ResourceLocator();
        dbFile = resourceLocator.getCorrectPathForFile("json_DB/", gameLevel.getJSONDBFileForLevel());
        //because we want to perform getResourceAsStream on the dbFile, we need to eliminate the slash "/" that the string starts with:
        if (dbFile.charAt(0) == '/') {
            dbFile = dbFile.substring(1, dbFile.length());
        }
    }

    @Override
    public List<Card> getCardsFromDB(int numOfCards) {
        JSONArray initialObjectsSet = getObjectFromJSONFile(dbFile, "equivalence_card_sets");

        ArrayList<Object> setObjects = extractObjectsFromJSONArray(initialObjectsSet, numOfCards);
        JSONFileHandler jsonFileHandler = new JSONFileHandler();
        List<Card> cardSet = new ArrayList<>();
        Iterator it = setObjects.iterator();
        while(it.hasNext()) {
            JSONObject currObj = (JSONObject) it.next();
            Card newCard = new CategorizedCard(
                    (String) currObj.get("label"),
                    jsonFileHandler.jsonArrayToStringArray((JSONArray) currObj.get("images")),
                    jsonFileHandler.jsonArrayToStringArray((JSONArray) currObj.get("sounds")),
                    jsonFileHandler.jsonArrayToStringArray((JSONArray) currObj.get("descriptiveSounds")),
                    (String)currObj.get("category"),
                    (String)currObj.get("equivalenceCardSetHashCode"),
                    (String)currObj.get("nameSound")
            );
            cardSet.add(newCard);
        }
        return cardSet;
    }

    /**
     * Given a json file, read the root object (identified by the second parameter)
     * @param dbFile the db file path
     * @param objectId the id identifying the desired object
     * @return
     */
    public JSONArray getObjectFromJSONFile(String dbFile, String objectId) {
        JSONArray objectSets;
        Scanner scanner = null;
        try {

            scanner = new Scanner(new InputStreamReader(getClass().getClassLoader().getResourceAsStream(dbFile)));
            String jsonStr = scanner.useDelimiter("\\A").next();

            JSONObject rootObject = new JSONObject(jsonStr); // Parse the JSON to a JSONObject
            objectSets = jsonFileHandler.getJSONArrayFromObject(rootObject, objectId);
            objectSets = assignHashCodesToCardsSets(objectSets);

        } finally {
            assert scanner != null;
            scanner.close();
        }
        return objectSets;
    }


    /**
     * Extract a specific number of  {@link JSONObject}, Given a set of Json Objects
     * @param cardSets the set of Json objects
     * @param numOfCards the number of objects to be extracted
     * @return a subset of the initial set
     */
    private ArrayList<Object> extractObjectsFromJSONArray(JSONArray cardSets, int numOfCards) {
        ArrayList<JSONObject> cardsListTemp;
        ArrayList<Object> extractedCards = new ArrayList<>();
        int randomNumber;
        int cardCount = 0;
        while (cardCount < numOfCards) {
            cardsListTemp = new ArrayList<>();
            // produce a random number for the card sets (we want to select a random card set)
            randomNumber = random_int(0, cardSets.length());
            // select a random equivalence card set
            JSONArray randomCardSet = cardSets.getJSONArray(randomNumber);
            // equivalenceCardSetHashCode describes the current card set
            // shuffle the selected card set so that we pick random cards
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
                        if(extractedCards.contains(currCard)) {
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
            extractedCards.addAll(cardsListTemp);
            cardCount += cardsListTemp.size();
        }
        return extractedCards;
    }


    /**
     * given a JSONArray of card objects, assign a unique hash code (for the equivalence set) to each card.
     * @param cardSets the set of cards
     * @return the set of cards with the hash codes
     */
    public JSONArray assignHashCodesToCardsSets(JSONArray cardSets) {
        Iterator it = cardSets.iterator();
        while(it.hasNext()) {
            String equivalenceCardSetHashCode = randomString();
            JSONArray currSet = (JSONArray) it.next();
            Iterator itCards = currSet.iterator();
            while(itCards.hasNext()) {
                JSONObject currCard = (JSONObject) itCards.next();
                if(currCard.get("equivalenceCardSetHashCode").equals(""))
                    currCard.put("equivalenceCardSetHashCode", equivalenceCardSetHashCode);
            }
        }
        return cardSets;
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
