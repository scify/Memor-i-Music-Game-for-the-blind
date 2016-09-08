package org.scify.memori.refactoredClasses;

import org.scify.memori.interfaces.refactored.AudioEngine;

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
