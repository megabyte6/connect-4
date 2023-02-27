package com.megabyte6.connect4.model;

import java.util.function.Supplier;
import javafx.scene.paint.Color;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
public class Player {

    public static final Supplier<Player> NONE = () -> new Player("", 0, Color.WHITE);
    public static final Supplier<Player> OBSTACLE = () -> new Player("Obstacle", 0, Color.BLACK);

    private String name;
    @Setter(AccessLevel.NONE)
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

    public void incrementScore() {
        score++;
    }

}
