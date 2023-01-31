package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.Game;
import com.megabyte6.connect4.model.GamePiece;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.Position;
import com.megabyte6.connect4.util.SceneManager;
import com.megabyte6.connect4.util.Walker;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.util.Duration;

import java.util.Objects;
import java.util.stream.Stream;

import static com.megabyte6.connect4.util.Range.range;

public class GameController implements Controller {

    private final Game game = new Game("John", "James");
    private Position[] winningPositions = null;

    @FXML
    private AnchorPane root;

    @FXML
    private Pane markerContainer;
    private GamePiece marker;
    private final DoubleBinding[] markerBindings = new DoubleBinding[game.getColumnCount()];

    @FXML
    private Pane gameBoard;

    @FXML
    private void initialize() {
        // Initialize gameBoard width and height.
        StackPane gameBoardContainer = (StackPane) gameBoard.getParent();

        gameBoardContainer.widthProperty().addListener((observable, oldValue, newValue) ->
                handleWindowSizeChanged(new Dimension2D(newValue.doubleValue(), gameBoardContainer.getHeight())));
        gameBoardContainer.heightProperty().addListener((observable, oldValue, newValue) ->
                handleWindowSizeChanged(new Dimension2D(gameBoardContainer.getWidth(), newValue.doubleValue())));

        // Draw horizontal grid lines.
        for (var i : range(game.getRowCount() + 1)) {
            double multiplier = ((double) i) / game.getRowCount();
            DoubleBinding y = gameBoard.heightProperty().multiply(multiplier);

            Line line = new Line();
            line.setStroke(Color.WHITE);
            line.startYProperty().bind(y);
            line.endXProperty().bind(gameBoard.widthProperty());
            line.endYProperty().bind(y);

            gameBoard.getChildren().add(line);
        }
        // Draw vertical grid lines.
        for (var i : range(game.getColumnCount())) {
            double multiplier = ((double) i) / game.getColumnCount();
            DoubleBinding x = gameBoard.widthProperty().multiply(multiplier);

            Line line = new Line();
            line.setStroke(Color.WHITE);
            line.startXProperty().bind(x);
            line.endXProperty().bind(x);
            line.endYProperty().bind(gameBoard.heightProperty());

            gameBoard.getChildren().add(line);
        }

        // Create GamePieces.
        DoubleBinding cellSizeBinding = gameBoard.heightProperty().divide(game.getRowCount());
        final int border = 5;
        DoubleBinding radiusBinding = cellSizeBinding.divide(2).subtract(border);
        for (int col : range(game.getColumnCount())) {
            double xMultiplier = ((double) col) / game.getColumnCount();
            DoubleBinding xOffset = cellSizeBinding.divide(2);

            DoubleBinding xBinding = gameBoard.widthProperty()
                    .multiply(xMultiplier).add(xOffset);

            for (int row : range(game.getRowCount())) {
                double yMultiplier = ((double) row) / game.getRowCount();
                DoubleBinding yOffset = cellSizeBinding.divide(2);

                DoubleBinding yBinding = gameBoard.heightProperty()
                        .multiply(yMultiplier).add(yOffset);

                GamePiece blankPiece = new GamePiece();
                blankPiece.layoutXProperty().bind(xBinding);
                blankPiece.layoutYProperty().bind(yBinding);
                blankPiece.radiusProperty().bind(radiusBinding);
                blankPiece.setFill(App.BACKGROUND_COLOR);
                blankPiece.setStroke(Color.WHITE);

                game.setGamePiece(blankPiece, col, row);
            }
        }

        // Draw GamePieces.
        Stream.of(game.getGameBoard())
                .flatMap(Stream::of)
                .filter(Objects::nonNull)
                .forEach(gamePiece -> gameBoard.getChildren().add(gamePiece));

        // Initialize marker container.
        markerContainer.maxWidthProperty().bind(gameBoard.widthProperty());

        // Define marker locations
        DoubleBinding offset = cellSizeBinding.divide(2);
        for (var column : range(game.getColumnCount())) {
            double multiplier = ((double) column) / game.getColumnCount();

            markerBindings[column] = markerContainer.widthProperty()
                    .multiply(multiplier).add(offset);
        }

        // Initialize marker.
        marker = new GamePiece();
        marker.layoutXProperty().bind(markerBindings[game.getSelectedColumn()]);
        marker.layoutYProperty().bind(markerContainer.heightProperty().divide(2));
        marker.radiusProperty().bind(radiusBinding);
        marker.setFill(game.getCurrentPlayer().getColor());

        markerContainer.getChildren().add(marker);

        // Set up key listeners.
        markerContainer.setOnMouseMoved(event -> updateMarkerPosition(event.getX()));
        gameBoard.setOnMouseMoved(event -> updateMarkerPosition(event.getX()));
        root.setOnMouseClicked(event -> placePiece());
    }

