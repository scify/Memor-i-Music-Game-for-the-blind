package org.scify.memori.memoripoc;

import org.scify.memori.interfaces.AudioEngine;

import java.io.IOException;

/**
 * Created by pisaris on 5/9/2016.
 */
public class FlipAudioEngine implements AudioEngine {

    @Override
    public void playSound(String sSoundType) {
        try {
            Runtime.getRuntime().exec("beep");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
