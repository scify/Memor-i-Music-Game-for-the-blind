package org.scify.memori.refactoredClasses;

import org.scify.memori.MainOptions;
import org.scify.memori.interfaces.refactored.*;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MemoriRules implements Rules {

    @Override
    public GameState getInitialState() {
        return new MemoriGameState();
    }

    @Override
    public GameState getNextState(GameState gsCurrent, UserAction uaAction) {
        // Rules 1-4: Valid movement
        // type: movement, params: coords
        // delayed: false, blocking:yes

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
                    // Reset all cards (error event), DELAYED
                    //gsCurrentState.getEventQueue().add(new GameEvent("reset", uaAction.getCoords(), new Date().getTime() + 1000, true));
                //else if all same or first card
                    //add card to tuple



        MemoriGameState gsCurrentState = (MemoriGameState)gsCurrent;
        gsCurrentState.resetEventsQueue();
        MemoriTerrain memoriTerrain = (MemoriTerrain) (gsCurrentState.getTerrain());
        Tile currTile = memoriTerrain.getTile((int) uaAction.getCoords().getX(), (int) uaAction.getCoords().getY());
        //System.out.println("user action at: " + (int) uaAction.getCoords().getX() + "," + (int) uaAction.getCoords().getY());

        if (uaAction.getActionType().equals("flip")) {
            gsCurrentState.nextTurn(); // TODO: Remove
            //check if the tile is already open
            if(isTileOpen(currTile)) {
                System.err.println("OPEN TILE");
                //play the card music
                gsCurrentState.getEventQueue().add(new GameEvent("music", uaAction.getCoords()));
            } else {
                memoriTerrain.toggleTile(currTile);
                gsCurrentState.getEventQueue().add(new GameEvent("flip", uaAction.getCoords()));
                //if a similar card is available in open cards, we have a pattern
                if(memoriTerrain.isTileInOpenTiles(currTile)) {
                    gsCurrentState.getEventQueue().add(new GameEvent("won", uaAction.getCoords()));
                    //set all open cards (that compose the pattern) as won cards
                    memoriTerrain.setAllOpenTilesWon();
                    ((MemoriGameState) gsCurrent).resetTurn();
                } else {
                    if(!memoriTerrain.openTiles.isEmpty()) {
                        System.err.println("WRONG PATTERN");
                        gsCurrentState.getEventQueue().add(new GameEvent("invalidPattern", uaAction.getCoords()));
                        //TODO: close all open cards
                        memoriTerrain.addTileToOpenTiles(currTile);
                        resetAllOpenCards(memoriTerrain);
                    } else {
                        memoriTerrain.addTileToOpenTiles(currTile);
                    }
                }
            }

        } else if(uaAction.getActionType().equals("movement")) {
            gsCurrentState.getEventQueue().add(new GameEvent("movement", uaAction.getCoords()));
        }
        else
            gsCurrentState.getEventQueue().add(new GameEvent("invalidAction", uaAction.getCoords()));
        return gsCurrentState;
    }

    @Override
    public boolean isGameFinished(GameState gsCurrent) {
        return ((MemoriGameState)gsCurrent).areAllTilesWon();
//        return false;
    }

    public boolean isTileOpen(Tile tile) {
        return tile.getFlipped();
    }

    public void resetAllOpenCards(MemoriTerrain memoriTerrain) {
        System.err.println("BEFORE SETTING ALL OPEN CARDS AS CLOSED");
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.submit(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.err.println("SETTING ALL OPEN CARDS AS CLOSED");
            memoriTerrain.openTiles.forEach(Tile::flip);
        });
    }

    public boolean isLastMove(MemoriGameState gsCurrent) {
        return MainOptions.NUMBER_OF_OPEN_CARDS <= gsCurrent.getiCurrentTurn();
    }

}
