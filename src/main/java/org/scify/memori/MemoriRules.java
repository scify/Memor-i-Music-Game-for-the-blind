

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
import org.scify.memori.interfaces.*;
import org.scify.memori.interfaces.*;

import java.awt.geom.Point2D;
import java.util.*;

public class MemoriRules implements Rules {
    private HighScoreHandler highScore;
    TimeWatch watch;
    public MemoriRules() {
        highScore = new HighScoreHandler();
        watch = TimeWatch.start();
    }
    @Override
    public GameState getInitialState() {
        return new MemoriGameState();
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction uaAction) {


        MemoriGameState gsCurrentState = (MemoriGameState)gsCurrent;
        //detect if valid movement and update row and column indexes
        if(movementValid(uaAction.getKeyEvent(), gsCurrentState)) {
            gsCurrentState.updateRowIndex(uaAction.getKeyEvent());
            gsCurrentState.updateColumnIndex(uaAction.getKeyEvent());
            uaAction.setCoords(new Point2D.Double(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex()));
        } else {
            // if invalid movement, return only an invalid game event
            gsCurrentState.getEventQueue().add(new GameEvent("invalidMovement", new Point2D.Double(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex())));
            return gsCurrentState;
        }
        MemoriTerrain memoriTerrain = (MemoriTerrain) (gsCurrentState.getTerrain());
        // currTile is the tile that was moved on or acted upon
        Tile currTile = memoriTerrain.getTile(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex());
        System.out.println(currTile.getTileType());
        // Rules 1-4: Valid movement
        // type: movement, params: coords
        // delayed: false (zero), blocking:yes
        if(uaAction.getActionType().equals("movement")) {
            gsCurrentState.getEventQueue().add(new GameEvent("movement", uaAction.getCoords()));
        } else if (uaAction.getActionType().equals("flip")) {
            // Rule 6: flip
            // If target card flipped
            if(isTileFlipped(currTile)) {
                //if card won
                if(isTileWon(currTile)) {
                    //play empty sound
                    gsCurrentState.getEventQueue().add(new GameEvent("empty", uaAction.getCoords()));
                } else {
                    //else if card not won
                    // play card sound
                    gsCurrentState.getEventQueue().add(new GameEvent("cardSound", uaAction.getCoords(), 0, true));
                }
            } else {
                System.out.println("tile not flipped");
                // else if not flipped
                // flip card
                flipTile(currTile);
                // push flip feedback event (delayed: false, blocking: no)
                gsCurrentState.getEventQueue().add(new GameEvent("flip", uaAction.getCoords()));
                gsCurrentState.getEventQueue().add(new GameEvent("cardSound", uaAction.getCoords()));
                if(tileIsLastOfTuple(memoriTerrain, currTile)) {
                    // If last of n-tuple flipped (i.e. if we have enough cards flipped to form a tuple)
                    gsCurrentState.getEventQueue().add(new GameEvent("success", uaAction.getCoords(), new Date().getTime() + 1500, true));
                    //if in tutorial mode, push explaining events
                    if(MainOptions.TUTORIAL_MODE) {
                        System.err.println("tutorial mode");
                        gsCurrentState.getEventQueue().add(new GameEvent("success", uaAction.getCoords(), new Date().getTime() + 1500, false));
                    }
                    // add tile to open tiles
                    memoriTerrain.addTileToOpenTiles(currTile);
                    // set all open cards won
                    setAllOpenTilesWon(memoriTerrain);
                    //reset open tiles
                    memoriTerrain.resetOpenTiles();
                } else {
                    // else not last card in tuple
                    if(atLeastOneOtherTileIsDifferent(memoriTerrain, currTile)) {
                        // Flip card back
                        // flipTile(currTile);
                        memoriTerrain.addTileToOpenTiles(currTile);
                        //TODO: push new turn event
                        // Reset all tiles
                        List<Point2D> openTilesPoints = resetAllOpenTiles(memoriTerrain);
                        memoriTerrain.resetOpenTiles();
                        for (Iterator<Point2D> iter = openTilesPoints.iterator(); iter.hasNext(); ) {
                            Point2D position = iter.next();
                                gsCurrentState.getEventQueue().add(new GameEvent("flipBack", position, new Date().getTime() + 1500, false));
                        }
                        // Push failure feedback event
                        gsCurrentState.getEventQueue().add(new GameEvent("failure", uaAction.getCoords(), new Date().getTime() + 1500, false));
                    } else {
                        memoriTerrain.addTileToOpenTiles(currTile);
                    }
                }
            }
        }

        //if last round, create appropriate READY_TO_FINISH event
        if(isLastRound(gsCurrent)) {
            //if ready to finish event already in events queue
            if(eventsQueueContainsEvent(gsCurrent.getEventQueue(), "READY_TO_FINISH")) {
                //listen for user action indicating game over
                if(uaAction.getActionType().equals("quit")) {
                    //the game should finish but not load a next level
                    gsCurrentState.loadNextLevel = false;
                    gsCurrentState.gameFinished = true;
                }
                if(uaAction.getActionType().equals("nextLevel")) {
                    //the game should finish and load a next level
                    gsCurrentState.loadNextLevel = true;
                    gsCurrentState.gameFinished = true;
                }
                //TODO: allow either returning to main screen, or go to next level
            } else {
                //add appropriate event
                gsCurrentState.getEventQueue().add(new GameEvent("READY_TO_FINISH", ""));
                //add UI events
                gsCurrentState.getEventQueue().add(new GameEvent("success", uaAction.getCoords(), new Date().getTime() + 1500, true));
                //TODO: Add event informing the user about either returning to main screen or starting next level
                highScore.updateHighScore(watch);
            }
        }

        return gsCurrentState;
    }

