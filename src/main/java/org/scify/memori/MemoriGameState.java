
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

package org.scify.memori;

import javafx.scene.input.KeyEvent;
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
    /**
     * Variable to flag a finished game
     */
    boolean gameFinished = false;

    boolean loadNextLevel = false;

    /**
     * indexes defining the user poistion on the GridPane
     */
    private int columnIndex = 0;
    private int rowIndex = 0;

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

    public void updateColumnIndex(KeyEvent evt) {
        switch(evt.getCode()) {
            case LEFT:
                columnIndex--;
                break;
            case RIGHT:
                columnIndex++;
                break;
            default: break;
        }
    }

    public void updateRowIndex(KeyEvent evt) {

        switch(evt.getCode()) {
            case UP:
                rowIndex--;
                break;
            case DOWN:
                rowIndex++;
                break;
            default: break;
        }
    }


    public int getColumnIndex() {

        return columnIndex;
    }

    public int getRowIndex() {
        return rowIndex;
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
