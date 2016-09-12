package org.scify.memori.interfaces;

/**
 * Created by pisaris on 5/9/2016.
 */
public abstract class Player {

    protected int iScore;
    protected String sName;

    public int getScore() {
        return iScore;
    }

    public void setScore(int iNewScore) {
        iScore = iNewScore;
    }

    public String getName() {
        return sName;
    }

//    public Set<UserAction> getPossibleUserActions()
}
