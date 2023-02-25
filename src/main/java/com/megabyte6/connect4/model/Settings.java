package com.megabyte6.connect4.model;

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

    public static final Settings DEFAULT = new Settings(7, 6, 4, Color.YELLOW, Color.RED);

    private int columnCount;
    private int rowCount;

    private int winRequirement;

    @NonNull
    private Color player1Color;
    @NonNull
    private Color player2Color;

    public Settings(int columnCount, int rowCount, int winRequirement, Color player1Color, Color player2Color) {
        this.columnCount = columnCount;
        this.rowCount = rowCount;
        this.winRequirement = winRequirement;
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
        config.set("player1Color", player1Color.toString());
        config.set("player2Color", player2Color.toString());

        config.save();
    }

}
