package com.megabyte6.connect4.model;

import static com.megabyte6.connect4.util.Range.range;

import javafx.scene.paint.Color;

public class Game {

    private final Player player1;
    private final Player player2;

    private Player currentPlayer;

    private int selectedColumn = 3;

    // [column][row]
    private final GamePiece[][] gameBoard = new GamePiece[7][6];

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

    public Player getPlayer1() {
        return player1;
    }

    public Player getPlayer2() {
        return player2;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setSelectedColumn(int index) {
        if (index < 0 || index >= gameBoard.length)
            return;
        selectedColumn = index;
    }

    public int getSelectedColumn() {
        return selectedColumn;
    }

    public GamePiece[][] getGameBoard() {
        return gameBoard;
    }

    public GamePiece[] getGameBoardColumn(int columnNumber) {
        return gameBoard[columnNumber];
    }

    public GamePiece[] getGameBoardRow(int rowIndex) {
        GamePiece[] row = new GamePiece[gameBoard.length];
        for (int i : range(gameBoard.length)) {
            row[i] = gameBoard[i][rowIndex];
        }

        return row;
    }

    public int getColumnCount() {
        return gameBoard.length;
    }

    public int getRowCount() {
        return gameBoard[0].length;
    }

    public GamePiece getGamePiece(int columnIndex, int rowIndex) {
        return gameBoard[columnIndex][rowIndex];
    }

    public void setGamePiece(GamePiece gamePiece, int columnIndex, int rowIndex) {
        gameBoard[columnIndex][rowIndex] = gamePiece;
    }

}
