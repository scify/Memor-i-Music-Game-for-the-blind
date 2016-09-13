package org.scify.memori;

import javafx.scene.control.Button;
import org.scify.memori.MainOptions;
import org.scify.memori.interfaces.Tile;

public class Card implements Tile{
    private Button button;
    private String tileType;
    private String imgName;
    private boolean isFlipped;
    private boolean isWon;
    private String sound;

    public Button getButton() {
        return button;
    }
    @Override
    public boolean getWon() {
        return isWon;
    }

    @Override
    public void setWon() {
        isWon = true;
    }

    public void setCardNotWon() {
        isWon = false;
    }

    @Override
    public boolean getFlipped() {
        return isFlipped;
    }

    public Card(String id, String img, String soundFile) {
        imgName = img;
        button = new Button();
        //image = new BackgroundImage( new Image( getClass().getResource("/img/" + imgName).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        sound = soundFile;
        button.setId(id);
        button.setPrefHeight(MainOptions.mHeight/MainOptions.NUMBER_OF_ROWS - (MainOptions.mHeight/MainOptions.NUMBER_OF_ROWS * 0.05));
        button.setPrefWidth(MainOptions.mWidth/MainOptions.NUMBER_OF_COLUMNS);
        button.getStyleClass().addAll("cardButton", "closedCard");
        tileType = id;
        setCardNotWon();
        isFlipped = false;
    }

    @Override
    public String getTileType() {
        return tileType;
    }


    @Override
    public void flip() {
        isFlipped = !isFlipped;
    }

    /**
     * function to set the UI of the flipped card (change icons)
     */
    public void flipUI() {
        String imgFile = "/img/" + imgName;
        button.setStyle("-fx-background-image: url(" + imgFile +")");
    }

    public void flipBackUI () {
        String imgFile = "/img/door.jpg";
        button.setStyle("-fx-background-image: url(" + imgFile +")");
    }

    public String getSound() {
        return sound;
    }

}
