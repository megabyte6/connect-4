package com.megabyte6.connect4;

import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.NoFormatFoundException;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.model.Settings;
import com.megabyte6.connect4.util.SceneManager;
import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.nio.file.Files;
import java.nio.file.Path;

import static java.util.Objects.requireNonNullElse;

public class App extends Application {

    public static final Color BACKGROUND_COLOR = Color.web("#2d2d2d");
    public static final double DISABLED_OPACITY = 0.8;

    private static Player player1 = new Player("", Color.YELLOW);
    private static Player player2 = new Player("", Color.RED);
    private static Player winner = Player.NONE;

    private static Settings settings;
    private static final Path settingsPath = Path.of("config.toml");

    public static void main(String[] args) {
        settings = readSettings(settingsPath);

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

    private static Settings readSettings(Path path) {
        if (Files.notExists(path) || Files.isDirectory(path))
            return Settings.DEFAULT;

        final FileConfig config;
        try {
            config = FileConfig.of(path);
        } catch (NoFormatFoundException e) {
            e.printStackTrace();
            return Settings.DEFAULT;
        }
        config.load();

        final Settings df = Settings.DEFAULT;
        final int columnCount = requireNonNullElse(config.get("columnCount"), df.getColumnCount());
        final int rowCount = requireNonNullElse(config.get("rowCount"), df.getRowCount());
        final int winRequirement = requireNonNullElse(config.get("winRequirement"), df.getWinRequirement());
        final String player1Color = requireNonNullElse(config.get("player1Color"), df.getPlayer1Color().toString());
        final String player2Color = requireNonNullElse(config.get("player2Color"), df.getPlayer2Color().toString());

        config.close();

        return new Settings(
                columnCount,
                rowCount,
                winRequirement,
                Color.valueOf(player1Color),
                Color.valueOf(player2Color)
        );
    }

    private static void writeSettings(Path path, Settings settings) {
        if (Files.isDirectory(path))
            return;

        final FileConfig config;
        try {
            config = FileConfig.of(path);
        } catch (NoFormatFoundException e) {
            e.printStackTrace();
            return;
        }

        config.set("columnCount", settings.getColumnCount());
        config.set("rowCount", settings.getRowCount());
        config.set("winRequirement", settings.getWinRequirement());
        config.set("player1Color", settings.getPlayer1Color().toString());
        config.set("player2Color", settings.getPlayer2Color().toString());

        config.save();
        config.close();
    }

    public static void writeSettings() {
        writeSettings(settingsPath, settings);
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

    public static Settings getSettings() {
        return App.settings;
    }

    public static void setSettings(Settings settings) {
        App.settings = settings;
    }

}
