package org.scify.memori.refactoredClasses;

import org.scify.memori.MainOptions;
import org.scify.memori.interfaces.refactored.GameEvent;
import org.scify.memori.interfaces.refactored.GameState;
import org.scify.memori.interfaces.refactored.Player;
import org.scify.memori.interfaces.refactored.Terrain;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class MemoriGameState implements GameState {
    protected Terrain terrain;
    protected Player pCurrent;
    protected int iCurrentTurn;
    Queue<GameEvent> gameEventQueue;

    public void nextTurn() {
        iCurrentTurn ++;
    }

    public MemoriGameState() {
        terrain = new MemoriTerrain();
        pCurrent = new Player() {
            @Override
            public int getScore() {
                return 0;
            }
        };
        iCurrentTurn = 0;
        gameEventQueue = new LinkedList();
    }


    public MemoriGameState(Player pSinglePlayer) {
        terrain = new MemoriTerrain();
        pCurrent = pSinglePlayer;
        iCurrentTurn = 0;
    }

    public Terrain getTerrain() {
        return terrain;
    }

    @Override
    public Queue<GameEvent> getEventQueue() {
        return gameEventQueue;
    }


    @Override
    public Player getCurrentPlayer() {
        return pCurrent;
    }
    @Override
    public void resetEventsQueue() {
        gameEventQueue = new LinkedList();
    }

    /**
     * CHecks if all tiles are in a won state
     * @return
     */
    public boolean areAllTilesWon() {
        boolean gameWon = true;
        for(int x = 0; x < MainOptions.NUMBER_OF_ROWS; x++) {
            for(int y = 0; y < MainOptions.NUMBER_OF_COLUMNS; y++) {
                Card currCard = (Card)terrain.getTileState(x, y);
                if(!currCard.getWon()) {
                    gameWon = false;
                    break;
                }
            }
        }
        return gameWon;
    }

}
