
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

package org.scify.windmusicgame;

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
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.interfaces.*;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.util.*;

import static javafx.scene.input.KeyCode.*;
import static javafx.scene.input.KeyCode.LEFT;
import static javafx.scene.input.KeyCode.RIGHT;

/**
 * The Rendering Engine is responsible for handling Game Events (drawing, playing audios etc) as well as implementing the UI events listener
 * (keyboard events in this case)
 */
public class FXRenderingEngine implements RenderingEngine<MemoriGameState>, UI, EventHandler<KeyEvent> {

    /**
     * The rendering engine processes the game events, one at a time.
     * The currently processed {@link GameEvent} may block any UI input.
     */
    GameEvent currentGameEvent;
    /**
     * A {@link AudioEngine} object, able to play sounds
     */
    private FXAudioEngine fxAudioEngine;

    /**
     * The {@link GridPane} holds all cards
     */
    private GridPane gridPane;
    /**
     * computes the current screen height and width
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
    private List<String> endLevelStartingSounds = new ArrayList<>();
    private List<String> endLevelEndingSounds = new ArrayList<>();

    /**
     * Fun factor sounds occur every 3 levels
     */
    protected List<String> funFactorSounds = new ArrayList<>();
    private GameWithLevelsOptions gameWithLevelsOptions;

