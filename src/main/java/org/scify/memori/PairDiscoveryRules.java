package org.scify.memori;

import org.scify.memori.interfaces.Rules;

public class PairDiscoveryRules implements Rules{

    private GameState gameState = new GameState();

    public int getNumberOfColumns() {
        return MainOptions.NUMBER_OF_COLUMNS;
    }

    public int getNumberOfRows() {
        return MainOptions.NUMBER_OF_ROWS;
    }

    public int getNumberOfOpenCards() {
        return MainOptions.NUMBER_OF_OPEN_CARDS;
    }

    public boolean isLastMove(GameState gsCurrent) {
        return MainOptions.NUMBER_OF_OPEN_CARDS <= gsCurrent.getMoveCount();
    }

    public GameState getInitialGameState() {
        return gameState;
    }

    public boolean sameCardClickedTwice(Card card, GameState gameState) {
        //check if the cards exists in open cards stack
        return gameState.isSameCardInOpenCards(card);
    }

    public boolean isGameOver(GameState gameState) {
        //our only condition to determine if the game is over is if all deck cards are open
        boolean allCardsFlipped = true;
        for(Card currCard: gameState.getDeckCards()) {
            if(!currCard.getFlipped()) {
                allCardsFlipped = false;
                break;
            }
        }
        return allCardsFlipped;

    }

    public boolean anotherCardExists(Card card, GameState gameState) {
        return gameState.isCardInOpenCards(card);
    }

    public boolean isMovementValid(Card clickedCard) {
        if(!clickedCard.getWon()) {
            if (sameCardClickedTwice(clickedCard, gameState)) {
                System.out.println("same card!");
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
