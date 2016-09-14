package org.scify.memori.interfaces;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

import java.awt.geom.Point2D;

public class UserAction {
    String actionType;

    public void setCoords(Point2D coords) {
        this.coords = coords;
    }

    Point2D coords;
    KeyEvent keyEvent;
    KeyCode direction;


    public KeyCode getDirection() {

        return direction;
    }

    public UserAction(String sType, KeyEvent evt) {
        actionType = sType;
        keyEvent = evt;
        direction = evt.getCode();
    }

    public KeyEvent getKeyEvent() {
        return keyEvent;
    }

    public Point2D getCoords() {
        return coords;
    }

    public String getActionType() {
        return actionType;
    }
}
