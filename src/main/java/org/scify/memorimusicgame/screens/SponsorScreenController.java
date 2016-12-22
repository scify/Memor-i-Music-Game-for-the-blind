package org.scify.memorimusicgame.screens;

import javafx.scene.input.MouseEvent;

import java.io.IOException;

/**
 * Sponsor page controller Class.
 * Created by pisaris on 17/10/2016.
 */
public class SponsorScreenController {
    /**
     * Opens a link to the sponsor page
     * @param event the mouse event
     */
    public void openSponsorLink(MouseEvent event) {
        try {
            new ProcessBuilder("x-www-browser", "https://www.wind.gr").start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
