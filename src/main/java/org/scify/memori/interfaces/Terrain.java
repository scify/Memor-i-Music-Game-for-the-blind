package org.scify.memori.interfaces;

/**
 * Created by pisaris on 5/9/2016.
 */
public interface Terrain {
    int getWidth();
    int getHeight();
    String getTileState(int x, int y);
}
