
/**
 * Copyright 2016 SciFY.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.scify.memorimusicgame.fx;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.scify.memorimusicgame.helper.MemoriConfiguration;
import org.scify.memorimusicgame.interfaces.AudioEngine;

import java.net.URL;
import java.util.ArrayList;

public class FXAudioEngine implements AudioEngine{

    private AudioClip audioClip;
    private MediaPlayer movementSoundPlayer;
    private Media movementSoundMedia;
    private String soundBasePath = "/audios/";
    private String movementSound = "miscellaneous/movement_sound.mp3";
    private String successSound = "miscellaneous/success.wav";
    private String invalidMovementSound = "miscellaneous/bump.mp3";
    private String emptySound = "miscellaneous/door-knock.wav";
    private String numBasePath = "/numbers/";
    private String letterBasePath = "/letters/";
    private ArrayList<AudioClip> playingAudios = new ArrayList<>();

    /**
     * the directory of the current language
     */
    private String langDirectory;
    /**
     * the directory of the default language
     */
    private String defaultLangDirectory;


    public FXAudioEngine() {
        MemoriConfiguration configuration = new MemoriConfiguration();
        // if the game loads for the first time, we need to set the default language
        this.defaultLangDirectory = configuration.getProjectProperty("APP_LANG_DEFAULT");
        this.langDirectory = configuration.getProjectProperty("APP_LANG");
    }

    /**
     * Pauses the currently playing audio, if there is one
     */
    private void pauseSound() {
        if(audioClip != null)
            audioClip.stop();
        if(movementSoundPlayer != null)
            movementSoundPlayer.stop();
    }

    /**
     * Plays a sound describing a certain movement on the UI layout
     * @param balance left/right panning value of the sound
     * @param rate indicates how fast the sound will be playing. Used to distinguish vertical movements
     */
    public void playMovementSound(double balance, double rate) {
        pauseSound();
        if(movementSoundMedia == null) {
            System.err.println("construct new movement sound player");
            movementSoundMedia = new Media(FXAudioEngine.class.getResource(getCorrectPathForFile(movementSound)).toExternalForm());
            movementSoundPlayer = new MediaPlayer(movementSoundMedia);
        }
        movementSoundPlayer.setBalance(balance);
        movementSoundPlayer.setRate(rate);
        movementSoundPlayer.setOnEndOfMedia(() -> movementSoundPlayer.stop());
        movementSoundPlayer.play();
    }

    /**
     * Plays a sound associated with a Card object
     * @param soundFile the file name (path) of the audio clip
     * @param isBlocking whether the player should block the calling Thread while the sound is playing
     */
    public void playCardSound(String soundFile, boolean isBlocking) {
        playSound(soundFile, isBlocking);
    }


    /**
     * Plays an appropriate sound associated with a successful Game Event
     */
    public void playSuccessSound() {
        pauseAndPlaySound(successSound, true);
    }

    /**
     * Plays an appropriate sound associated with an invalid movement
     * @param balance the sound balance (left , right)
     * @param isBlocking if the event should block the ui thread
     */
    public void playInvalidMovementSound(double balance, boolean isBlocking) {
        playBalancedSound(balance, invalidMovementSound, isBlocking);
    }


    /**
     * Plays an appropriate sound associated with an "empty" Game Event (if the user clicks on an already won Card)
     */
    public void playEmptySound() {
        playSound(emptySound);
    }

    @Override
    public void playSound(String soundFile) {
        playSound(soundFile, false);
    }

    /**
     * Plays a sound given a certain balance
     * @param balance the desired balance
     * @param soundFile the file name (path) of the audio clip
     */
    public void playBalancedSound(double balance, String soundFile, boolean isBlocking) {
        pauseCurrentlyPlayingAudios();
        audioClip = new AudioClip(FXAudioEngine.class.getResource(getCorrectPathForFile(soundFile)).toExternalForm());
        audioClip.play(1, balance, 1, balance, 1);
        playingAudios.add(audioClip);
        if(isBlocking)
            while (audioClip.isPlaying()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
    }

    /**
     * This function looks for the given file in the current language
     * If not found, loads the default language
     * If not found, then it is a language-independent file
     * @param soundFilePath the resource-relative path of the file
     * @return the correct file path
     */
    private String getCorrectPathForFile(String soundFilePath) {
        String soundPath;
        // default sound path is as if the file is language-dependent. Searching for current language
        soundPath = soundBasePath + this.langDirectory + soundFilePath;

        URL soundFile = FXAudioEngine.class.getResource(soundPath);
        if(soundFile == null) {
            // if no file exists, try to load default language

            soundPath = soundBasePath + this.defaultLangDirectory + "/" + soundFilePath;
            System.err.println("Loading default language for: " + soundPath);
        }
        return soundPath;
    }

    /**
     * Plays a sound given a sound file path
     * @param soundFilePath the file name (path) of the audio clip
     * @param isBlocking whether the player should block the calling {@link Thread} while the sound is playing
     */
    public void playSound(String soundFilePath, boolean isBlocking) {
        try {
            audioClip = new AudioClip(FXAudioEngine.class.getResource(getCorrectPathForFile(soundFilePath)).toExternalForm());
            audioClip.play();
        } catch (Exception e) {
            System.err.println("error loading sound for: " + soundFilePath);
            return;
        }
        playingAudios.add(audioClip);
        if (isBlocking) {
            // Wait until completion
            while (audioClip.isPlaying()) {
                try {
                    Thread.sleep(100L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * Pauses the currently playing sound and plays a new one
     * @param soundFile the file of the sound we want to play
     * @param isBlocking whether the sound should block the {@link Thread} while playing
     */
    public void pauseAndPlaySound(String soundFile, boolean isBlocking) {
        pauseCurrentlyPlayingAudios();
        playSound(soundFile, isBlocking);
    }

    /**
     * Plays the sound representation of a number.
     * @param number the given number
     */
    public void playNumSound(int number) {
        pauseCurrentlyPlayingAudios();
        playSound(numBasePath + String.valueOf(number) + ".mp3", true);
    }

    /**
     * PLay the sound representation of a letter.
     * @param number the number associated with the letter (e.g. 1 for A, 2 for B, etc. We do not care for capital letters or not).
     */
    public void playLetterSound(int number) {
        pauseCurrentlyPlayingAudios();
        playSound(letterBasePath + number + ".mp3", true);
    }

    /**
     * Pause any currently playing audios (every audio that is playing is stored in the playingAudios list).
     */
    public void pauseCurrentlyPlayingAudios() {
        for (AudioClip audio: playingAudios) {
            if(audio.isPlaying())
                audio.stop();
        }
    }

}
