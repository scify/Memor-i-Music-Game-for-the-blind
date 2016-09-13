package org.scify.memori;

import org.scify.memori.interfaces.GameEvent;
import org.scify.memori.interfaces.GameState;
import org.scify.memori.interfaces.Player;
import org.scify.memori.interfaces.Terrain;

import java.util.LinkedList;
import java.util.Queue;

public class MemoriGameState implements GameState {
    protected Terrain terrain;
    protected Player pCurrent;
    protected int iCurrentTurn;
    Queue<GameEvent> gameEventQueue;

    public void nextTurn() {
        iCurrentTurn ++;
    }

    public int getiCurrentTurn() {
        return iCurrentTurn;
    }

    public void resetTurn() {
        iCurrentTurn = 0;
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
    public LinkedList<GameEvent> getEventQueue() {
        return (LinkedList)gameEventQueue;
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
        //System.out.println("won: " + terrain.areAllTilesWon());
        return terrain.areAllTilesWon();
    }

}
