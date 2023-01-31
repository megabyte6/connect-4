package com.megabyte6.connect4;

import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.SceneManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static final int WIN_REQUIREMENT = 4;
    public static final Color BACKGROUND_COLOR = Color.web("#2d2d2d");
    public static final double DISABLED_OPACITY = 0.8;
    public static Player player1 = Player.NONE;
    public static Player player2 = Player.NONE;

    public static void main(String[] args) {
        launch(args);
    }

    public static void delay(long millis, Runnable runAfter) {
        if (millis < 0)
            throw new IllegalArgumentException("Delay time cannot be negative.");

        Task<Void> sleep = new Task<>() {
            @Override
            protected Void call() {
                try {
                    Thread.sleep(millis);
                } catch (InterruptedException e) {
                    System.err.println("Sleep interrupted.");
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    throw new IllegalArgumentException(
                            "Delay time cannot be negative.", e.fillInStackTrace());
                }
                return null;
            }
        };
        sleep.setOnSucceeded(event -> runAfter.run());

        new Thread(sleep);
    }

    public static Player getPlayer1() {
        return App.player1;
    }

    public static void setPlayer1(Player player) {
        App.player1 = player;
    }

    public static Player getPlayer2() {
        return App.player2;
    }

    public static void setPlayer2(Player player) {
        App.player2 = player;
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.init(primaryStage, "Start", BACKGROUND_COLOR);

        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("Connect 4");
        primaryStage.show();
    }

}