    private boolean checkForWin() {
        var lastMove = game.getLastMove();
        if (lastMove == null)
            return false;
        Player player = lastMove.a();
        int column = lastMove.b();
        int row = lastMove.c();

        Walker walker = new Walker(game, player, new Position(column, row));
        Position[] positions = walker.findWinPosition();

        if (positions == null)
            return false;

        winningPositions = positions;
        return true;
    }

    private boolean checkForTie() {
        return game.getColumnCount() * game.getRowCount() == game.getMoveCount();
    }

    private void gameWon() {
        game.gameOver();
    }

    private void gameTie() {
        game.gameOver();
    }

    private void updateMarkerPosition(double mouseXPos) {
        if (!game.getActive())
            return;

        double columnWidth = gameBoard.getMaxWidth() / game.getColumnCount();
        int mouseColumn = (int) Math.floor(mouseXPos / columnWidth);

        // If the marker is already there, don't move it.
        if (mouseColumn == game.getSelectedColumn())
            return;

        moveMarkerToIndex(mouseColumn);
    }

    private void moveMarkerToIndex(int index) {
        if (index < 0 || index >= markerBindings.length)
            return;
        game.setSelectedColumn(index);
        updateMarkerBindings();
    }

    private void updateMarkerBindings() {
        marker.layoutXProperty().bind(markerBindings[game.getSelectedColumn()]);
    }

    private void placePiece() {
        if (!game.getActive() && !game.isGameOver()) {
            SceneManager.popup("Please return to the current move.");
            return;
        }
        if (game.isGameOver())
            return;

        final int column = game.getSelectedColumn();
        final int row = game.findNextFreeRow(column);

        // Column is full.
        if (row == -1)
            return;

        GamePiece selectedPiece = game.getGamePiece(column, row);
        selectedPiece.setOwner(game.getCurrentPlayer());
        game.addMoveToHistory(game.getCurrentPlayer(), column, row);

        if (checkForWin()) {
            gameWon();
            return;
        }
        if (checkForTie()) {
            gameTie();
            return;
        }

        game.swapTurns();
        marker.setOwner(game.getCurrentPlayer());
    }

    // Update gameBoard size because the GridPane doesn't resize automatically
    // when circles are added.
    private void handleWindowSizeChanged(Dimension2D containerDimensions) {
        if (containerDimensions == null)
            return;
        final double size = Math.min(
                containerDimensions.getWidth() / game.getColumnCount(),
                containerDimensions.getHeight() / game.getRowCount());
        gameBoard.setMaxSize(size * game.getColumnCount(), size * game.getRowCount());
    }

    @FXML
    private void handleBackButton() {
        game.moveHistoryPointerBack();
    }

    @FXML
    private void handleForwardButton() {
        game.moveHistoryPointerForward();
    }

    @FXML
    private void handleCurrentMoveButton() {
        while (!game.historyPointerIsAtLatestMove()) {
            game.moveHistoryPointerForward();
        }
    }

    @FXML
    private void handleReturnToStartScreen() {
        SceneManager.switchScenes("Start", Duration.millis(400));
    }

    @FXML
    private void handleRestartGame() {
        SceneManager.switchScenes("Game", Duration.millis(400));
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
