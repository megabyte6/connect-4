package com.megabyte6.connect4.util;

import com.megabyte6.connect4.App;
import javafx.scene.paint.Color;

public class Settings {

    public static final Settings DEFAULT = new Settings(7, 6, 4, Color.YELLOW, Color.RED);

    private int columnCount;
    private int rowCount;

    private int winRequirement;

    private Color player1Color;
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

    public int getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(int columnCount) {
        this.columnCount = columnCount;
    }

    public int getRowCount() {
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public int getWinRequirement() {
        return winRequirement;
    }

    public void setWinRequirement(int winRequirement) {
        this.winRequirement = winRequirement;
    }

    public Color getPlayer1Color() {
        return player1Color;
    }

    public void setPlayer1Color(Color player1Color) {
        this.player1Color = player1Color;
        App.getPlayer1().setColor(player1Color);
    }

    public Color getPlayer2Color() {
        return player2Color;
    }

    public void setPlayer2Color(Color player2Color) {
        this.player2Color = player2Color;
        App.getPlayer2().setColor(player2Color);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;

        Settings settings = (Settings) o;

        return columnCount == settings.columnCount
                && rowCount == settings.rowCount
                && winRequirement == settings.winRequirement
                && player1Color.equals(settings.player1Color)
                && player2Color.equals(settings.player2Color);
    }

    @Override
    public int hashCode() {
        int result = columnCount;
        result = 31 * result + rowCount;
        result = 31 * result + winRequirement;
        result = 31 * result + (player1Color != null ? player1Color.hashCode() : 0);
        result = 31 * result + (player2Color != null ? player2Color.hashCode() : 0);
        return result;
    }

}
