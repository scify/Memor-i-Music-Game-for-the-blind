
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

package org.scify.memori;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.scify.memori.interfaces.AudioEngine;

import java.util.ArrayList;
import java.util.HashMap;

public class FXAudioEngine implements AudioEngine{

    private AudioClip audioClip;
    private MediaPlayer movementSoundPlayer;
    private Media movementSoundMedia;
    private String soundBasePath = "/audios/";
    private String movementSound = "beep.wav";
    private String successSound = "success.wav";
    private String invalidMovementSound = "invalid_movement.wav";
    private String failureSound = "error.wav";
    private String emptySound = "game_effects/door-knock.wav";
    private String numBasePath = "numbers/";
    private String letterBasePath = "letters/";
    private ArrayList<AudioClip> playingAudios = new ArrayList<>();

    private HashMap<Integer, String> rowHelpSounds = new HashMap<>();
    private HashMap<Integer, String> columnHelpSounds = new HashMap<>();

    public FXAudioEngine() {
        columnHelpSounds.put(0, "one.wav");
        columnHelpSounds.put(1, "two.wav");
        columnHelpSounds.put(2, "three.wav");
        columnHelpSounds.put(3, "four.wav");

        rowHelpSounds.put(0, "A.wav");
        rowHelpSounds.put(1, "B.wav");
        rowHelpSounds.put(2, "C.wav");
        rowHelpSounds.put(3, "D.wav");

    }

    public void playHelperSound(int rowIndex, int columnIndex) {
        System.out.println("row: " + rowHelpSounds.get(rowIndex));
        System.out.println("column: " + columnHelpSounds.get(columnIndex));
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
        pauseCurrentlyPlayingAudios();
        if(movementSoundMedia == null) {
            movementSoundMedia = new Media(FXAudioEngine.class.getResource(soundBasePath + movementSound).toExternalForm());
            movementSoundPlayer = new MediaPlayer(movementSoundMedia);
        }
        movementSoundPlayer.setBalance(balance);
        movementSoundPlayer.setRate(rate);
        movementSoundPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                movementSoundPlayer.stop();
            }
        });
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
        playSound(successSound, true);
    }

    /**
     * Plays an appropriate sound associated with an invalid movement
     */
    public void playInvalidMovementSound() {
        playSound(invalidMovementSound);
    }

    /**
     * Plays an appropriate sound associated with a failure Game Event
     */
    public void playFailureSound() {
        pauseAndPlaySound(failureSound, false);
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
    public void playBalancedSound(double balance, String soundFile) {
        pauseCurrentlyPlayingAudios();
        audioClip = new AudioClip(FXAudioEngine.class.getResource(soundBasePath + soundFile).toExternalForm());
        audioClip.play(1, balance, 1, balance, 1);
        playingAudios.add(audioClip);
        while (audioClip.isPlaying()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Plays a sound given a sound file path
     * @param soundFile the file name (path) of the audio clip
     * @param isBlocking whether the player should block the calling Thread while the sound is playing
     */
    public void playSound(String soundFile, boolean isBlocking) {
        audioClip = new AudioClip(FXAudioEngine.class.getResource(soundBasePath + soundFile).toExternalForm());
        audioClip.play();
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

    public void pauseAndPlaySound(String soundFile, boolean isBlocking) {
        pauseCurrentlyPlayingAudios();
        playSound(soundFile, isBlocking);
    }

    public void playNumSound(int number) {
        pauseCurrentlyPlayingAudios();
        playSound(numBasePath + String.valueOf(number) + ".wav", true);
    }

    public void playLetterSound(int number) {
        pauseCurrentlyPlayingAudios();
        playSound(letterBasePath + String.valueOf(number) + ".wav", true);
    }

    public void pauseCurrentlyPlayingAudios() {
        if(playingAudios.size() > 0)
            playingAudios.forEach(AudioClip::stop);
    }

}
