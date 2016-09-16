

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

import javafx.scene.input.KeyCode;
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
        List<GameEvent> eventsList = gsCurrentState.getEventQueue();
        eventsList.size();
        //Iterate through game events. If there is a blocking event, return.
        ListIterator<GameEvent> listIterator = gsCurrentState.getEventQueue().listIterator();
        while (listIterator.hasNext()) {
            GameEvent currentGameEvent = listIterator.next();
            if(currentGameEvent.blocking)
                return gsCurrentState;
        }

        if(movementValid(uaAction.getKeyEvent(), gsCurrentState)) {
            gsCurrentState.updateRowIndex(uaAction.getKeyEvent());
            gsCurrentState.updateColumnIndex(uaAction.getKeyEvent());
            uaAction.setCoords(new Point2D.Double(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex()));
        } else {
            // if invalid movement, return only an invalid game event
            gsCurrentState.getEventQueue().add(new GameEvent("invalidMovement", new Point2D.Double(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex())));
            if(MainOptions.TUTORIAL_MODE) {
                if (!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_INVALID_MOVEMENT")) {
                    gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_INVALID_MOVEMENT_UI", new Point2D.Double(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex())));
                    gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_INVALID_MOVEMENT", new Point2D.Double(gsCurrentState.getRowIndex(), gsCurrentState.getColumnIndex())));
                }
            }
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
            if(MainOptions.TUTORIAL_MODE) {
                if (eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_0"))
                    performFlip(currTile, gsCurrentState, uaAction, memoriTerrain);
            }
            else
                performFlip(currTile, gsCurrentState, uaAction, memoriTerrain);
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

        if(MainOptions.TUTORIAL_MODE)
            tutorialRulesSet(gsCurrentState, uaAction);

        return gsCurrentState;
    }

    private void performFlip(Tile currTile, MemoriGameState gsCurrentState, UserAction uaAction, MemoriTerrain memoriTerrain) {
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
            gsCurrentState.getEventQueue().add(new GameEvent("DOOR_OPEN", uaAction.getCoords(), 0,true));
            gsCurrentState.getEventQueue().add(new GameEvent("cardSound", uaAction.getCoords(), new Date().getTime() + 1200, false));
            if(MainOptions.TUTORIAL_MODE){
                if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "FLIP_EXPLANATION")) {
                    //add tutorial_3 event to queue
                    gsCurrentState.getEventQueue().add(new GameEvent("FLIP_EXPLANATION", uaAction.getCoords()));
                    // add tutorial_3 UI event to queue
                    gsCurrentState.getEventQueue().add(new GameEvent("FLIP_EXPLANATION_UI", uaAction.getCoords(), new Date().getTime() + 3000, true));

                }
            }
            if(tileIsLastOfTuple(memoriTerrain, currTile)) {
                // If last of n-tuple flipped (i.e. if we have enough cards flipped to form a tuple)
                gsCurrentState.getEventQueue().add(new GameEvent("success", uaAction.getCoords(), new Date().getTime() + 2500, true));
                //if in tutorial mode, push explaining events
                if(MainOptions.TUTORIAL_MODE) {
                    if (!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_CORRECT_PAIR")) {
                        gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_CORRECT_PAIR", uaAction.getCoords()));
                        gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_CORRECT_PAIR_UI", uaAction.getCoords(), new Date().getTime() + 2500, true));
                    }
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
                        gsCurrentState.getEventQueue().add(new GameEvent("flipBack", position, new Date().getTime() + 2500, false));
                    }
                    // Push failure feedback event
                    //gsCurrentState.getEventQueue().add(new GameEvent("failure", uaAction.getCoords(), new Date().getTime() + 1500, false));

                    //TODO: Add doors closed sound effect
                    gsCurrentState.getEventQueue().add(new GameEvent("DOORS_CLOSED", uaAction.getCoords(), new Date().getTime() + 3000, false));
                    gsCurrentState.getEventQueue().add(new GameEvent("DOORS_CLOSED", uaAction.getCoords(), new Date().getTime() + 3300, false));
                    if(MainOptions.TUTORIAL_MODE) {
                        if (!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_WRONG_PAIR")) {
                            gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_WRONG_PAIR", uaAction.getCoords()));
                            gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_WRONG_PAIR_UI", uaAction.getCoords(), new Date().getTime() + 4000, true));
                        }
                    }
                    if(MainOptions.TUTORIAL_MODE) {
                        if (!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_DOORS_CLOSED")) {
                            gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_DOORS_CLOSED", uaAction.getCoords()));
                            gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_DOORS_CLOSED_UI", uaAction.getCoords(), new Date().getTime() + 6000, true));
                        }
                    }
                } else {
                    memoriTerrain.addTileToOpenTiles(currTile);
                }
            }
        }
    }

    private void tutorialRulesSet(MemoriGameState gsCurrentState, UserAction uaAction) {

            // if tutorial_0 event does not exist
                //If user clicked space
                    // add tutorial_0 event to queue
                    // add tutorial_0 UI event to queue
            // else if tutorial_0 event exists
                //if tutorial_1 event does not exist
                    //if user clicked RIGHT
                        //add tutorial_1 event to queue
                        //add tutorial_1 UI event to queue
                    //else  if user did not click RIGHT
                        //add UI event indicating that the user should click RIGHT
                //else if tutorial_1 event exists
                    //if tutorial_2 event does not exist
                        //if user clicked LEFT
                            //add tutorial_2 event to queue
                            //add tutorial_2 UI event to queue
                        //else if user did not click LEFT
                            //add UI event indicating that the user should click RIGHT
                    //else if tutorial_2 event exists
                        //if tutorial_3 event does not exist
                            //if user clicked FLIP
                                //add tutorial_3 event to queue
                                //add tutorial_3 UI event to queue
                        //if tutorial_3 event exists
        // if tutorial_0 event does not exist
        if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_0")) {
            //If user clicked space
            if (uaAction.getActionType().equals("flip")) {
                // add tutorial_0 event to queue
                gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_0", uaAction.getCoords()));
                // add tutorial_0 UI event to queue
                gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_0_UI", uaAction.getCoords(), 0, true));
            }
            // else if tutorial_0 event exists
        } else {
            //if tutorial_1 event does not exist
            if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_1_STEP_1")) {
                //if user clicked RIGHT
                if (uaAction.getDirection() == KeyCode.RIGHT) {
                    //add tutorial_1 event to queue
                    gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_1_STEP_1", uaAction.getCoords()));
                    //add tutorial_1 UI event to queue
                    gsCurrentState.getEventQueue().add(new GameEvent("GO_RIGHT_AGAIN", uaAction.getCoords(), 0, true));
                } //else  if user did not click RIGHT
                else {
                    gsCurrentState.getEventQueue().add(new GameEvent("NOT_RIGHT_UI", uaAction.getCoords(), 0, true));
                }
                //else if tutorial_1 event exists
            } else {
                if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_1_STEP_2")) {
                    //if user clicked RIGHT
                    if (uaAction.getDirection() == KeyCode.RIGHT) {
                        //add tutorial_1 event to queue
                        gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_1_STEP_2", uaAction.getCoords()));
                    } //else  if user did not click RIGHT
                    else {
                        gsCurrentState.getEventQueue().add(new GameEvent("NOT_RIGHT_UI", uaAction.getCoords(), 0, true));
                    }
                    //else if tutorial_1 event exists
                } else {
                    //if tutorial_2 event does not exist
                    if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_2")) {
                        //if user clicked LEFT
                        if (uaAction.getDirection() == KeyCode.LEFT) {
                            //add tutorial_2 event to queue
                            gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_2", uaAction.getCoords()));
                            // add tutorial_2 UI event to queue
                            gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_2_UI", uaAction.getCoords(), new Date().getTime() + 500, false));
                        } //else if user did not click LEFT
                        else {
                            //add UI event indicating that the user should click LEFT
                            gsCurrentState.getEventQueue().add(new GameEvent("NOT_LEFT_UI", uaAction.getCoords(), 0, true));
                        }
                    }//else if tutorial_2 event exists
                    else {
                        //if tutorial_3 event does not exist
                        if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "DOORS_EXPLANATION")) {
                            //if user clicked ENTER
                            if (uaAction.getActionType().equals("enter")) {
                                //add tutorial_3 event to queue
                                gsCurrentState.getEventQueue().add(new GameEvent("DOORS_EXPLANATION", uaAction.getCoords()));
                                // add tutorial_3 UI event to queue
                                gsCurrentState.getEventQueue().add(new GameEvent("DOORS_EXPLANATION_UI", uaAction.getCoords(), 0, true));
                            } else {
                                //gsCurrentState.getEventQueue().add(new GameEvent("NOT_ENTER_UI", uaAction.getCoords(), 0, false));
                            }
                        }
                    }
                }

            }
        }
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
