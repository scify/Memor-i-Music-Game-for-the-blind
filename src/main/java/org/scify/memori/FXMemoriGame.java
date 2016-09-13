package org.scify.memori;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class FXMemoriGame extends MemoriGame {


    @Override
    public void initialize() {
        super.initialize();

        FXRenderingEngine fUI = new FXRenderingEngine(SceneHandler.mainWindow);
        uInterface = fUI;
        reRenderer = fUI;

        // Plus update current scene
        SceneHandler.pushScene(fUI.gameScene);

        fUI.gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    System.err.println("END GAME");
                    SceneHandler.popScene();
                    break;
            }
        });
    }

}
