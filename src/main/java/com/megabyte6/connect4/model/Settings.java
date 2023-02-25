package com.megabyte6.connect4.model;

import static java.util.Objects.requireNonNullElse;
import java.nio.file.Files;
import java.nio.file.Path;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.megabyte6.connect4.App;
import javafx.scene.paint.Color;
import lombok.Cleanup;
import lombok.Data;
import lombok.NonNull;

@Data
public class Settings {

    public static final Settings DEFAULT = new Settings(7, 6, 4, false, 0, Color.YELLOW, Color.RED);

    private int columnCount;
    private int rowCount;

    private int winRequirement;

    private boolean timerEnabled;
    private int timerLength;

    @NonNull
    private Color player1Color;
    @NonNull
    private Color player2Color;

    public Settings(int columnCount, int rowCount, int winRequirement, boolean timerEnabled, int timerLength,
            Color player1Color, Color player2Color) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;

        this.winRequirement = winRequirement;

        this.timerEnabled = timerEnabled;
        this.timerLength = timerLength;

        this.player1Color = player1Color;
        this.player2Color = player2Color;

        App.getPlayer1().setColor(player1Color);
        App.getPlayer2().setColor(player2Color);
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
        config.set("player1Color", player1Color.toString());
        config.set("player2Color", player2Color.toString());

        config.save();
    }

    public static Settings load(Path path) {
        if (Files.notExists(path) || Files.isDirectory(path))
            return Settings.DEFAULT;

        @Cleanup
        final FileConfig config = FileConfig.of(path);
        config.load();

        return new Settings(
                requireNonNullElse(config.get("columnCount"), DEFAULT.getColumnCount()),
                requireNonNullElse(config.get("rowCount"), DEFAULT.getRowCount()),
                requireNonNullElse(config.get("winRequirement"), DEFAULT.getWinRequirement()),
                requireNonNullElse(config.get("timerEnabled"), DEFAULT.isTimerEnabled()),
                requireNonNullElse(config.get("timerLength"), DEFAULT.getTimerLength()),
                Color.valueOf(requireNonNullElse(
                        config.get("player1Color"), DEFAULT.getPlayer1Color().toString())),
                Color.valueOf(requireNonNullElse(
                        config.get("player2Color"), DEFAULT.getPlayer2Color().toString())));
    }

}
