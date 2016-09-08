package org.scify.memori.memoripoc;

import org.scify.memori.*;
import org.scify.memori.interfaces.AudioEngine;
import org.scify.memori.interfaces.*;
import org.scify.memori.interfaces.GameState;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Created by pisaris on 5/9/2016.
 */
public class FlipConsoleUI implements UserInterface, RenderingEngine {
    AudioEngine aeSound;

    public FlipConsoleUI() {
        aeSound = new FlipAudioEngine();
    }

    @Override
    public UserAction getNextUserAction(Player pCurrentPlayer) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Please enter you next coordinates.");
        System.out.println("Enter x:");
        String xPos = null;
        try {
            xPos = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int x = Integer.valueOf(xPos);
        System.out.println("Enter y:");
        String yPos = null;
        try {
            yPos = reader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        int y = Integer.valueOf(yPos);

        return new UserAction("flip", x, y);
    }

    @Override
    public void drawGameState(GameState currentState) {
        FlipGameState fgsState = (FlipGameState)currentState;
        //Draw the game state
        System.out.println(fgsState.getTerrain());

        // Deal with events, if they exist
        if (((FlipGameState) currentState).getEvents().isEmpty())
            return;

        if (((FlipGameState) currentState).getEvents().get(0).equals("Invalid Action")) {
            aeSound.playSound("beep");
            System.out.printf("You have provided an invalid action.");
            ((FlipGameState) currentState).getEvents().remove(0); // Indicate event as handled
        }
    }
}
