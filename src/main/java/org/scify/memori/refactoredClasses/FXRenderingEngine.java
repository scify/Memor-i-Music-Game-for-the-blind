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

    /**
     * An Audio Engine object, able to play sounds
     */
    private FXAudioEngine fxAudioEngine;
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
        fxAudioEngine = new FXAudioEngine();

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
        } else {
            lLastUpdate = lNewTime;
            // DEBUG LINES
//            System.err.println("Updating components " +lLastUpdate);
            List<GameEvent> eventsList = currentState.getEventQueue();
            ListIterator<GameEvent> listIterator = eventsList.listIterator();
            while (listIterator.hasNext()) {
                //System.out.println(listIterator.next());
                GameEvent currentGameEvent = listIterator.next();
                String eventType = currentGameEvent.type;
                Point2D coords;
                Card currCard;
                switch (eventType) {
                    case "movement":
                        coords = (Point2D) currentGameEvent.parameters;
                        focusOnTile((int) coords.getX(), (int) coords.getY());
                        movementSound((int) coords.getX(), (int) coords.getY());
                        System.out.println("now at: " + rowIndex + "," + columnIndex);
                        listIterator.remove();
                        break;
                    case "invalidMovement":
                        Platform.runLater(() -> {
                            fxAudioEngine.playInvalidMovementSound();
                        });
                        listIterator.remove();
                        break;
                    case "empty":
                        Platform.runLater(() -> {
                            fxAudioEngine.playEmptySound();
                        });
                        listIterator.remove();
                        break;
                    case "cardSound":
                        coords = (Point2D) currentGameEvent.parameters;
                        currCard = (Card) ((MemoriTerrain) (currentState.getTerrain())).getTileByRowAndColumn((int) coords.getY(), (int) coords.getX());
                        Platform.runLater(() -> {
                            fxAudioEngine.playCardSound(currCard.getSound(), currentGameEvent.blocking);
                        });
                        listIterator.remove();
                        break;
                    case "flip":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) ((MemoriTerrain) (currentState.getTerrain())).getTileByRowAndColumn((int) coords.getY(), (int) coords.getX());
                            Platform.runLater(() -> {
                                currCard.flipUI();
                            });
                            listIterator.remove();
                        }
                        break;
                    case "flipBack":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) ((MemoriTerrain) (currentState.getTerrain())).getTileByRowAndColumn((int) coords.getY(), (int) coords.getX());
                            Platform.runLater(() -> {
                                currCard.flipBackUI();
                            });
                            listIterator.remove();
                        }
                        break;
                    case "success":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            Platform.runLater(() -> {
                                fxAudioEngine.playSuccessSound();
                            });
                            listIterator.remove();
                        }
                        break;
                    case "failure":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            Platform.runLater(() -> {
                                fxAudioEngine.playFailureSound();
                            });
                            //TODO: play appropriate sound informing about new turn
                            listIterator.remove();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void playGameOver() {
        System.err.println("play game over");
        Platform.runLater(() -> {
            fxAudioEngine.playSuccessSound();
        });
    }



    /**
     * Given the coordinates, marks a Node as visited (green background) by applying a CSS class
     * @param rowIndex the Node x position
     * @param columnIndex the Node y position
     */
    private void focusOnTile(int rowIndex, int columnIndex) {
        System.out.println("point: " + rowIndex + "," + columnIndex);
        //get Node (in our case it's a button)
        Node node = getNodeByRowColumnIndex(rowIndex, columnIndex, mGridPane);
        //TODO: remove
        //node.requestFocus();
        //remove the focused class from every other Node
        ObservableList<Node> nodes = mGridPane.getChildren();
        for(Node nd: nodes) {
            nd.getStyleClass().remove("focusedCard");
        }
        //apply the CSS class
        node.getStyleClass().addAll("focusedCard");
        Button btn = (Button) node;
        //DEBUG print button id
        System.out.println(btn.getId());
    }

    /**
     * Computes the sound balance (left-right panning) and rate and plays the movement sound
     * @param rowIndex the Node x position
     * @param columnIndex the Node y position
     */
    private void movementSound(int rowIndex, int columnIndex) {
        System.err.println("movementSound");
        double soundBalance = map(columnIndex, 0.0, (double) MainOptions.NUMBER_OF_ROWS - 0.9, -1.0, 1.0);
        double rate = map(rowIndex, 0.0, (double) MainOptions.NUMBER_OF_COLUMNS - 0.9, 1.0, 1.5);
        fxAudioEngine.playMovementSound(soundBalance, rate);
    }

    private Node getNodeByRowColumnIndex(final int row,final int column, GridPane gridPane) {
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
        }

        Platform.runLater(()-> { //set first card as visited
            mGridPane.getChildren().get(0).getStyleClass().addAll("focusedCard"); });
    }

    public void setUpFXComponents() throws IOException {

        System.out.println("setUpFXComponents");
        mGridPane = ((GridPane) root);
        mStage.setTitle("Memor-i");
        gameScene.getStylesheets().add("css/style.css");
        // OBSOLETE
        // mStage.setScene(gameScene);

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

    //maps a value to a new
    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
