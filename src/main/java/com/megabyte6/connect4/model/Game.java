package com.megabyte6.connect4.model;

import javafx.scene.paint.Color;

public class Game {

    private Player player1;
    private Player player2;

    public Game(String player1Name, String player2Name) {
        player1 = new Player(player1Name, Color.RED);
        player2 = new Player(player2Name, Color.YELLOW);
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

}
