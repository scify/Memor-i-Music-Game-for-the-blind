package org.scify.memori.interfaces.refactored;

import java.awt.geom.Point2D;

/**
 * Created by pisaris on 5/9/2016.
 */
public class UserAction {
    String actionType;
    Point2D coords;

    public UserAction(String sType, int x, int y) {
        actionType = sType;
        coords = new Point2D.Double(x,y);
    }

    public Point2D getCoords() {
        return coords;
    }

    public String getActionType() {
        return actionType;
    }
}
