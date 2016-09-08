package org.scify.memori;

import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;

public class Card {
    private Button mBtn;
    private String mId;
    private String imgName;
    private int xPos;
    private int yPos;
    private boolean isFlipped;
    private boolean isWon;
    BackgroundImage image;
    private String sound;

    public void setxPos(int x) {
        xPos = x;
    }

    public void setyPos(int y) {
        yPos = y;
    }

    public int getxPos() {
        return xPos;
    }

    public int getyPos() {
        return yPos;
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

    public void flipCard() {
        String imgFile = "/img/" + imgName;
        if(isFlipped) {
            imgFile = "/img/questionmark.png";
        }
        isFlipped = !isFlipped;
        mBtn.setStyle("-fx-background-image: url(" + imgFile +")");

    }

    public Card(String id, String img, String soundFile, PairDiscoveryRules rules) {
        imgName = img;
        mBtn = new Button();
        image = new BackgroundImage( new Image( getClass().getResource("/img/" + imgName).toExternalForm()), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT);
        sound = soundFile;
        mBtn.setId(id);
        mBtn.setPrefHeight(MainOptions.mHeight/rules.getNumberOfRows());
        mBtn.setPrefWidth(MainOptions.mWidth/rules.getNumberOfColumns());
        mBtn.getStyleClass().addAll("cardButton", "closedCard");
        
        mId = id;
        setCardNotWon();
        isFlipped = false;
    }

    public Button getButton() {
        return mBtn;
    }

    public String getmId() {
        return mId;
    }

    public String getSound() {
        return sound;
    }
}
