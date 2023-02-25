package com.megabyte6.connect4;

import java.nio.file.Path;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.model.Settings;
import com.megabyte6.connect4.util.SceneManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import lombok.Getter;
import lombok.Setter;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#2d2d2d");
    public static final double DISABLED_OPACITY = 0.8;

    @Getter
    @Setter
    private static Player player1 = new Player("", Color.YELLOW);
    @Getter
    @Setter
    private static Player player2 = new Player("", Color.RED);
    @Getter
    private static Player winner = Player.NONE;

    @Getter
    private static Settings settings;
    private static final Path settingsPath = Path.of("config.toml");

    public static void main(String[] args) {
        settings = Settings.load(settingsPath);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        SceneManager.init(primaryStage, "Start", BACKGROUND_COLOR);

        primaryStage.getIcons().add(new Image("icon.png"));
        primaryStage.setTitle("Connect 4");
        primaryStage.show();
    }

    @Override
    public void stop() {
        writeSettings();
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

    public static void writeSettings() {
        settings.save(settingsPath);
    }

    public static void setWinner(Player player) {
        if (!player.equals(player1) && !player.equals(player2) && !player.equals(Player.NONE))
            return;
        App.winner = player;
    }

    public static void setSettings(Settings settings) {
        App.settings = settings;
        player1.setColor(settings.getPlayer1Color());
        player2.setColor(settings.getPlayer2Color());
    }

}
