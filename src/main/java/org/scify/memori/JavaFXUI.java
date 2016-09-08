package org.scify.memori;

import javafx.stage.Stage;
import org.scify.memori.interfaces.UI;

import java.io.IOException;
import java.util.List;

public class JavaFXUI implements UI{
    private Terrain mTerrain;

    public JavaFXUI() {

    }

    public void initUi(Stage stage, Stage prevStage) {
        mTerrain = new Terrain(stage, prevStage);

        try {
            mTerrain.createTerrain();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addCardsToTerrain(List<Card> shuffledCards, List<Card> unShuffledCards) {
        int pos = 0;
        for(Card currCard: shuffledCards) {
            mTerrain.addCardToGrid(currCard, unShuffledCards.get(pos).getxPos(), unShuffledCards.get(pos).getyPos());
            pos++;
        }
    }


    public void begin() {
        mTerrain.showStage();
    }

}
