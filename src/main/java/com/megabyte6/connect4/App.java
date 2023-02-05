package com.megabyte6.connect4;

import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.SceneManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#2d2d2d");
    public static final double DISABLED_OPACITY = 0.8;

    private static Player player1 = new Player("", Color.YELLOW);
    private static Player player2 = new Player("", Color.RED);
    private static Player winner = Player.NONE;

    private static int winRequirement = 4;
    private static int columnCount = 7;
    private static int rowCount = 6;

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

    public static Player getWinner() {
        return App.winner;
    }

    public static void setWinner(Player player) {
        if (!player.equals(player1) && !player.equals(player2) && !player.equals(Player.NONE))
            return;
        App.winner = player;
    }

    public static int getWinRequirement() {
        return App.winRequirement;
    }

    public static void setWinRequirement(int winRequirement) {
        App.winRequirement = winRequirement;
    }

    public static int getColumnCount() {
        return App.columnCount;
    }

    public static void setColumnCount(int columns) {
        App.columnCount = columns;
    }

    public static int getRowCount() {
        return App.rowCount;
    }

    public static void setRowCount(int rows) {
        App.rowCount = rows;
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.init(primaryStage, "Start", BACKGROUND_COLOR);

        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("Connect 4");
        primaryStage.show();
    }

}
