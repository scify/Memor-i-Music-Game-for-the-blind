
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
import java.util.concurrent.ConcurrentLinkedQueue;

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

    /**
     * Each game level has an introductory sound associated with it
     */
    private Map<Integer, String> introductorySounds = new HashMap<>();
    /**
     * Every time we play a game we follow the story line
     */
    private Map<Integer, String> storyLineSounds = new HashMap<>();

    /**
     * Every time a level ends, we should construct the end level Sound which consists of:
     * 1) starting sound 2) the time in which the player finished the level 3) an ending sound
     */
    private String[] endLevelStartingSounds = {"sound1.wav", "sound2.wav", "sound3.wav", "sound4.wav"};
    private String[] endLevelEndingSounds = {"sound1.wav", "sound2.wav", "sound3.wav", "sound4.wav"};

    public FXRenderingEngine() {
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));
            introductorySounds.put(1, "level1IntroSound.wav");
            introductorySounds.put(2, "level2IntroSound.wav");
            introductorySounds.put(3, "level3IntroSound.wav");
            introductorySounds.put(4, "level4IntroSound.wav");
            introductorySounds.put(5, "level5IntroSound.wav");
            introductorySounds.put(6, "level6IntroSound.wav");
            introductorySounds.put(7, "level7IntroSound.wav");
            introductorySounds.put(8, "level8IntroSound.wav");

            storyLineSounds.put(1, "storyLine1.wav");
            storyLineSounds.put(2, "storyLine2.wav");
            storyLineSounds.put(3, "storyLine3.wav");
            storyLineSounds.put(4, "storyLine4.wav");
            storyLineSounds.put(5, "storyLine5.wav");
            storyLineSounds.put(6, "storyLine6.wav");
            storyLineSounds.put(7, "storyLine7.wav");
            storyLineSounds.put(8, "storyLine8.wav");
            storyLineSounds.put(9, "storyLine9.wav");

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
            Platform.runLater(() -> {
                updateFXComponents(currentState);
            });
        }
    }

    public void setUpFXComponents() throws IOException {
        System.out.println("setUpFXComponents");
        gridPane = ((GridPane) root);
        gameScene.getStylesheets().add("css/style.css");
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

        //TODO: ask ggianna
        if(MainOptions.TUTORIAL_MODE) {
            System.err.println("TUTORIAL STARTS PLEASE CLICK ENTER");
            //TODO: Play tutorial introductory sound
            fxAudioEngine.pauseAndPlaySound("game_instructions/tutorial_intro.wav", false);
        } else {
            //TODO: Play game level introductory sound
            //play current storyline (game progress) sound
            fxAudioEngine.pauseAndPlaySound("storyline_audios/" + storyLineSounds.get(MainOptions.storyLineLevel), true);
            fxAudioEngine.pauseAndPlaySound("level_intro_sounds/" + introductorySounds.get(MainOptions.gameLevel), true);
        }
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

    private long lLastUpdate = -1L;
    protected void updateFXComponents(MemoriGameState currentState) {
        long lNewTime = new Date().getTime();
        if (lNewTime - lLastUpdate < 100L) {// If no less than 1/10 sec has passed
            Thread.yield(); // TODO: Use it or do nothing?
            return; // Do nothing
        } else {
            lLastUpdate = lNewTime;
            List<GameEvent> eventsList = Collections.synchronizedList(currentState.getEventQueue());
            final Iterator<GameEvent>[] listIterator = new Iterator[]{eventsList.iterator()};
            while (listIterator[0].hasNext()) {
                //System.out.println(listIterator.next());
                final GameEvent currentGameEvent = listIterator[0].next();
                String eventType = currentGameEvent.type;
                final Point2D[] coords = new Point2D[1];
                Card currCard;
                switch (eventType) {
                    case "movement":

                        coords[0] = (Point2D) currentGameEvent.parameters;
                        focusOnTile((int) coords[0].getX(), (int) coords[0].getY());
                        movementSound((int) coords[0].getX(), (int) coords[0].getY());
                        System.out.println("now at: " + coords[0].getX() + "," + coords[0].getY());
                        listIterator[0].remove();
                        break;
                    case "invalidMovement":
                        fxAudioEngine.playInvalidMovementSound();
                        listIterator[0].remove();

                        break;
                    case "empty":
                        fxAudioEngine.playEmptySound();
                        listIterator[0].remove();
                        break;
                    case "cardSound":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords[0] = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) ((MemoriTerrain) (currentState.getTerrain())).getTileByRowAndColumn((int) coords[0].getY(), (int) coords[0].getX());

                            fxAudioEngine.playCardSound(currCard.getSound(), currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "flip":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords[0] = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) ((MemoriTerrain) (currentState.getTerrain())).getTileByRowAndColumn((int) coords[0].getY(), (int) coords[0].getX());

                            currCard.flipUI();
                            listIterator[0].remove();
                        }
                        break;
                    case "flipBack":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords[0] = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) ((MemoriTerrain) (currentState.getTerrain())).getTileByRowAndColumn((int) coords[0].getY(), (int) coords[0].getX());

                            currCard.flipBackUI();
                            listIterator[0].remove();
                        }
                        break;
                    case "success":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSuccessSound();
                            listIterator[0].remove();
                        }
                        break;
                    case "NUMERIC":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            int number = (int)currentGameEvent.parameters;
                            fxAudioEngine.playNumSound(number);
                            listIterator[0].remove();
                        }
                        break;
                    case "LEVEL_SUCCESS_STEP_1":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            int idx = new Random().nextInt(endLevelStartingSounds.length);
                            String randomSound = (endLevelStartingSounds[idx]);
                            fxAudioEngine.pauseAndPlaySound("end_level_starting_sounds/" + randomSound, currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "LEVEL_SUCCESS_STEP_2":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            int idx = new Random().nextInt(endLevelStartingSounds.length);
                            String randomSound = (endLevelEndingSounds[idx]);
                            fxAudioEngine.pauseAndPlaySound("end_level_ending_sounds/" + randomSound, currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "failure":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playFailureSound();
                            listIterator[0].remove();
                        }
                        break;
                    case "TUTORIAL_0_UI":
                        //TODO: These sound effects should be combined into 1
                        fxAudioEngine.pauseAndPlaySound("game_instructions/count_on_you.wav", currentGameEvent.blocking);
                        fxAudioEngine.playSound("game_instructions/wraia_ftasame.wav", currentGameEvent.blocking);
                        fxAudioEngine.playSound("game_instructions/please_press_right.wav", currentGameEvent.blocking);
                        listIterator[0].remove();

                        break;
                    case "GO_RIGHT_AGAIN":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            System.err.println("TUTORIAL_0_UI PLEASE CLICK RIGHT");
                            fxAudioEngine.playSound("game_instructions/please_press_right.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "TUTORIAL_2_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            //TODO: These sound effects should be combined into 1
                            fxAudioEngine.pauseAndPlaySound("game_instructions/please_press_down.wav", currentGameEvent.blocking);
                            fxAudioEngine.pauseAndPlaySound("game_instructions/when_ready_press_continue.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "DOORS_EXPLANATION_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            //TODO: These sound effects should be combined into 1
                            fxAudioEngine.playSound("game_instructions/doors_explanation.wav", currentGameEvent.blocking);
                            fxAudioEngine.playSound("game_instructions/press_flip.wav", currentGameEvent.blocking);
                            listIterator[0].remove();

                        }
                        break;
                    case "FLIP_EXPLANATION_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_instructions/flip_explanation.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "TUTORIAL_INVALID_MOVEMENT_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_instructions/tutorial_invalid_movement.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "NOT_RIGHT_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            System.err.println("PLEASE CLICK RIGHT!");
                            fxAudioEngine.playSound("game_instructions/not_right.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "NOT_LEFT_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            System.err.println("TUTORIAL_1_UI PLEASE CLICK LEFT");
                            fxAudioEngine.playSound("game_instructions/not_left.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "TUTORIAL_WRONG_PAIR_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_instructions/wrong_pair.wav", currentGameEvent.blocking);
                            listIterator[0].remove();
                        }
                        break;
                    case "TUTORIAL_DOORS_CLOSED_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_instructions/doors_closing_explanation.wav", currentGameEvent.blocking);
                            listIterator[0].remove();

                        }
                        break;
                    case "TUTORIAL_CORRECT_PAIR_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_instructions/correct_pair_explanation.wav", currentGameEvent.blocking);
                            listIterator[0].remove();

                        }
                        break;
                    case "DOORS_CLOSED":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_effects/door_shutting.wav", currentGameEvent.blocking);
                            listIterator[0].remove();

                        }
                        break;
                    case "TUTORIAL_END_GAME_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_instructions/tutorial_end_game.wav", currentGameEvent.blocking);
                            listIterator[0].remove();

                        }
                        break;
                    case "DOOR_OPEN":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("game_effects/open_door.wav", currentGameEvent.blocking);
                            try {
                                listIterator[0].remove();
                            } catch (Exception e) {
                                listIterator[0] = eventsList.iterator();
                            }
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

    /**
     * Handles the UI events (button clicks) and populates the user actions list
     * @param event the event emitted from Ui
     */
    @Override
    public void handle(KeyEvent event) {

        // DEBUG LINES
        System.err.println("Key press! " + new Date().getTime());

        UserAction userAction = null;
        //Handle different kinds of UI (keyboard) events
        if (event.getCode() == SPACE) {
            userAction = new UserAction("flip", event);
        } else if(isMovementAction(event)) {
            userAction = new UserAction("movement", event);
        } else if(event.getCode() == ENTER) {
            //userAction = new UserAction("help", event);
            userAction = new UserAction("enter", event);
        } else if(event.getCode() == F1) {
            userAction = new UserAction("quit", event);
        }
//        else if(event.getCode() == F2) {
//            userAction = new UserAction("nextLevel", event);
//        }
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
