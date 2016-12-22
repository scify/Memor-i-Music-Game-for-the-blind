
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

import javafx.scene.input.KeyEvent;
import org.scify.memorimusicgame.interfaces.*;

import java.util.LinkedList;
import java.util.Queue;

public class MemoriGameState implements GameState {
    /**
     * The terrain holding all the UI elements
     */
    protected Terrain terrain;
    protected Player pCurrent;
    protected int iCurrentTurn;
    /**
     * A queue containing the game events (handled by the renderer Class)
     */
    Queue<GameEvent> gameEventQueue;
    /**
     * Variable to flag a finished game
     */
    boolean gameFinished = false;
    /**
     * Variable to indicate whether a new game should start immediately
     */
    boolean loadNextLevel = false;
    /**
     * Variable to indicate whether the current lavel should replay
     */
    public boolean replayLevel;
    /**
     * indexes defining the user poistion on the GridPane
     */
    private int columnIndex = 0;
    private int rowIndex = 0;


    GameOptions gameOptions;

    public MemoriGameState(GameOptions gameOptions) {
        this.gameOptions = gameOptions;
        terrain = new MemoriTerrain(gameOptions);
        pCurrent = new Player() {
            @Override
            public int getScore() {
                return 0;
            }
        };
        iCurrentTurn = 0;
        gameEventQueue = new LinkedList();
    }


    public MemoriGameState(Player pSinglePlayer, GameOptions gameOptions) {
        terrain = new MemoriTerrain(gameOptions);
        pCurrent = pSinglePlayer;
        iCurrentTurn = 0;
    }

    /**
     * Updates the column index based on the Key event passed
     * @param evt the Key Event
     */
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

    /**
     * Updates the row index based on the Key event passed
     * @param evt the Key Event
     */
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

    /**
     * Get the terrain that the UI elements are laid on
     */
    public Terrain getTerrain() {
        return terrain;
    }

    /**
     * Getter for the game events queue
     * @return A LinkedList representation of the game events queue
     */
    @Override
    public LinkedList<GameEvent> getEventQueue() {
        return (LinkedList)gameEventQueue;
    }


    @Override
    public Player getCurrentPlayer() {
        return pCurrent;
    }

    /**
     * Empties the game events queue
     */
    @Override
    public void resetEventsQueue() {
        gameEventQueue = new LinkedList();
    }

    /**
     * Checks if all tiles are in a won state
     * @return true if all tiles are won
     */
    public boolean areAllTilesWon() {
        //System.out.println("won: " + terrain.areAllTilesWon());
        return terrain.areAllTilesWon();
    }

    public Queue<GameEvent> getGameEventQueue() {
        return gameEventQueue;
    }

    public void setLoadNextLevel(boolean loadNextLevel) {
        this.loadNextLevel = loadNextLevel;
    }

    public void setGameFinished(boolean gameFinished) {

        this.gameFinished = gameFinished;
    }

    public boolean isGameFinished() {
        return gameFinished;
    }

    public boolean isLoadNextLevel() {
        return loadNextLevel;
    }

    public boolean isReplayLevel() {
        return replayLevel;
    }

    public GameOptions getGameOptions() {
        return gameOptions;
    }
}
