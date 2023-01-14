package com.megabyte6.connect4.model;

import javafx.scene.paint.Color;

public class Player {

    private String name;
    private Color color;

    public Player() {
        this("John Doe", Color.WHITE);
    }

    public Player(String name) {
        this(name, Color.WHITE);
    }

    public Player(String name, Color color) {
        this.name = name;
        this.color = color;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

}
