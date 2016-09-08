package org.scify.memori.interfaces;

import org.scify.memori.Card;
import org.scify.memori.Game;
import org.scify.memori.GameState;

public interface Rules {
    boolean isLastMove(GameState gsCurrent);
    boolean isGameOver(GameState gsCurrent);
    boolean isMovementValid(Card clickedCard);
    GameState getInitialGameState();
    int getNumberOfRows();
    int getNumberOfColumns();
    int getNumberOfOpenCards();
    boolean anotherCardExists(Card card, GameState gs);
}
