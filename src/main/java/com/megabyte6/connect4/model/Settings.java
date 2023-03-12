package com.megabyte6.connect4.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.function.Supplier;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.megabyte6.connect4.App;
import javafx.scene.paint.Color;
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

    public void save(Path path) throws IOException, StreamWriteException, DatabindException {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(path.toFile(), this);
    }

    public static Settings load(Path path) throws IOException, StreamReadException, DatabindException {
        if (Files.isDirectory(path) || !Files.isReadable(path))
            return DEFAULT.get();

        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.toFile(), Settings.class);
    }

    public static Settings loadElseDefault(Path path) {
        Settings settings;
        try {
            settings = load(path);
        } catch (DatabindException dbe) {
            dbe.printStackTrace();
            settings = DEFAULT.get();
        } catch (Exception e) {
            System.err.println("WARNING: Settings failed to load.");
            settings = DEFAULT.get();
        }

        return settings;
    }

}
