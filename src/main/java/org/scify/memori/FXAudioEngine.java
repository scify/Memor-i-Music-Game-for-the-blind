package org.scify.memori;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import org.scify.memori.interfaces.AudioEngine;

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
    private String emptySound = "blip.wav";

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

    private void pauseSound() {
        if(audioClip != null)
            audioClip.stop();
        if(movementSoundPlayer != null)
            movementSoundPlayer.stop();
    }

    public void playMovementSound(double balance, double rate) {
        //playSound(movementSound);
        pauseSound();
        if(movementSoundMedia == null) {
            //System.out.println("initialise movement sound");
            movementSoundMedia = new Media(FXAudioEngine.class.getResource(soundBasePath + movementSound).toExternalForm());
            movementSoundPlayer = new MediaPlayer(movementSoundMedia);
        }
        //audioClip = new AudioClip(AudioEngine.class.getResource(soundBasePath + movementSound).toExternalForm());
        movementSoundPlayer.setBalance(balance);
        movementSoundPlayer.setRate(rate);
        //audioClip.rateProperty();
        movementSoundPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                //System.out.println("stop movement sound");
                movementSoundPlayer.stop();
            }
        });
        movementSoundPlayer.play();


    }

    public void playCardSound(String soundFile, boolean isBlocking) {
        playSound(soundFile, isBlocking);
    }


    public void playSuccessSound() {
        playSound(successSound, true);
    }

    public void playInvalidMovementSound() {
        playSound(invalidMovementSound);
    }

    public void playFailureSound() {
        //System.out.println("playFailureSound");
        playSound(failureSound);
    }

    public void playEmptySound() {
        playSound(emptySound);
    }

    @Override
    public void playSound(String soundFile) {
        playSound(soundFile, false);
    }

    public void playSound(String soundFile, boolean isBlocking) {
        pauseSound();
        audioClip = new AudioClip(FXAudioEngine.class.getResource(soundBasePath + soundFile).toExternalForm());
        audioClip.setCycleCount(0);
        audioClip.play();

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

}
