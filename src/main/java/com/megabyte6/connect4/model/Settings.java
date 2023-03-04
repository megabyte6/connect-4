package com.megabyte6.connect4.model;

import static java.util.Objects.requireNonNullElse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.megabyte6.connect4.App;
import javafx.scene.paint.Color;
import lombok.Cleanup;
import lombok.Data;
import lombok.NonNull;

@Data
public class Settings {

    public static final Supplier<Settings> DEFAULT = () -> new Settings(7, 6, 4, false, 10000, false,
            Color.YELLOW, Color.RED, false, 5, Color.WHITE);

    private int columnCount;
    private int rowCount;
    private int winRequirement;

    private boolean timerEnabled;
    private int timerLength;
    private boolean timerAutoDrop;

    @NonNull
    private Color player1Color;
    @NonNull
    private Color player2Color;

    private boolean obstaclesEnabled;
    private int numOfObstacles;
    @NonNull
    private Color obstacleColor;

    public Settings(int columnCount, int rowCount, int winRequirement,
            boolean timerEnabled, int timerLength, boolean timerAutoDrop,
            Color player1Color, Color player2Color,
            boolean obstaclesEnabled, int numOfObstacles, Color obstacleColor) {

        this.columnCount = columnCount >= 0 ? columnCount : 7;
        this.rowCount = rowCount >= 0 ? rowCount : 6;
        this.winRequirement = winRequirement >= 0 ? winRequirement : 4;

        this.timerEnabled = timerEnabled;
        this.timerLength = timerLength >= 0 ? timerLength : 10000;
        this.timerAutoDrop = timerAutoDrop;

        this.player1Color = player1Color;
        this.player2Color = player2Color;

        this.obstaclesEnabled = obstaclesEnabled;
        this.numOfObstacles = numOfObstacles;
        this.obstacleColor = obstacleColor;

        App.getPlayer1().setColor(player1Color);
        App.getPlayer2().setColor(player2Color);
    }

    public int getTimerLengthInSeconds() {
        return timerLength / 1000;
    }

    public void setTimerLengthInSeconds(int timerLength) {
        if (timerLength < 0)
            throw new IllegalArgumentException("Timer length cannot be negative.");
        this.timerLength = timerLength * 1000;
    }

    public void setPlayer1Color(Color player1Color) {
        this.player1Color = player1Color;
        App.getPlayer1().setColor(player1Color);
    }

    public void setPlayer2Color(Color player2Color) {
        this.player2Color = player2Color;
        App.getPlayer2().setColor(player2Color);
    }

    public void save(Path path) {
        if (Files.isDirectory(path))
            return;

        @Cleanup
        final FileConfig config = FileConfig.of(path);

        config.set("columnCount", columnCount);
        config.set("rowCount", rowCount);
        config.set("winRequirement", winRequirement);
        config.set("timerEnabled", timerEnabled);
        config.set("timerLength", timerLength);
        config.set("timerAutoDrop", timerAutoDrop);
        config.set("player1Color", player1Color.toString());
        config.set("player2Color", player2Color.toString());
        config.set("obstaclesEnabled", obstaclesEnabled);
        config.set("numOfObstacles", numOfObstacles);
        config.set("obstacleColor", obstacleColor.toString());

        config.save();
    }

    public static Settings load(Path path) {
        if (Files.notExists(path) || Files.isDirectory(path))
            return Settings.DEFAULT.get();

        @Cleanup
        final FileConfig config = FileConfig.of(path);
        config.load();

        final Settings def = Settings.DEFAULT.get();

        return new Settings(
                requireNonNullElse(config.get("columnCount"), def.getColumnCount()),
                requireNonNullElse(config.get("rowCount"), def.getRowCount()),
                requireNonNullElse(config.get("winRequirement"), def.getWinRequirement()),
                requireNonNullElse(config.get("timerEnabled"), def.isTimerEnabled()),
                requireNonNullElse(config.get("timerLength"), def.getTimerLength()),
                requireNonNullElse(config.get("timerAutoDrop"), def.isTimerAutoDrop()),
                Color.valueOf(requireNonNullElse(
                        config.get("player1Color"), def.getPlayer1Color().toString())),
                Color.valueOf(requireNonNullElse(
                        config.get("player2Color"), def.getPlayer2Color().toString())),
                requireNonNullElse(config.get("obstaclesEnabled"), def.isObstaclesEnabled()),
                requireNonNullElse(config.get("numOfObstacles"), def.getNumOfObstacles()),
                Color.valueOf(requireNonNullElse(
                        config.get("obstacleColor"), def.getObstacleColor().toString())));
    }

}
