package org.scify.memori.memoripoc;

import org.scify.memori.interfaces.GameState;
import org.scify.memori.interfaces.Player;
import org.scify.memori.interfaces.Terrain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pisaris on 5/9/2016.
 */
public class FlipGameState implements GameState {
    protected Terrain t;
    protected List<String> lsEvents;
    protected Player pCurrent;
    protected int iCurrentTurn;

    public void nextTurn() {
        iCurrentTurn ++;
    }

    public FlipGameState() {
        t = new FlipTerrain();
        lsEvents = new ArrayList<String>();
        pCurrent = new Player() {
            @Override
            public int getScore() {
                return 0;
            }
        };
        iCurrentTurn = 0;

    }


    public FlipGameState(Player pSinglePlayer) {
        t = new FlipTerrain();
        lsEvents = new ArrayList<String>();
        pCurrent = pSinglePlayer;
        iCurrentTurn = 0;
    }

    public Terrain getTerrain() {
        return t;
    }

    public List<String> getEvents() {
        return lsEvents;
    }

    @Override
    public Player getCurrentPlayer() {
        return pCurrent;
    }


}
