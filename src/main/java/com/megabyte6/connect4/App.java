package com.megabyte6.connect4;

import com.megabyte6.connect4.util.SceneManager;

import javafx.application.Application;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#2d2d2d");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        SceneManager.init(primaryStage, "Start", BACKGROUND_COLOR);

        // primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("Connect 4");
        primaryStage.setScene(SceneManager.getScene());
        primaryStage.show();
    }

}
