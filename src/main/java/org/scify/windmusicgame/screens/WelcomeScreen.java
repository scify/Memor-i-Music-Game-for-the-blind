/*
  Copyright 2016 SciFY.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */

package org.scify.windmusicgame.screens;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.scify.windmusicgame.MainOptions;

import java.awt.*;

/**
 * First Screen of the game
 * Created by pisaris on 11/10/2016.
 */
public class WelcomeScreen extends Application {
    private Rectangle graphicsEnvironment = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();

    public WelcomeScreen() {
        System.err.println("SELF:" + this.toString() + " from " + Thread.currentThread().toString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/welcome_screen.fxml"));
        Parent root = fxmlLoader.load();
        primaryStage.setTitle("Memor-i");
        WelcomeScreenController controller = fxmlLoader.getController();
        // set as width and height the screen width and height
        MainOptions.mWidth = graphicsEnvironment.getWidth() - 10;
        MainOptions.mHeight = graphicsEnvironment.getHeight() - 10;
        // construct the scene (the content of the stage)
        Scene primaryScene = new Scene(root, MainOptions.mWidth, MainOptions.mHeight);

        controller.setParameters(primaryStage, primaryScene);
    }
}
