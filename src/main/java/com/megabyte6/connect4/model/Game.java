package com.megabyte6.connect4.model;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.util.tuple.Triplet;
import com.megabyte6.connect4.util.tuple.Tuple;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static com.megabyte6.connect4.util.Range.range;

public class Game {

    private boolean paused = false;
    private boolean gameOver = false;

    private final Player player1;
    private final Player player2;

    private Player currentPlayer;

    // [column][row]
    private final GamePiece[][] gameBoard;

    private int selectedColumn;

    // Player, Column, Row
    private final LinkedList<Triplet<Player, Integer, Integer>> moveHistory = new LinkedList<>();
    private int historyPointer = -1;

    public Game(Player player1, Player player2, int columns, int rows) {
        this.player1 = player1;
        this.player2 = player2;
        currentPlayer = this.player1;

        gameBoard = new GamePiece[columns][rows];
        selectedColumn = columns / 2;
    }

    public void swapTurns() {
        currentPlayer = currentPlayer == player1
                ? player2
                : player1;
    }

    public int findNextFreeRow(int columnIndex) {
        final GamePiece[] column = getGameBoardColumn(columnIndex);

        for (int i : range(column.length - 1, -1)) {
            if (column[i].getOwner().equals(Player.NONE.get()))
                return i;
        }

        return -1;
    }

    public List<Integer> findFreeColumns() {
        List<Integer> freeColumns = new ArrayList<>();

        for (int i = 0; i < getColumnCount(); i++) {
            if (findNextFreeRow(i) != -1) {
                freeColumns.add(i);
            }
        }

        return freeColumns;
    }

    public boolean isOutOfBounds(int columnIndex, int rowIndex) {
        return columnIsOutOfBounds(columnIndex) || rowIsOutOfBounds(rowIndex);
    }

    public boolean columnIsOutOfBounds(int columnIndex) {
        return columnIndex < 0 || columnIndex >= getColumnCount();
    }

    public boolean rowIsOutOfBounds(int rowIndex) {
        return rowIndex < 0 || rowIndex >= getRowCount();
    }

    public boolean isPaused() {
        return paused;
    }

    public void pause() {
        paused = true;
    }

    public void unpause() {
        paused = false;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public void gameOver() {
        gameOver = true;
        paused = true;
    }

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setSelectedColumn(int index) {
        if (paused || index < 0 || index >= gameBoard.length)
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

    public void addMoveToHistory(Player player, int column, int row) {
        moveHistory.add(Tuple.of(player, column, row));
        historyPointer = getMoveCount() - 1;
    }

    public int getMoveCount() {
        return moveHistory.size();
    }

    public void moveHistoryPointerBack() {
        // Check if the user is already at the beginning.
        if (historyPointer == -1)
            return;

        paused = true;

        var selectedAction = moveHistory.get(historyPointer);
        GamePiece selectedGamePiece = getGamePiece(selectedAction.b(), selectedAction.c());
        selectedGamePiece.setOwner(Player.NONE.get());
        selectedGamePiece.setFill(App.BACKGROUND_COLOR);

        historyPointer--;
    }

    public void moveHistoryPointerForward() {
        // Check if the user is already at the end.
        if (historyPointerIsAtLatestMove())
            return;

        historyPointer++;

        var selectedAction = moveHistory.get(historyPointer);
        GamePiece selectedGamePiece = getGamePiece(selectedAction.b(), selectedAction.c());
        selectedGamePiece.setOwner(selectedAction.a());

        if (historyPointerIsAtLatestMove() && !gameOver)
            paused = false;
    }

    public boolean historyPointerIsAtLatestMove() {
        return historyPointer == moveHistory.size() - 1;
    }

    /**
     * @param index Index of the move.
     * @return A Triplet of the Player who made the move, the column of the
     * location of the move, and the row of the location of the move.
     */
    public Triplet<Player, Integer, Integer> getMoveAtIndex(int index) {
        return moveHistory.get(index);
    }

    /**
     * @return A Triplet of the Player who made the move, the column of the
     * location of the move, and the row of the location of the move.
     */
    public Triplet<Player, Integer, Integer> getLastMove() {
        if (historyPointer <= 0)
            return null;
        return getMoveAtIndex(historyPointer);
    }

}
