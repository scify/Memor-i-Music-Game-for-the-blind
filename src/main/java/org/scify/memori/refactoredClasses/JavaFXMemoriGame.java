package org.scify.memori.refactoredClasses;

import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JavaFXMemoriGame extends MemoriGame {
    Stage mainWindow;
    List<Scene> allScenes;

    public JavaFXMemoriGame(Stage prevWindow) {
        super();

        mainWindow = prevWindow;
        allScenes = new ArrayList<>();
        allScenes.add(prevWindow.getScene()); // Initialize with main scene
    }

    public Stage getActiveWindow() {
        return mainWindow;
    }

    @Override
    public void initialize() {
        super.initialize();

        FXRenderingEngine fUI = new FXRenderingEngine(mainWindow);
        uInterface = fUI;
        reRenderer = fUI;

        // Plus update current scene
        pushScene(fUI.gameScene);

        fUI.gameScene.setOnKeyReleased(event -> {
            switch (event.getCode()) {
                case ESCAPE:
                    //mStage.close();
                    popScene();
                    break;
            }
        });
    }

    public void pushScene(Scene sToPush) {
        allScenes.add(sToPush);
        // Set the added scene as active
        mainWindow.setScene(sToPush);
    }

    public Scene  popScene() {
        Scene sToPop = allScenes.get(allScenes.size() - 1); // Get last added
        allScenes.remove(allScenes.size() - 1); // and pop it
        // update active scene
        mainWindow.setScene(allScenes.get(allScenes.size() - 1));
        // return removed scene
        return sToPop;
    }
}
