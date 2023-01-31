package com.megabyte6.connect4.model;

import javafx.scene.paint.Color;

public class Player {

    public static final Player NONE = new Player("", 0, Color.WHITE);

    private String name;
    private int score;
    private Color color;

    public Player() {
        this("John Doe", 0, Color.WHITE);
    }

    public Player(String name, Color color) {
        this(name, 0, color);
    }

    public Player(String name, int score, Color color) {
        this.name = name;
        this.score = score;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void incrementScore() {
        score++;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof Player other) {
            return name.equals(other.name)
                    && color.equals(other.color);
        }
        return false;
    }

    @Override
    public String toString() {
        return "Player[" +
                "name=" + name + ", " +
                "color=" + color +
                "]";
    }

}
