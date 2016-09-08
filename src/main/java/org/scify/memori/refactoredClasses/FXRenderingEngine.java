package org.scify.memori.refactoredClasses;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.scify.memori.*;
import org.scify.memori.interfaces.refactored.*;
import org.scify.memori.interfaces.refactored.Player;
import org.scify.memori.interfaces.refactored.RenderingEngine;
import org.scify.memori.interfaces.refactored.UI;
import org.scify.memori.interfaces.refactored.UserAction;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;

public class FXRenderingEngine implements RenderingEngine<MemoriGameState>, UI, EventHandler<KeyEvent> {

    private static Stage mStage = new Stage();
    private GridPane mGridPane;
    private double mWidth = Screen.getPrimary().getBounds().getWidth();
    private double mHeight = Screen.getPrimary().getBounds().getHeight();
    private boolean firstDraw = true;
    Scene gameScene;
    protected Parent root;

    private int columnIndex = 0;
    private int rowIndex = 0;

    public FXRenderingEngine(Stage currentStage) {
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
            mStage = currentStage; // Initialize the stage variable
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        gameScene = new Scene(root, mWidth, mHeight);
    }

    /**
     * List of actions captured by the user interaction. User in the Player-derived methods.
     */
    List<UserAction> pendingUserActions = Collections.synchronizedList(new ArrayList<>());



    @Override
    public void drawGameState(MemoriGameState currentState) {
        if (firstDraw) {

            Platform.runLater(() -> {
                try {
                    setUpFXComponents();
                    initFXComponents(currentState);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }});
            firstDraw = false;
        }
        else {
            Platform.runLater(() -> { updateFXComponents(currentState); });
        }
    }

    private long lLastUpdate = -1L;
    protected void updateFXComponents(MemoriGameState currentState) {
        long lNewTime = new Date().getTime();
        if (lNewTime - lLastUpdate < 100L) {// If no less than 1/10 sec has passed
            Thread.yield(); // TODO: Use it or do nothing?
            return; // Do nothing
        }
        else {
            lLastUpdate = lNewTime;
            // DEBUG LINES
//            System.err.println("Updating components " +lLastUpdate);
            Queue<GameEvent> eventsQueue = currentState.getEventQueue();

            // TODO: Iterate over all events
            // TODO: ...and remove handled
            Node node;
            if(!eventsQueue.isEmpty()) {
                if(eventsQueue.element().type.equals("movement")) {
                    Point2D param = (Point2D) eventsQueue.element().parameters;
                    System.out.println("point: " + param.getY() + "," + param.getX());
                    node = getNodeByRowColumnIndex((int) param.getX(), (int) param.getY(), mGridPane);
                    //node.requestFocus();
                    node.getStyleClass().addAll("focusedCard");
                    Button btn = (Button) node;
                    System.out.println(btn.getId());
                } else {
                    //TODO handle other game events
                }
                currentState.resetEventsQueue(); // Clear all events
            }
        }
    }

    private Node getNodeByRowColumnIndex(final int row,final int column,GridPane gridPane) {
        Node result = null;
        ObservableList<Node> childrens = gridPane.getChildren();
        for(Node node : childrens) {
            if(gridPane.getRowIndex(node) == row && gridPane.getColumnIndex(node) == column) {
                result = node;
                break;
            }
        }
        return result;
    }

    protected void initFXComponents(MemoriGameState currentState) {
        MemoriGameState memoriGS = currentState;
        MemoriTerrain terrain = (MemoriTerrain) memoriGS.getTerrain();

        Map<Point2D, Tile> initialTiles = terrain.getTiles();
        Iterator it = initialTiles.entrySet().iterator();

        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Point2D point = (Point2D) pair.getKey();
            Card card = (Card) pair.getValue();
            Platform.runLater(()-> {
                mGridPane.add(card.getButton(), (int) point.getX(), (int) point.getY());
                card.getButton().setOnKeyPressed(this);
            });

            //it.remove(); // avoids a ConcurrentModificationException
        }

        Platform.runLater(()-> { mGridPane.getChildren().get(0).requestFocus(); });
    }

    public void setUpFXComponents() throws IOException {

        System.out.println("setUpFXComponents");
        mGridPane = ((GridPane) root);
        mStage.setTitle("Memor-i");
        gameScene.getStylesheets().add("css/style.css");
        // OBSOLETE
        // mStage.setScene(gameScene);
        gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    mStage.close();
                    //TODO: open previous screen again
                    break;
            }
        });
    }

    @Override
    public UserAction getNextUserAction(Player pCurrentPlayer) {
        UserAction toReturn = null;
        if(!pendingUserActions.isEmpty()) {
            toReturn = pendingUserActions.get(0);
            pendingUserActions.remove(0);
        }
        return toReturn;
    }

    @Override
    public void handle(KeyEvent event) {
        // DEBUG LINES
        System.err.println("Key press!" +new Date().getTime());

        UserAction userAction = null;

        if (event.getCode() == SPACE) {
            userAction = new UserAction("flip", rowIndex, columnIndex);
        } else if(isMovementAction(event)) {
            if(movementValid(event)) {
                updateColumnIndex(event);
                updateRowIndex(event);
                userAction = new UserAction("movement", rowIndex, columnIndex);
            } else {
                userAction = new UserAction("invalidMovement", rowIndex, columnIndex);
            }
        } else if(event.getCode() == ENTER) {
            userAction = new UserAction("help", rowIndex, columnIndex);
        }
        pendingUserActions.add(0, userAction);
    }

    private boolean isMovementAction(KeyEvent evt) {
        return evt.getCode() == UP || evt.getCode() == DOWN || evt.getCode() == LEFT || evt.getCode() == RIGHT;
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

    public boolean movementValid(KeyEvent evt) {
        switch(evt.getCode()) {
            case LEFT:
                if(columnIndex == 0) {
                    return false;
                }
                break;
            case RIGHT:
                if(columnIndex == MainOptions.NUMBER_OF_COLUMNS - 1) {
                    return false;
                }
                break;
            case UP:
                if(rowIndex == 0) {
                    return false;
                }
                break;
            case DOWN:
                if(rowIndex == MainOptions.NUMBER_OF_ROWS - 1) {
                    return false;
                }
                break;
        }
        return true;
    }
}
