
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

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Screen;
import javafx.stage.Stage;


public class MainScreen extends Application {
    private StackPane stackPane;
    private double mWidth = Screen.getPrimary().getBounds().getWidth();
    private double mHeight = Screen.getPrimary().getBounds().getHeight();


    public MainScreen() {
        System.err.println("SELF:" + this.toString() + " from " + Thread.currentThread().toString());
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        // DEBUG LINES
        System.err.println("SELF:" + this.toString());
        //Load fxml file (layout xml) for first screen
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/fxml/first_screen.fxml"));
        Parent root = fxmlLoader.load();
        stackPane = ((StackPane) root);
        primaryStage.setTitle("Memor-i");
        MainScreenController controller = fxmlLoader.getController();
        // set as width and height the screen width and height
        MainOptions.mWidth = mWidth;
        MainOptions.mHeight = mHeight;
        // construct the scene (the content of the stage)
        Scene primaryScene = new Scene(root, mWidth, mHeight);

        controller.setParameters(primaryStage, primaryScene);

    }

}
