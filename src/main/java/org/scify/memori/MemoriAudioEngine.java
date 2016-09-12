package org.scify.memori;

import org.scify.memori.interfaces.AudioEngine;

import java.io.IOException;

public class MemoriAudioEngine implements AudioEngine {

    @Override
    public void playSound(String sSoundType) {
        try {
            Runtime.getRuntime().exec("beep");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
