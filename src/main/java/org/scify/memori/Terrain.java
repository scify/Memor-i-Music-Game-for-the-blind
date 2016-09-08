package org.scify.memori;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class Terrain {

    private static Stage mStage = new Stage();
    private Stage mPrevStage;
    private GridPane mGridPane;
    private double mWidth = Screen.getPrimary().getBounds().getWidth();
    private double mHeight = Screen.getPrimary().getBounds().getHeight();
    Scene gameScene;

    public Terrain(Stage primaryStage, Stage prevStage) {
        mStage = primaryStage;
        mPrevStage = prevStage;
        MainOptions.mHeight = mHeight;
        MainOptions.mWidth = mWidth;
    }

    public void createTerrain() throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));

        mGridPane = ((GridPane) root);
        mStage.setTitle("Memor-i");

        gameScene = new Scene(root, mWidth, mHeight);
        //mStage.setFullScreen(true);
        gameScene.getStylesheets().add("css/style.css");
        //mGridPane.setPrefWidth(500);
        mStage.setScene(gameScene);
        gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    mStage.close();
                    mPrevStage.show();
                    mPrevStage.requestFocus();
                    //mPrevStage.setFullScreen(true);
                    break;
            }
        });
    }



    public void addCardToGrid(Card card, int xPos, int yPos) {
        mGridPane.add(card.getButton(), xPos, yPos);
    }

    public int getIndexOfCard(Button btn) {
        System.out.println("Row: "+ GridPane.getRowIndex(btn));
        System.out.println("Column: "+ GridPane.getColumnIndex(btn));
        return GridPane.getColumnIndex(btn);
    }

    public void showStage() {
        mStage.show();
    }

    public void closeStage() {
        mStage.close();
    }

    public double getmWidth() {
        return mWidth;
    }

    public double getmHeight() {
        return mHeight;
    }
}
