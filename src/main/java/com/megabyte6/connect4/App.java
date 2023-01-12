package com.megabyte6.connect4;

import com.megabyte6.connect4.util.SceneManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#2d2d2d");

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(new StackPane(
                FXMLLoader.load(getClass().getResource("view/Start.fxml"))));

        // primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("Connect 4");
        primaryStage.setScene(scene);
        primaryStage.show();

        SceneManager.init(primaryStage, scene, BACKGROUND_COLOR);
    }

}
