package org.scify.windmusicgame.screens;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Sponsor page controller Class.
 * Created by pisaris on 17/10/2016.
 */
public class SponsorScreenController {
    public void openLink(MouseEvent keyEvent) {
        try {
            new ProcessBuilder("x-www-browser", "https://www.wind.gr").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
