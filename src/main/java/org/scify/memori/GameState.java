package org.scify.memori;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import org.scify.memori.interfaces.Rules;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class GameState {
    private int moveCount = 0;
    //openedCards is a list that changes in each round. When a cars is flipped by the user, it goes into the list
    //when the round is over (either the user has managed to find a pattern or not) the openedCards list it then emptied
    private List<Card> openedCards = new ArrayList<Card>();
    //deckCards is a list containing all the cards in the matrix
    private List<Card> deckCards = new ArrayList<Card>();

    private int columnIndex = 0;
    private int rowIndex = 0;

    private HighScore mHighScore = new HighScore();
    TimeWatch watch = TimeWatch.start();

    public List<Card> getDeckCards() {
        return deckCards;
    }

    /**
     * shuffles the list with all the cards
     */
    public void shuffleDeck () {
        long seed = System.nanoTime();
        Collections.shuffle(deckCards, new Random(seed));
    }

    /**
     * when a user manages to find a card pattern, all the opened cards are marked as 'won'
     */
    public void setAllOpenCardsWon() {
        openedCards.forEach(Card::winCard);

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(500);
                AudioEngine.playSuccessSound();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            moveCount = 0;
        });

        thread.start();
    }

    public void addCardToDeck(Card card) {
        deckCards.add(card);
    }

    public Card getCardByBtn(Button cardBtn) {
        for(Card currCard: deckCards) {
            if(currCard.getButton() == cardBtn) {
                return currCard;
            }
        }
        return null;
    }

    public void addCardToOpenCards(Card card) {
        openedCards.add(card);
    }

    private void emptyCardsList() {
        for(Card currCard: openedCards) {
            if(!currCard.getWon()) {
                currCard.flipCard();
            }
        }
        openedCards = new ArrayList<>();
    }

    //flip all open cards back except for the won ones
    public void resetGameState(Rules rules, boolean wonRound) {

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(700);
                System.out.println("RESET GAME STATE");
                if(!wonRound)
                    AudioEngine.playErrorSound();
                emptyCardsList();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            moveCount = 0;
        });

        thread.start();
        if(rules.isGameOver(this)) {
            displayGameOver();
            mHighScore.updateHighScore(watch);
        }
    }

    public void displayGameOver() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Memor-i");
        alert.setHeaderText("You are awesome");
        alert.setContentText("You won!");

        alert.showAndWait();

    }

    public void incrementMoves() {
        moveCount++;
    }

    public int getMoveCount() {
        System.out.println("moveCount: " + moveCount);
        return moveCount;
    }

    public void playMovementSound(Rules rules) {
        double soundBalance = map(columnIndex, 0.0, (double) rules.getNumberOfColumns() - 0.9, -1.0, 1.0);
        double rate = map(rowIndex, 0.0, (double) rules.getNumberOfColumns() - 0.9, 1.0, 1.5);

        AudioEngine.playMovementSound(soundBalance, rate);
    }

    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    public void playHelperSound() {
        AudioEngine.playHelperSound(rowIndex, columnIndex);
    }


    public boolean isCardInOpenCards(Card card) {
        boolean found = false;
        for(Card currCard: openedCards) {
            if(currCard.getmId().equals(card.getmId())) {
                System.out.println("FOUND ANOTHER CARD");
                found = true;
                break;
            }
        }
        return found;
    }

    public boolean isSameCardInOpenCards(Card card) {
        boolean found = false;
        for(Card currCard: openedCards) {
            if(currCard == card) {
                found = true;
                break;
            }
        }
        return found;
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

    public boolean movementValid(KeyEvent evt, Rules mRules) {
        switch(evt.getCode()) {
            case LEFT:
                if(columnIndex == 0) {
                    return false;
                }
                break;
            case RIGHT:
                if(columnIndex == mRules.getNumberOfColumns() - 1) {
                    return false;
                }
                break;
            case UP:
                if(rowIndex == 0) {
                    return false;
                }
                break;
            case DOWN:
                if(rowIndex == mRules.getNumberOfRows() - 1) {
                    return false;
                }
                break;
        }
        return true;
    }
}
