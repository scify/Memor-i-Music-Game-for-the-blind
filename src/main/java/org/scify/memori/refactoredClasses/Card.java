package org.scify.memori.refactoredClasses;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import org.scify.memori.MainOptions;
import org.scify.memori.interfaces.refactored.Tile;

public class Card implements Tile{
    private Button button;
    private String tileType;
    private String imgName;
    private boolean isFlipped;
    private boolean isWon;
    BackgroundImage image;
    private String sound;

    public Button getButton() {
        return button;
    }

    public boolean getWon() {
        return isWon;
    }

    public void winCard() {
        isWon = true;
    }

    public void setCardNotWon() {
        isWon = false;
    }

    public boolean getFlipped() {
        return isFlipped;
    }

    public Card(String id, String img, String soundFile) {
        imgName = img;
        button = new Button();
        image = new BackgroundImage( new Image( getClass().getResource("/img/" + imgName).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        sound = soundFile;
        button.setId(id);
        button.setPrefHeight(MainOptions.mHeight/MainOptions.NUMBER_OF_ROWS);
        button.setPrefWidth(MainOptions.mWidth/MainOptions.NUMBER_OF_COLUMNS);
        button.getStyleClass().addAll("cardButton", "closedCard");
        
        tileType = id;
        setCardNotWon();
        isFlipped = false;
//        String imgFile = "/img/" + imgName;
//        button.setStyle("-fx-background-image: url(" + imgFile +")");
    }

    @Override
    public String getTileType() {
        return tileType;
    }


    @Override
    public void flip() {
        String imgFile = "/img/" + imgName;
//        if(isFlipped) {
//            imgFile = "/img/questionmark.png";
//        }
        isFlipped = !isFlipped;
        button.setStyle("-fx-background-image: url(" + imgFile +")");
    }

    public String getSound() {
        return sound;
    }

}
