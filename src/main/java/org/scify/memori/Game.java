package org.scify.memori;

import javafx.stage.Stage;

import java.util.*;


public class Game {

    private UserActions mUserActions = new UserActions();
    private PairDiscoveryRules mRules = new PairDiscoveryRules();
    private GameState gsCurrentState = mRules.getInitialGameState();
    JavaFXUI ui;

    private Stage prevWindow;


    public Game(Stage window) {
        prevWindow = window;
    }

    public void start(Stage primaryStage) throws Exception{
        System.out.println("Game start called");
        ui = new JavaFXUI();
        ui.initUi(primaryStage, prevWindow);

        List<Card> unShuffledCards = new ArrayList<>();

        //cardsMap will contain the values of the json object as key-value pairs
        Map<String, ArrayList<String>> cardsMap;
        //Preparing the JSON parser class
        FileHandler parser = new FileHandler();
        //read the cards from the JSON file
        cardsMap = parser.readCardsFromJSONFile();

        //given the cards ids and image names, we produce the deck with all the cards
        produceDeckOfCards(mRules.getNumberOfOpenCards(), cardsMap, unShuffledCards, mRules);

        int cardIndex = 0;
        for(int i = 1; i <= mRules.getNumberOfColumns(); i++) {
            for (int j = 1; j <= mRules.getNumberOfRows(); j++) {
                gsCurrentState.getDeckCards().get(cardIndex).setxPos(i);
                gsCurrentState.getDeckCards().get(cardIndex).setyPos(j);
                cardIndex++;
            }
        }

        gsCurrentState.shuffleDeck();
        List<Card> shuffledCards = gsCurrentState.getDeckCards();
        mUserActions.assignCardClickHandlers(shuffledCards, mRules, gsCurrentState);


        ui.addCardsToTerrain(shuffledCards, unShuffledCards);

        ui.begin();
    }

    private void produceDeckOfCards(int cardVarieties, Map<String, ArrayList<String>> cardsMap, List<Card> unShuffledCards, PairDiscoveryRules rules) {
        for (Map.Entry<String, ArrayList<String>> entry : cardsMap.entrySet()) {
            for(int cardsNum = 0; cardsNum < cardVarieties; cardsNum++) {
                ArrayList<String> cardAttrs = entry.getValue();
                //cardSounds is a comma separated string of sound files
                String cardSound = cardAttrs.get(1);
                //we need to transform it into an array and poll one sound

                Card newCard = new Card(entry.getKey(), cardAttrs.get(0), cardSound, rules);
                gsCurrentState.addCardToDeck(newCard);
                unShuffledCards.add(newCard);
            }
        }
    }



}
