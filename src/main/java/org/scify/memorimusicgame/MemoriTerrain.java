
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

package org.scify.memorimusicgame;

import org.scify.memorimusicgame.card.Card;
import org.scify.memorimusicgame.card.MemoriCardDelegator;
import org.scify.memorimusicgame.games_options.GameWithLevelsOptions;
import org.scify.memorimusicgame.interfaces.GameOptions;
import org.scify.memorimusicgame.interfaces.Terrain;
import org.scify.memorimusicgame.interfaces.Tile;

import java.awt.geom.Point2D;
import java.util.*;

/**
 * Holds the basic grid of cards.
 * Responsible for creating the cards (reading from the .json file) and assigning the cards to a List
 */
public class MemoriTerrain implements Terrain {
    /**
     * Each {@link Tile} is tied with a position {@link Point2D} on the terrain
     */
    Map<Point2D, Tile> tiles;
    /**
     * When a player flips a card, this card is stored in a temporary list (gets reset at the beginning of every round)
     */
    List<Tile> openTiles;

    public Map<Point2D, Tile> getTiles() {
        return tiles;
    }

    protected GameOptions gameOptions;


    /**
     * Constructs the basic terrain, gets the {@link Card}s from the DB (.json file) and assigns them to a {@link List}.
     * @param gameOptions
     */
    public MemoriTerrain(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        tiles = new HashMap<>();
        openTiles = new ArrayList<>();
        List<Card> unshuffledCards = produceDeckOfCards((GameWithLevelsOptions) gameOptions);
        List<Card> shuffledCards = shuffleDeck(unshuffledCards);

        int cardIndex = 0;
        for (int iX = 0; iX < getWidth(); iX++) {
            for (int iY = 0; iY < getHeight(); iY++) {
                tiles.put(new Point2D.Double(iX, iY), shuffledCards.get(cardIndex));
                cardIndex++;
            }
        }
    }

    /**
     *
     * @param gameWithLevelsOptions
     * @return A {@link List} of {@link Card}s that will participate in the game
     */
    private List<Card> produceDeckOfCards(GameWithLevelsOptions gameWithLevelsOptions) {
        //cardsMap will contain the values of the json object as key-value pairs
        List<Card> cardsList;
        //Preparing the JSON parser class
        MemoriCardDelegator cardDelegator = new MemoriCardDelegator(gameWithLevelsOptions);
        //read the cards from the JSON file
        cardsList = cardDelegator.getMemoriCards();

        return cardsList;
    }

    /**
     * Shuffles the given {@link List} of {@link Card}s.
     * @param deckCards the list of cards
     * @return the shuffled list of cards
     */
    public List<Card> shuffleDeck(List<Card> deckCards) {
        long seed = System.nanoTime();
        Collections.shuffle(deckCards, new Random(seed));
        return deckCards;
    }

    @Override
    public int getWidth() {
        return MainOptions.NUMBER_OF_COLUMNS;
    }

    @Override
    public int getHeight() {
        return MainOptions.NUMBER_OF_ROWS;
    }

    /**
     * Given a row and column, find and return the corresponding card.
     * @param rowIndex the row index
     * @param columnIndex the column index
     * @return the {@link Tile} found in the requested position
     */
    @Override
    public Tile getTile(int rowIndex, int columnIndex) {
        return tiles.get(new Point2D.Double(columnIndex, rowIndex));
    }

    /**
     * Check if the card of same type is already in open cards.
     *
     * @param tile the requested tile
     * @return card state
     */
    public boolean isTileInOpenTiles(Tile tile) {
        boolean answer = false;
        if (!openTiles.isEmpty())
            for (Tile currTile : openTiles) {
                if (currTile.getLabel().equals(tile.getLabel()))
                    answer = true;
            }
        return answer;
    }

    @Override
    public String toString() {
        return tiles.toString();
    }

    /**
     * When a tile is flipped, it is then added to the open tiles temporary list.
     * @param tile the requested {@link Tile}
     */
    public void addTileToOpenTiles(Tile tile) {
        openTiles.add(tile);
    }


    /**
     * Checks if all the tiles on the Terrain are won
     * @return true if all tiles are won
     */
    @Override
    public boolean areAllTilesWon() {
        final boolean[] answer = {true};
        tiles.forEach((point, curTile) -> {
            if (!curTile.getWon())
                answer[0] = false;
        });
        return answer[0];
    }

    /**
     * When a new round starts, the open tiles {@link List} gets reset.
     */
    public void resetOpenTiles() {
        openTiles = new ArrayList<>();
    }

    public List<Tile> getOpenTiles() {
        return openTiles;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }
}