    private boolean eventsQueueContainsEvent(Queue<GameEvent> eventQueue, String eventType) {
        Iterator<GameEvent> iter = eventQueue.iterator();
        GameEvent currentGameEvent;
        while (iter.hasNext()) {
            currentGameEvent = iter.next();
            if(currentGameEvent.type.equals(eventType))
                return true;
        }
        return false;
    }

    private boolean isLastRound(GameState gsCurrent) {
        return ((MemoriGameState)gsCurrent).areAllTilesWon();
    }

    /**
     * Sets the tuple as won
     * @param memoriTerrain the terrain containing the open tiles tuple
     */
    private void setAllOpenTilesWon(MemoriTerrain memoriTerrain) {
        for (Tile openTile : memoriTerrain.openTiles) {
            openTile.setWon();
        }
    }

    private boolean atLeastOneOtherTileIsDifferent(MemoriTerrain memoriTerrain, Tile currTile) {
        boolean answer = false;
        for (Iterator<Tile> iter = memoriTerrain.openTiles.iterator(); iter.hasNext(); ) {
            Tile element = iter.next();
            if(!Objects.equals(element.getTileType(), currTile.getTileType()))
                answer = true;
        }
        return answer;
    }

    private boolean tileIsLastOfTuple(MemoriTerrain memoriTerrain, Tile currTile) {
        boolean answer = false;
        for (Iterator<Tile> iter = memoriTerrain.openTiles.iterator(); iter.hasNext(); ) {
            Tile element = iter.next();
            if(Objects.equals(element.getTileType(), currTile.getTileType()))
                if(memoriTerrain.openTiles.size() == MainOptions.NUMBER_OF_OPEN_CARDS - 1)
                    answer = true;
        }
        return answer;
    }

    private void flipTile(Tile currTile) {
        currTile.flip();
    }



    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        MemoriGameState memoriGameState = (MemoriGameState)gsCurrent;
        return memoriGameState.gameFinished;
    }

    public boolean isTileFlipped(Tile tile) {
        return tile.getFlipped();
    }

    public boolean isTileWon(Tile tile) {
        return tile.getWon();
    }

    public List<Point2D> resetAllOpenTiles(MemoriTerrain memoriTerrain) {
        List<Point2D> openTilesPoints = new ArrayList<>();
        memoriTerrain.openTiles.forEach(Tile::flip);
        for (Iterator<Tile> iter = memoriTerrain.openTiles.iterator(); iter.hasNext(); ) {
            Tile element = iter.next();
            Iterator it = memoriTerrain.tiles.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry pair = (Map.Entry)it.next();
                if(element == pair.getValue()) {
                    Point2D pos = (Point2D)pair.getKey();
                    openTilesPoints.add(new Point2D.Double(pos.getY(), pos.getX()));
                }
            }
        }

        return openTilesPoints;
    }

    /**
     * Determines whether the user move was valid
     * @param evt
     * @return true if the user move was valid
     */
    public boolean movementValid(KeyEvent evt, MemoriGameState memoriGameState) {
        switch(evt.getCode()) {
            case LEFT:
                if(memoriGameState.getColumnIndex() == 0) {
                    return false;
                }
                break;
            case RIGHT:
                if(memoriGameState.getColumnIndex() == MainOptions.NUMBER_OF_COLUMNS - 1) {
                    return false;
                }
                break;
            case UP:
                if(memoriGameState.getRowIndex() == 0) {
                    return false;
                }
                break;
            case DOWN:
                if(memoriGameState.getRowIndex() == MainOptions.NUMBER_OF_ROWS - 1) {
                    return false;
                }
                break;
        }
        return true;
    }

}
