package org.scify.memori;

import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class SceneHandler {

    static Stage mainWindow;
    static List<Scene> allScenes = new ArrayList<>();

    public static void pushScene(Scene sToPush) {
        allScenes.add(sToPush);
        // Set the added scene as active
        mainWindow.setScene(sToPush);
    }

    public static Scene  popScene() {
        Scene sToPop = allScenes.get(allScenes.size() - 1); // Get last added
        allScenes.remove(allScenes.size() - 1); // and pop it
        // update active scene
        mainWindow.setScene(allScenes.get(allScenes.size() - 1));
        // return removed scene
        return sToPop;
    }
}
