package org.scify.memori.memoripoc;

import org.scify.memori.interfaces.Terrain;

import java.awt.geom.Point2D;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by pisaris on 5/9/2016.
 */
public class FlipTerrain implements Terrain {
    Map<Point2D, String> mTiles;

    public FlipTerrain() {
        mTiles = new HashMap<>();

        for (int iX = 0; iX < getWidth(); iX++) {
            for (int iY = 0; iY < getWidth(); iY++) {
                mTiles.put(new Point2D.Double(iX, iY), "-");
            }
        }
    }

    @Override
    public int getWidth() {
        return 3;
    }

    @Override
    public int getHeight() {
        return 4;
    }

    @Override
    public String getTileState(int x, int y) {
        return mTiles.get(new Point2D.Double(x,y));
    }

    public void toggleTile(int x, int y) {
        if (mTiles.get(new Point2D.Double(x,y)).equals("-"))
            mTiles.put(new Point2D.Double(x,y),"X");
        else
            mTiles.put(new Point2D.Double(x,y),"-");
    }

    @Override
    public String toString() {
        return mTiles.toString();
    }
}
