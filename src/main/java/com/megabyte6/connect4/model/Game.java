package com.megabyte6.connect4.model;

import static com.megabyte6.connect4.util.Range.range;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.megabyte6.connect4.App;
import com.megabyte6.connect4.util.tuple.Triplet;
import com.megabyte6.connect4.util.tuple.Tuple;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

@ToString
@EqualsAndHashCode
public class Game {

    @Getter
    private boolean paused = false;
    @Getter
    private boolean gameOver = false;

    private final Player player1;
    private final Player player2;

    @Getter
    private Player currentPlayer;

    // [column][row]
    @Getter
    private final GamePiece[][] gameBoard;

    @Getter
    private int selectedColumn;

    @Getter
    @Setter
    private Timer timer;

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

    public void setPaused(boolean paused) {
        this.paused = paused;

        if (timer != null) {
            if (paused) {
                timer.stop();
            } else {
                timer.resume();
            }
        }
    }

    public void gameOver() {
        gameOver = true;
        setPaused(true);
    }

    public void setSelectedColumn(int index) {
        if (isPaused() || index < 0 || index >= gameBoard.length)
            return;
        selectedColumn = index;
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

    public void resetTimer(@NonNull Runnable onUpdate, @NonNull Runnable onTimeout) {
        if (!App.getSettings().isTimerEnabled())
            return;

        if (timer != null)
            timer.stop();

        timer = new Timer(App.getSettings().getTimerLength());
        timer.setOnUpdate(onUpdate);
        timer.setOnTimeout(onTimeout);
        timer.start();
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

        setPaused(true);

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
            setPaused(false);
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
