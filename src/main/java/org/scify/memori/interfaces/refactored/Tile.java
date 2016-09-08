package org.scify.memori.interfaces.refactored;

public interface Tile {
    void flip();
    String getTileType();
    void setWon();
    boolean getFlipped();
    boolean getWon();
}
