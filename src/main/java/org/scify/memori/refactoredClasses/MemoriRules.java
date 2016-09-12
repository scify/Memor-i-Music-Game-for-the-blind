package org.scify.memori.refactoredClasses;

import org.scify.memori.MainOptions;
import org.scify.memori.interfaces.refactored.*;

import java.awt.geom.Point2D;
import java.util.*;

public class MemoriRules implements Rules {

    private boolean gameFinished = false;

    @Override
    public GameState getInitialState() {
        return new MemoriGameState();
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction uaAction) {

        MemoriGameState gsCurrentState = (MemoriGameState)gsCurrent;
        MemoriTerrain memoriTerrain = (MemoriTerrain) (gsCurrentState.getTerrain());
        // currTile is the tile that was moved on or acted upon
        Tile currTile = memoriTerrain.getTile((int) uaAction.getCoords().getX(), (int) uaAction.getCoords().getY());
        System.out.println(currTile.getTileType());
        // Rules 1-4: Valid movement
        // type: movement, params: coords
        // delayed: false (zero), blocking:yes
        if(uaAction.getActionType().equals("movement")) {
            gsCurrentState.getEventQueue().add(new GameEvent("movement", uaAction.getCoords()));
        } else if(uaAction.getActionType().equals("invalidMovement")) {
            // Rule 5: Invalid movement
            // type: invalidMove, params: none
            // delayed: false, blocking:yes
            gsCurrentState.getEventQueue().add(new GameEvent("invalidMovement", uaAction.getCoords()));
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
                    gsCurrentState.getEventQueue().add(new GameEvent("success", uaAction.getCoords(), new Date().getTime() + 1500, false));
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
                        //gsCurrentState.getEventQueue().add(new GameEvent("flipBack", uaAction.getCoords(), new Date().getTime() + 1500, true));
                        memoriTerrain.addTileToOpenTiles(currTile);
                        //TODO: push new turn event (or should be handled only in renderer?
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

        // Rules 1-4: Valid movement
        // type: movement, params: coords
        // delayed: false (zero), blocking:yes
        // Rule 5: Invalid movement
        // type: invalidMove, params: none
        // delayed: false, blocking:yes
        // Rule 6: flip
        // If target card flipped
            //if card won
                //play empty sound
            //else if card not won
                // play card sound
        // else if not flipped
            // flip card
            // push flip feedback event (delayed: true, blocking:yes)
            // If last of n-tuple flipped (i.e. if we have enough cards flipped to form a tuple)
                // Fix all cards in tuple as won cards
                // Push success feedback event, DELAYED
            // else not last card in tuple
                // if at least one other open cards are different
                    // Push failure feedback event
                    // Flip card back
                    // Reset all cards (error event)
                    //gsCurrentState.getEventQueue().add(new GameEvent("reset", uaAction.getCoords(), new Date().getTime() + 1000, true));
                //else if all same or first card
                    //add card to tuple

        if(isLastRound(gsCurrent)) {
            gameFinished = true;
        }
        return gsCurrentState;
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
        return gameFinished;
    }

    public boolean isTileFlipped(Tile tile) {
        return tile.getFlipped();
    }

    public boolean isTileWon(Tile tile) {
        return tile.getWon();
    }

    public List<Point2D> resetAllOpenTiles(MemoriTerrain memoriTerrain) {
        List<Point2D> openTilesPoints = new ArrayList<>();
        System.err.println("SETTING ALL OPEN CARDS AS CLOSED");
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
            //it.remove();
        }

        return openTilesPoints;
    }

    public boolean isLastMove(MemoriGameState gsCurrent) {
        return MainOptions.NUMBER_OF_OPEN_CARDS <= gsCurrent.getiCurrentTurn();
    }

}
