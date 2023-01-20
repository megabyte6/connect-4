package com.megabyte6.connect4.model;

import javafx.scene.paint.Color;

public class Game {

    private final Player player1;
    private final Player player2;

    private Player currentPlayer;

    public Game(String player1Name, String player2Name) {
        player1 = new Player(player2Name, Color.YELLOW);
        player2 = new Player(player1Name, Color.RED);

        currentPlayer = player1;
    }

    public void swapTurns() {
        currentPlayer = currentPlayer == player1
                ? player2
                : player1;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

}