    public FXRenderingEngine(GameOptions gameOptions) {
        this.gameWithLevelsOptions = (GameWithLevelsOptions) gameOptions;
        try {
            root = FXMLLoader.load(getClass().getResource("/fxml/game.fxml"));

            endLevelStartingSounds = this.gameWithLevelsOptions.getEndLevelStartingSounds();

            endLevelEndingSounds = this.gameWithLevelsOptions.getEndLevelEndingSounds();
            funFactorSounds = this.gameWithLevelsOptions.getFunFactorSounds();

            List<MemoriGameLevel> allLevels = this.gameWithLevelsOptions.getGameLevels();

            for(MemoriGameLevel gameLevel: allLevels) {
                this.introductorySounds.put(gameLevel.getLevelCode(), gameLevel.getIntroSound());
            }

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
     * Pauses every rendering function
     */
    @Override
    public void cancelCurrentRendering() {
        fxAudioEngine.pauseCurrentlyPlayingAudios();
    }

    private long lLastUpdate = -1L;

    protected void updateFXComponents(MemoriGameState currentState) {
        long lNewTime = new Date().getTime();
        if (lNewTime - lLastUpdate < 100L) {// If no less than 1/10 sec has passed
            Thread.yield();
            return; // Do nothing
        } else {
            lLastUpdate = lNewTime;
            List<GameEvent> eventsList = Collections.synchronizedList(currentState.getEventQueue());
            ListIterator<GameEvent> listIterator = eventsList.listIterator();
            while (listIterator.hasNext()) {
                //System.out.println(listIterator.next());
                currentGameEvent = listIterator.next();
                String eventType = currentGameEvent.type;
                Point2D coords;
                Card currCard;
                switch (eventType) {
                    case "movement":

                        coords = (Point2D) currentGameEvent.parameters;
                        movementSound((int) coords.getX(), (int) coords.getY());
                        Platform.runLater(() -> {
                            focusOnTile((int) coords.getX(), (int) coords.getY());
                            System.out.println("now at: " + coords.getX() + "," + coords.getY());
                        });
                        listIterator.remove();
                        break;
                    case "invalidMovement":
                        coords = (Point2D) currentGameEvent.parameters;
                        invalidMovementSound((int) coords.getX(), (int) coords.getY(), currentGameEvent.blocking);
                        listIterator.remove();

                        break;
                    case "EMPTY":
                        fxAudioEngine.playEmptySound();
                        listIterator.remove();
                        break;
                    case "cardSound":
                        if (new Date().getTime() > currentGameEvent.delay) {

                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) currentState.getTerrain().getTile((int) coords.getX(), (int) coords.getY());
                            String cardSound = currCard.getRandomSound();
                            if(cardSound != null)
                                fxAudioEngine.playCardSound(cardSound, currentGameEvent.blocking);
                            listIterator.remove();

                        }
                        break;
                    case "CARD_DESCRIPTION":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) currentState.getTerrain().getTile((int) coords.getX(), (int) coords.getY());
                            String cardDescriptionSound = currCard.getDescriptionSound();
                            if(cardDescriptionSound != null)
                                fxAudioEngine.pauseAndPlaySound(cardDescriptionSound, currentGameEvent.blocking);
                            listIterator.remove();

                        }
                        break;
                    case "CARD_NAME":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("instrument_names/" + currentGameEvent.parameters + ".mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "flip":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) currentState.getTerrain().getTile((int) coords.getX(), (int) coords.getY());
                            Platform.runLater(() -> {
                                currCard.flipUI(0);
                            });
                            listIterator.remove();
                        }
                        break;
                    case "flip_second":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) currentState.getTerrain().getTile((int) coords.getX(), (int) coords.getY());
                            Platform.runLater(() -> {
                                currCard.flipUI(1);
                            });
                            listIterator.remove();
                        }
                        break;
                    case "flipBack":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            coords = (Point2D) currentGameEvent.parameters;
                            currCard = (Card) currentState.getTerrain().getTile((int) coords.getX(), (int) coords.getY());
                            Platform.runLater(currCard::flipBackUI);
                            listIterator.remove();
                        }
                        break;
                    case "success":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSuccessSound();
                            listIterator.remove();
                        }
                        break;
                    case "NUMERIC":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            int number = (int)currentGameEvent.parameters;
                            fxAudioEngine.playNumSound(number);
                            listIterator.remove();
                        }
                        break;
                    case "LETTER":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            int number = (int)currentGameEvent.parameters;
                            fxAudioEngine.playLetterSound(number);
                            listIterator.remove();
                        }
                        break;
                    case "HELP_INSTRUCTIONS_UI":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound(gameWithLevelsOptions.getGameLevels().get(MainOptions.gameLevel - 1).getIntroHelperSound(), currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "HELP_EXPLANATION_ROW":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("game_instructions/help_explanation_row.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "HELP_EXPLANATION_COLUMN":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("game_instructions/help_explanation_column.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "LEVEL_SUCCESS_STEP_1":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            playRandomSoundFromPool(currentGameEvent, endLevelStartingSounds);
                            listIterator.remove();
                        }
                        break;
                    case "LEVEL_SUCCESS_STEP_2":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            playRandomSoundFromPool(currentGameEvent, endLevelEndingSounds);
                            listIterator.remove();
                        }

                        break;
                    case "FUN_FACTOR_UI":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            playRandomSoundFromPool(currentGameEvent, funFactorSounds);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_INTRO_UI":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "tutorial_intro_step_1.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "STORYLINE_AUDIO_UI":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            //fxAudioEngine.playSound("storyline_audios/" + storyLineSounds.get(MainOptions.storyLineLevel), currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "LEVEL_INTRO_AUDIO_UI":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound(introductorySounds.get(MainOptions.gameLevel), currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "GAME_END":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            System.err.println(this.gameWithLevelsOptions.getEndGameSound());
                            fxAudioEngine.pauseAndPlaySound(this.gameWithLevelsOptions.getEndGameSound(), currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "LEVEL_END_UNIVERSAL":
                        // if last level, play appropriate sound
                        if(gameWithLevelsOptions.getGameLevels().size() == MainOptions.gameLevel) {
                            // else if not last level, play universally ending sound
                            if (new Date().getTime() > currentGameEvent.delay) {
                                fxAudioEngine.pauseAndPlaySound("game_instructions/all_levels_ending.mp3", currentGameEvent.blocking);
                                listIterator.remove();
                            }
                            break;
                        } else {
                            // else if not last level, play universally ending sound
                            if (new Date().getTime() > currentGameEvent.delay) {
                                fxAudioEngine.pauseAndPlaySound("game_instructions/level_ending_universal.mp3", currentGameEvent.blocking);
                                listIterator.remove();
                            }
                            break;
                        }

                    case "MINUTE":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("miscellaneous/minute.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "MINUTES":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("miscellaneous/minutes.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "AND":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("miscellaneous/and.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "SECOND":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("miscellaneous/second.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "SECONDS":
                        //check if the event should happen after some time
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound("miscellaneous/seconds.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_0_UI":
                        fxAudioEngine.pauseAndPlaySound(gameWithLevelsOptions.getTutorialSoundBase() + "tutorial_intro_step_2.mp3", currentGameEvent.blocking);
                        listIterator.remove();
                        break;
                    case "GO_RIGHT_AGAIN":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "press_right_until_end.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_2_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound(gameWithLevelsOptions.getTutorialSoundBase() + "please_press_down.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "DOORS_EXPLANATION_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "doors_explanation.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "FLIP_EXPLANATION_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "flip_explanation.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_INVALID_MOVEMENT_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseAndPlaySound(gameWithLevelsOptions.getTutorialSoundBase() + "tutorial_invalid_movement.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "NOT_RIGHT_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            System.err.println("PLEASE CLICK RIGHT!");
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "not_right.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "NOT_LEFT_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            System.err.println("TUTORIAL_1_UI PLEASE CLICK LEFT");
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "not_left.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_WRONG_PAIR_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "wrong_pair.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_SUCCESS_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "tutorial_success.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_DOORS_CLOSED_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "doors_closing_explanation.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_CORRECT_PAIR_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "correct_pair_explanation.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "DOORS_SHUTTING":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("miscellaneous/doors_shutting.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "FAIL_SOUND":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound("miscellaneous/fail.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "STOP_AUDIOS":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.pauseCurrentlyPlayingAudios();
                            listIterator.remove();
                        }
                        break;
                    case "TUTORIAL_END_GAME_UI":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getTutorialSoundBase() + "tutorial_ending.mp3", currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    case "DOOR_OPEN":
                        if (new Date().getTime() > currentGameEvent.delay) {
                            fxAudioEngine.playSound(gameWithLevelsOptions.getCardsOpeningSound(), currentGameEvent.blocking);
                            listIterator.remove();
                        }
                        break;
                    default:
                        break;
                }
                currentGameEvent = null;
            }
        }
    }

    /**
     * When a level ends, play a success sound
     */
    @Override
    public void playGameOver() {
        System.err.println("play game over");
        Platform.runLater(() -> {
            fxAudioEngine.playSuccessSound();
        });
    }

    /**
     * Gets a random element from a pool with sound file names and passes it to the audio engine
     * @param currentGameEvent the current game event
     * @param soundPool the pool with sound file names
     */
    private void playRandomSoundFromPool(GameEvent currentGameEvent, List<String> soundPool) {
        int randInt = new Random().nextInt(soundPool.size());
        String randomSound = (soundPool.get(randInt));
        fxAudioEngine.pauseAndPlaySound(randomSound, currentGameEvent.blocking);
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

    /**
     * Computes the sound balance (left-right panning) and rate and plays the movement sound
     * @param rowIndex the Node x position
     * @param columnIndex the Node y position
     * @param isBlocking if the event should block the ui thread
     */
    private void invalidMovementSound(int rowIndex, int columnIndex, boolean isBlocking) {
        double soundBalance = map(columnIndex, 0.0, (double) MainOptions.NUMBER_OF_COLUMNS, -1.0, 2.0);
        double rate = map(rowIndex, 0.0, (double) MainOptions.NUMBER_OF_ROWS, 1.5, 1.0);
        fxAudioEngine.playInvalidMovementSound(soundBalance, isBlocking);
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
     * Handles the UI events (button clicks) and populates the {@link UserAction} list
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
            userAction = new UserAction("f1", event);
        } else if(event.getCode() == ESCAPE) {
            userAction = new UserAction("escape", event);
            event.consume();
        }

        //if there is a game event currently being processed
        if(currentGameEvent != null) {
            //if the currently processed event is blocking, the UI engine does not accept any user actions
            //if the currently processed event is not blocking, accept user actions
            if (!currentGameEvent.blocking)
                pendingUserActions.add(0, userAction);
        } else {
            //if there is no processed event, accept the user action
            pendingUserActions.add(0, userAction);
        }
    }


    /**
     * Determines whether the user action was a movement {@link GameEvent}.
     * @param evt the action event
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