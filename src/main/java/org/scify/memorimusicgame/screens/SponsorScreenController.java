package org.scify.memorimusicgame.screens;

import javafx.scene.input.MouseEvent;
import org.scify.memorimusicgame.helper.MemoriConfiguration;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

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
        MemoriConfiguration configuration = new MemoriConfiguration();
        ResourceBundle labels = ResourceBundle.getBundle("languages.strings", new Locale(configuration.getProjectProperty("APP_LANG"), configuration.getProjectProperty("APP_LANG_LOCALE_CAPITAL")));
        String sponsorLink = labels.getString("sponsor_link");
        try {
            new ProcessBuilder("x-www-browser", sponsorLink).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
