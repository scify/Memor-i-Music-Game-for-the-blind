package org.scify.windmusicgame.card;

import org.scify.windmusicgame.MainOptions;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Responsible for loading the cards for the game
 */
public class MemoriCardDelegator extends CardDBHandlerJSON{

    public MemoriCardDelegator(GameWithLevelsOptions gameWithLevelsOptions) {
        super(gameWithLevelsOptions);
    }



    public List<Card> getMemoriCards() {
        /*
          The number of cards we need depends on the level (number of rows and columns)
          divided by the number of the card tuple we want to form (2-card patterns, 3-card patterns, etc)
         */
        int numOfCards = (MainOptions.NUMBER_OF_COLUMNS * MainOptions.NUMBER_OF_ROWS);
        System.out.println("num of cards needed: " + numOfCards);
        return shuffleCards(getCardsFromDB(numOfCards));
    }

    /**
     * Shuffles the given {@link List} of {@link Card}s.
     * @param cards the list of cards
     * @return the shuffled list of cards
     */
    private List<Card> shuffleCards(List<Card> cards) {
        long seed = System.nanoTime();
        Collections.shuffle(cards, new Random(seed));
        return cards;
    }

}
