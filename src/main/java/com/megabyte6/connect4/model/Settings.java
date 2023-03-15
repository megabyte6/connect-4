package com.megabyte6.connect4.model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.colorwrapper.ColorWrapperDeserializer;
import com.megabyte6.connect4.model.colorwrapper.ColorWrapperSerializer;
import javafx.scene.paint.Color;
import lombok.Data;
import lombok.NonNull;

@Data
public class Settings {

    private int columnCount;
    private int rowCount;
    private int winRequirement;
    private boolean boardWrappingEnabled;

    private boolean timerEnabled;
    private int timerLength;
    private boolean timerAutoDrop;

    @NonNull
    @JsonSerialize(using = ColorWrapperSerializer.class)
    @JsonDeserialize(using = ColorWrapperDeserializer.class)
    private Color player1Color;
    @NonNull
    @JsonSerialize(using = ColorWrapperSerializer.class)
    @JsonDeserialize(using = ColorWrapperDeserializer.class)
    private Color player2Color;

    private boolean obstaclesEnabled;
    private int numOfObstacles;
    @NonNull
    @JsonSerialize(using = ColorWrapperSerializer.class)
    @JsonDeserialize(using = ColorWrapperDeserializer.class)
    private Color obstacleColor;

    public Settings() {
        columnCount = 7;
        rowCount = 6;
        winRequirement = 4;
        boardWrappingEnabled = false;
        timerEnabled = false;
        timerLength = 10000;
        timerAutoDrop = false;
        player1Color = Color.YELLOW;
        player2Color = Color.RED;
        obstaclesEnabled = false;
        numOfObstacles = 5;
        obstacleColor = Color.WHITE;
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
            return new Settings();

        final ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(path.toFile(), Settings.class);
    }

    public static Settings loadElseDefault(Path path) {
        Settings settings = new Settings();
        try {
            settings = load(path);
        } catch (Exception e) {
            System.err.println("WARNING: Settings failed to load.");
            e.printStackTrace();
        }

        return settings;
    }

}
