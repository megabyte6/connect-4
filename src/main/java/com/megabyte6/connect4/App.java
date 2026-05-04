package com.megabyte6.connect4;

import java.nio.file.Files;
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
import lombok.NonNull;
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
    private static Player winner = Player.NONE.get();

    @Getter
    private static Settings settings;
    private static final Path settingsPath = Path.of("config.json");
    private static final String XDG_APP_DIR = "connect-4";

    public static void main(String[] args) {
        settings = loadSettings();

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

    public static void delay(long millis, @NonNull Runnable runAfter) {
        if (millis < 0)
            throw new IllegalArgumentException("Delay time cannot be negative.");

        final Task<Void> sleep = new Task<>() {
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

        new Thread(sleep).start();;
    }

    public static void writeSettings() {
        if (!saveSettingsToPath(getConfigPath()))
            saveSettingsToPath(settingsPath);
    }

    public static void setWinner(@NonNull Player player) {
        if (!player.equals(player1) && !player.equals(player2) && !player.equals(Player.NONE.get()))
            return;
        App.winner = player;
    }

    public static void setSettings(@NonNull Settings settings) {
        App.settings = settings;
        player1.setColor(settings.getPlayer1Color());
        player2.setColor(settings.getPlayer2Color());
    }

    private static Settings loadSettings() {
        final Path configPath = getConfigPath();
        if (Files.isReadable(configPath) && !Files.isDirectory(configPath))
            return Settings.loadElseDefault(configPath);

        return Settings.loadElseDefault(settingsPath);
    }

    private static Path getConfigPath() {
        String configDir = System.getProperty("user.home");
        final String osName = System.getProperty("os.name", "").toLowerCase();
        if (osName.contains("win")) {
            String appData = System.getenv("APPDATA");
            if (appData == null || appData.isBlank())
                appData = System.getenv("LOCALAPPDATA");
            if (appData != null && !appData.isBlank())
                configDir = appData;
        } else if (osName.contains("mac")) {
            configDir = Path.of(System.getProperty("user.home"), "Library", "Application Support").toString();
        } else {
            String configHome = System.getenv("XDG_CONFIG_HOME");
            if (configHome == null || configHome.isBlank())
                configHome = Path.of(System.getProperty("user.home"), ".config").toString();
            configDir = configHome;
        }

        return Path.of(configDir, XDG_APP_DIR, "config.json");
    }

    private static boolean saveSettingsToPath(Path path) {
        try {
            final Path parent = path.getParent();
            if (parent != null)
                Files.createDirectories(parent);

            settings.save(path);
            return true;
        } catch (Exception e) {
            System.err.println("WARNING: Settings failed to save to " + path + ".");
            e.printStackTrace();
            return false;
        }
    }

}
