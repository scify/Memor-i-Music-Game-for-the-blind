package org.scify.windmusicgame.rules;

import javafx.scene.input.KeyCode;
import org.scify.windmusicgame.MemoriGameState;
import org.scify.windmusicgame.games_options.GameWithLevelsOptions;
import org.scify.windmusicgame.interfaces.GameEvent;
import org.scify.windmusicgame.interfaces.GameOptions;
import org.scify.windmusicgame.interfaces.GameState;
import org.scify.windmusicgame.interfaces.UserAction;

import java.util.Date;

/**
 * Class that contains Tutorial rules.
 */
public class TutorialRules extends MemoriRules {

    public TutorialRules() {
        super();
    }

    public GameState getInitialState(GameOptions gameOptions) {
        // TODO: based on game options class name, identify which game is currently playing
        // and update a private string variable with the corresponding tutorial dir
        GameWithLevelsOptions gameWithLevelsOptions = (GameWithLevelsOptions) gameOptions;
        System.err.println(gameWithLevelsOptions.getClass());
        return super.getInitialState(gameOptions);
    }

    public GameState getNextState(GameState gsCurrent, UserAction uaAction) {

        gsCurrent = super.getNextState(gsCurrent, uaAction);
        // handle the tutorial game events
        if(uaAction != null)
            tutorialRulesSet((MemoriGameState) gsCurrent, uaAction);
        return gsCurrent;
    }

    /**
     * When in tutorial mode, handles the tutorial game events
     * @param gsCurrentState the current game state
     * @param uaAction the user action object
     */
    protected void tutorialRulesSet(MemoriGameState gsCurrentState, UserAction uaAction) {

        // if tutorial_0 event does not exist
        //If user clicked space
        // add tutorial_0 event to queue
        // add tutorial_0 UI event to queue
        // else if tutorial_0 event exists
        //if tutorial_1 event does not exist
        //if user clicked RIGHT
        //add tutorial_1 event to queue
        //add tutorial_1 UI event to queue
        //else  if user did not click RIGHT
        //add UI event indicating that the user should click RIGHT
        //else if tutorial_1 event exists
        //if tutorial_2 event does not exist
        //if user clicked LEFT
        //add tutorial_2 event to queue
        //add tutorial_2 UI event to queue
        //else if user did not click LEFT
        //add UI event indicating that the user should click RIGHT
        //else if tutorial_2 event exists
        //if tutorial_3 event does not exist
        //if user clicked FLIP
        //add tutorial_3 event to queue
        //add tutorial_3 UI event to queue
        //if tutorial_3 event exists
        // if tutorial_0 event does not exist
        if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_0")) {
            //If user clicked space
            if (uaAction.getActionType().equals("flip")) {
                // add tutorial_0 event to queue
                gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_0"));
                // add tutorial_0 UI event to queue
                gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_0_UI", null, 0, true));
            }
            // else if tutorial_0 event exists
        } else {
            //if tutorial_1 event does not exist
            if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_1_STEP_1")) {
                //if user clicked RIGHT
                if (uaAction.getDirection() == KeyCode.RIGHT) {
                    //add tutorial_1 event to queue
                    gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_1_STEP_1"));
                    //add tutorial_1 UI event to queue
                    gsCurrentState.getEventQueue().add(new GameEvent("GO_RIGHT_AGAIN", null, new Date().getTime() + 200, false));
                } //else  if user did not click RIGHT
                else {
                    gsCurrentState.getEventQueue().add(new GameEvent("NOT_RIGHT_UI", null, new Date().getTime() + 200, true));
                }
                //else if tutorial_1 event exists
            } else {
                if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_1_STEP_2")) {
                    //if user clicked RIGHT
                    if (uaAction.getDirection() == KeyCode.RIGHT) {
                        //add tutorial_1 event to queue
                        gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_1_STEP_2"));
                    } //else  if user did not click RIGHT
                    else {
                        gsCurrentState.getEventQueue().add(new GameEvent("NOT_RIGHT_UI", null, new Date().getTime() + 200, true));
                    }
                    //else if tutorial_1 event exists
                } else {
                    //if tutorial_2 event does not exist
                    if(eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_INVALID_MOVEMENT")) {
                        // if the invalid movement event was handled by the rendering engine
                        if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_INVALID_MOVEMENT_UI")) {
                            if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "TUTORIAL_2")) {
                                //if user clicked LEFT
                                if (uaAction.getDirection() == KeyCode.LEFT) {
                                    // add tutorial_2 UI event to queue
                                    gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_2_UI", null, new Date().getTime() + 500, true));
                                    gsCurrentState.getEventQueue().add(new GameEvent("TUTORIAL_2"));

                                } else {
                                    //add UI event indicating that the user should click LEFT
                                    gsCurrentState.getEventQueue().add(new GameEvent("NOT_LEFT_UI", null, new Date().getTime() + 200, true));
                                }
                            } else {
                                //if tutorial_3 event does not exist
                                if(!eventsQueueContainsEvent(gsCurrentState.getEventQueue(), "DOORS_EXPLANATION")) {
                                    //if user clicked ENTER
                                    if (uaAction.getActionType().equals("enter")) {
                                        //add tutorial_3 event to queue
                                        gsCurrentState.getEventQueue().add(new GameEvent("DOORS_EXPLANATION"));
                                        // add tutorial_3 UI event to queue
                                        gsCurrentState.getEventQueue().add(new GameEvent("DOORS_EXPLANATION_UI", null, new Date().getTime() + 200, true));
                                    }
                                }
                            }
                        }
                    }
                }

            }
        }
    }
}
