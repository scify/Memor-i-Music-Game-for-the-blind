package org.scify.memori;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.net.URL;
import java.util.HashMap;

public class AudioEngine{

    private static AudioClip audioClip;
    private static MediaPlayer movementSoundPlayer;
    private static Media movementSoundMedia;
    private static String soundBasePath = "/audios/";
    private static String movementSound = "beep.mp3";
    private static String successSound = "success.wav";
    private static String invalidMovementSound = "invalid_movement.wav";
    private static String errorSound = "error.wav";
    private static String emptySound = "blip.wav";

    private static HashMap<Integer, String> rowHelpSounds = new HashMap<>();
    private static HashMap<Integer, String> columnHelpSounds = new HashMap<>();

    public AudioEngine() {
        columnHelpSounds.put(0, "one.wav");
        columnHelpSounds.put(1, "two.wav");
        columnHelpSounds.put(2, "three.wav");
        columnHelpSounds.put(3, "four.wav");

        rowHelpSounds.put(0, "A.wav");
        rowHelpSounds.put(1, "B.wav");
        rowHelpSounds.put(2, "C.wav");
        rowHelpSounds.put(3, "D.wav");

    }

    public static void playHelperSound(int rowIndex, int columnIndex) {
        System.out.println("row: " + rowHelpSounds.get(rowIndex));
        System.out.println("column: " + columnHelpSounds.get(columnIndex));
    }

    private static void pauseSound() {
        if(audioClip != null)
            audioClip.stop();
        if(movementSoundPlayer != null)
            movementSoundPlayer.stop();
    }

    public static void playMovementSound(double balance, double rate) {
        //playSound(movementSound);
        pauseSound();
        if(movementSoundMedia == null) {
            System.out.println("initialise movement sound");
            movementSoundMedia = new Media(AudioEngine.class.getResource(soundBasePath + movementSound).toExternalForm());
            movementSoundPlayer = new MediaPlayer(movementSoundMedia);
        }
        //audioClip = new AudioClip(AudioEngine.class.getResource(soundBasePath + movementSound).toExternalForm());
        movementSoundPlayer.setBalance(balance);
        movementSoundPlayer.setRate(rate);
        //audioClip.rateProperty();
        movementSoundPlayer.setOnEndOfMedia(new Runnable() {
            public void run() {
                System.out.println("stop movement sound");
                movementSoundPlayer.stop();
            }
        });
        movementSoundPlayer.play();


    }

    public static void playCardSound(String soundFile) {
        playSound(soundFile);
    }

    public static void playSuccessSound() {
        playSound(successSound);
    }

    public static void playInvalidMovementSound() {
        playSound(invalidMovementSound);
    }

    public static void playErrorSound() {
        //System.out.println("playFailureSound");
        playSound(errorSound);
    }

    public static void playEmptySound() {
        playSound(emptySound);
    }

    public static void playSound(String soundFile) {
        pauseSound();
        audioClip = new AudioClip(AudioEngine.class.getResource(soundBasePath + soundFile).toExternalForm());
        audioClip.setCycleCount(0);
        audioClip.play();
    }

}
