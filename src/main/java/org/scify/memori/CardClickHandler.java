package org.scify.memori;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import org.scify.memori.interfaces.Rules;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;

public class CardClickHandler implements EventHandler<KeyEvent> {
    private boolean wonRound = false;
    private Rules mRules;
    private GameState gsCurrentState;
    public CardClickHandler(Rules rules, GameState state) {
        mRules = rules;
        gsCurrentState = state;
    }

    public void handle(KeyEvent evt) {

        Card clickedCard = gsCurrentState.getCardByBtn((Button) evt.getSource());


        if (evt.getCode() == SPACE) {
            if(!clickedCard.getWon()) {
                AudioEngine.playCardSound(clickedCard.getSound());
            } else {
                AudioEngine.playEmptySound();
            }

            clickCard(clickedCard, gsCurrentState, mRules);
        } else if(isMovementAction(evt)) {

            if(gsCurrentState.movementValid(evt, mRules)) {
                gsCurrentState.updateColumnIndex(evt);
                gsCurrentState.updateRowIndex(evt);

                gsCurrentState.playMovementSound(mRules);
            } else {
                AudioEngine.playInvalidMovementSound();
            }
        } else if(evt.getCode() == ENTER) {
            gsCurrentState.playHelperSound();
        }

    }

    private boolean isMovementAction(KeyEvent evt) {
        return evt.getCode() == UP || evt.getCode() == DOWN || evt.getCode() == LEFT || evt.getCode() == RIGHT;
    }



    public void clickCard(Card clickedCard, GameState gsCurrentState, Rules rules) {

        if(rules.isMovementValid(clickedCard)) {
            clickedCard.flipCard();
            gsCurrentState.incrementMoves();

            System.out.println("ADDING CARD IN OPEN CARDS");

            if (rules.anotherCardExists(clickedCard, gsCurrentState)) {
                System.out.println("good!");
                wonRound = true;

                clickedCard.winCard();
                gsCurrentState.setAllOpenCardsWon();
            }
            gsCurrentState.addCardToOpenCards(clickedCard);

            if (rules.isLastMove(gsCurrentState)) {
                gsCurrentState.resetGameState(rules, wonRound);
            }

        } else {
            if(!rules.isLastMove(gsCurrentState) && !clickedCard.getWon()) {
                AudioEngine.playErrorSound();
            }
        }

        wonRound = false;

    }
}
