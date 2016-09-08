package org.scify.memori.interfaces.refactored;

/**
 * Created by pisaris on 5/9/2016.
 */
public interface Terrain {
    int getWidth();
    int getHeight();
    Tile getTile(int x, int y);
    boolean areAllTilesWon();
}
