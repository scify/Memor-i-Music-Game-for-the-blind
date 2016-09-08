package org.scify.memori;

import org.scify.memori.interfaces.Rules;

import java.util.List;

public class UserActions {

    public void assignCardClickHandlers(List<Card> shuffledCards, Rules mRules, GameState gsCurrentState) {
        for(Card currCard: shuffledCards) {
            currCard.getButton().setOnKeyPressed(new CardClickHandler(mRules, gsCurrentState));
        }
    }
}
