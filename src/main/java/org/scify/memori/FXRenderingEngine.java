
/**
 * Copyright 2016 SciFY.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scify.memori;

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
import org.scify.memori.interfaces.*;

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

    /**
     * gridpane holds all cards
     */
    private GridPane gridPane;
    /**
     * computing the current screen height and width
     */
    private double mWidth = Screen.getPrimary().getBounds().getWidth();
    private double mHeight = Screen.getPrimary().getBounds().getHeight();
    /**
     * first draw defines whether the rendering engine will initialize or update the UI components
     */
    private boolean firstDraw = true;
    /**
     * current game scene
     */
    Scene gameScene;
    /**
     * JavFX component to bind the scene with the .fxml and .css file
     */
    protected Parent root;


    public FXRenderingEngine() {
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        //initialize the audio engine object
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
            //initialize UI components=
                try {
                    setUpFXComponents();
                    initFXComponents(currentState);
                }
                catch (IOException e) {
                    e.printStackTrace();
                }
            firstDraw = false;
        }
        else {
            //update UI components
            updateFXComponents(currentState);
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
                        System.out.println("now at: " + coords.getX() + "," + coords.getY());
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
        Node node = getNodeByRowColumnIndex(rowIndex, columnIndex, gridPane);
        //remove the focused class from every other Node
        ObservableList<Node> nodes = gridPane.getChildren();
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
        double soundBalance = map(columnIndex, 0.0, (double) MainOptions.NUMBER_OF_COLUMNS, -1.0, 2.0);
        double rate = map(rowIndex, 0.0, (double) MainOptions.NUMBER_OF_ROWS, 1.5, 1.0);
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
        System.out.println("initFXComponents");
        MemoriGameState memoriGS = currentState;
        MemoriTerrain terrain = (MemoriTerrain) memoriGS.getTerrain();
        //Load the tiles list from the Terrain
        Map<Point2D, Tile> initialTiles = terrain.getTiles();
        Iterator it = initialTiles.entrySet().iterator();
        //Iterate through the tiles list to add them to the Layour object
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Point2D point = (Point2D) pair.getKey();
            Card card = (Card) pair.getValue();
            //add the card to layout when the Thread deems appropriate
            Platform.runLater(()-> {
                gridPane.add(card.getButton(), (int) point.getX(), (int) point.getY());
            });
            //Set up the event handler for the current card
            card.getButton().setOnKeyPressed(this);
        }

        Platform.runLater(()-> { //set first card as focused
            gridPane.getChildren().get(0).getStyleClass().addAll("focusedCard"); });
    }

    public void setUpFXComponents() throws IOException {
        System.out.println("setUpFXComponents");
        gridPane = ((GridPane) root);
        gameScene.getStylesheets().add("css/style.css");
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

    /**
     * Handles the UI events (button clicks) and populates the user actions list
     * @param event the event emitted from Ui
     */
    @Override
    public void handle(KeyEvent event) {
        // DEBUG LINES
        System.err.println("Key press!" +new Date().getTime());

        UserAction userAction = null;
        //Handle different kinds of UI (keyboard) events
        if (event.getCode() == SPACE) {
            userAction = new UserAction("flip", event);
        } else if(isMovementAction(event)) {
            userAction = new UserAction("movement", event);
        } else if(event.getCode() == ENTER) {
            userAction = new UserAction("help", event);
        } else if(event.getCode() == F1) {
            userAction = new UserAction("quit", event);
        } else if(event.getCode() == F2) {
            userAction = new UserAction("nextLevel", event);
        }
        pendingUserActions.add(0, userAction);
    }

    /**
     * Dtermines whether the user action was a movement game action
     * @param evt
     * @return true if the evt was a movement action
     */
    private boolean isMovementAction(KeyEvent evt) {
        return evt.getCode() == UP || evt.getCode() == DOWN || evt.getCode() == LEFT || evt.getCode() == RIGHT;
    }

    //maps a value to a new set
    private double map(double x, double in_min, double in_max, double out_min, double out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }
}
