package org.scify.memori.refactoredClasses;

import org.scify.memori.FileHandler;
import org.scify.memori.MainOptions;
import org.scify.memori.interfaces.refactored.Terrain;
import org.scify.memori.interfaces.refactored.Tile;

import java.awt.geom.Point2D;
import java.util.*;

public class MemoriTerrain implements Terrain {
    Map<Point2D, Tile> tiles;
    //TODO: should openTiles be member of Terrain or MemoriGameState?
    List<Tile> openTiles;

    public Map<Point2D, Tile> getTiles() {
        return tiles;
    }

    public MemoriTerrain() {
        tiles = new HashMap<>();
        openTiles = new ArrayList<>();
        List<Card> unshuffledCards = produceDeckOfCards(MainOptions.NUMBER_OF_OPEN_CARDS);
        List<Card> shuffledCards = shuffleDeck(unshuffledCards);

        int cardIndex = 0;

        for (int iX = 0; iX < getWidth(); iX++) {
            for (int iY = 0; iY < getHeight(); iY++) {
                tiles.put(new Point2D.Double(iX, iY), shuffledCards.get(cardIndex));
                cardIndex++;
            }
        }
    }

    private List<Card> produceDeckOfCards(int cardVarieties) {

        List<Card> unShuffledCards = new ArrayList<>();

        //cardsMap will contain the values of the json object as key-value pairs
        Map<String, ArrayList<String>> cardsMap;
        //Preparing the JSON parser class
        FileHandler parser = new FileHandler();
        //read the cards from the JSON file
        cardsMap = parser.readCardsFromJSONFile();

        for (Map.Entry<String, ArrayList<String>> entry : cardsMap.entrySet()) {
            for (int cardsNum = 0; cardsNum < cardVarieties; cardsNum++) {
                ArrayList<String> cardAttrs = entry.getValue();
                //cardSounds is a comma separated string of sound files
                String cardSound = cardAttrs.get(1);
                //we need to transform it into an array and poll one sound

                org.scify.memori.refactoredClasses.Card newCard = new org.scify.memori.refactoredClasses.Card(entry.getKey(), cardAttrs.get(0), cardSound);
                unShuffledCards.add(newCard);
            }
        }
        return unShuffledCards;
    }

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

    @Override
    public Tile getTile(int x, int y) {
        return tiles.get(new Point2D.Double(y, x));
    }

    public Tile getTileByRowAndColumn(int rowIndex, int columnIndex) {
        return tiles.get(new Point2D.Double(rowIndex, columnIndex));
    }

    /**
     * Check if the card of same type is already in open cards
     *
     * @param tile the requested tile
     * @return card state
     */
    public boolean isTileInOpenTiles(Tile tile) {
        boolean answer = false;
        if (!openTiles.isEmpty())
            for (Tile currTile : openTiles) {
                if (currTile.getTileType().equals(tile.getTileType()))
                    answer = true;
            }
        return answer;
    }

    /**
     * Set the tile won
     *
     * @param tile the requested tile
     */
    public void setTileWon(Tile tile) {
        tile.setWon();
    }

    public void setAllOpenTilesWon() {
        openTiles.forEach(Tile::setWon);
        //reset open tiles list
        openTiles = new ArrayList<>();
    }

    @Override
    public String toString() {
        return tiles.toString();
    }

    /**
     * flips the Node (card) located at the position (x, y)
     * @param tile the requested tile
     */
    public void toggleTile(Tile tile) {
        //System.out.println(tile.getTileType());
        Point2D point = (Point2D) getKeyFromValue(tiles, tile.getTileType());
        System.out.println("toggled: " + point.getX() + "," + point.getY());
        tile.flip();
    }

    public void addTileToOpenTiles(Tile tile) {
        openTiles.add(tile);
    }


    @Override
    public boolean areAllTilesWon() {
        final boolean[] answer = {true};
        tiles.forEach((point, curTile) -> {
            if (!curTile.getWon())
                answer[0] = false;
        });
        return answer[0];
    }


    public Object getKeyFromValue(Map hm, Object value) {
        for (Object o : hm.keySet()) {
            Tile tile = (Tile)hm.get(o);
            if (tile.getTileType().equals(value)) {
                return o;
            }
        }
        return null;
    }

    public void resetOpenTiles() {
        openTiles = new ArrayList<>();
    }
}
