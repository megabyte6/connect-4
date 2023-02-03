package com.megabyte6.connect4.controller;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.controller.dialog.ConfirmController;
import com.megabyte6.connect4.model.Game;
import com.megabyte6.connect4.model.GamePiece;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.Position;
import com.megabyte6.connect4.util.SceneManager;
import com.megabyte6.connect4.util.Walker;
import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
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

    private final Game game = new Game(App.getPlayer1(), App.getPlayer2());
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
    private Label player1Score;
    @FXML
    private Label currentTurn;
    @FXML
    private Label player2Score;

    @FXML
    private void initialize() {
        App.setWinner(Player.NONE);

        // Initialize gameBoard width and height.
        final StackPane gameBoardContainer = (StackPane) gameBoard.getParent();

        gameBoardContainer.widthProperty().addListener((observable, oldValue, newValue) ->
                handleWindowSizeChanged(new Dimension2D(newValue.doubleValue(), gameBoardContainer.getHeight())));
        gameBoardContainer.heightProperty().addListener((observable, oldValue, newValue) ->
                handleWindowSizeChanged(new Dimension2D(gameBoardContainer.getWidth(), newValue.doubleValue())));

        // Draw horizontal grid lines.
        for (int i : range(game.getRowCount() + 1)) {
            final double multiplier = ((double) i) / game.getRowCount();
            final DoubleBinding y = gameBoard.heightProperty().multiply(multiplier);

            final Line line = new Line();
            line.setStroke(Color.WHITE);
            line.startYProperty().bind(y);
            line.endXProperty().bind(gameBoard.widthProperty());
            line.endYProperty().bind(y);

            gameBoard.getChildren().add(line);
        }
        // Draw vertical grid lines.
        for (int i : range(game.getColumnCount())) {
            final double multiplier = ((double) i) / game.getColumnCount();
            final DoubleBinding x = gameBoard.widthProperty().multiply(multiplier);

            final Line line = new Line();
            line.setStroke(Color.WHITE);
            line.startXProperty().bind(x);
            line.endXProperty().bind(x);
            line.endYProperty().bind(gameBoard.heightProperty());

            gameBoard.getChildren().add(line);
        }

        // Create GamePieces.
        final DoubleBinding cellSizeBinding = gameBoard.heightProperty().divide(game.getRowCount());
        final int border = 5;
        final DoubleBinding radiusBinding = cellSizeBinding.divide(2).subtract(border);
        for (int col : range(game.getColumnCount())) {
            final double xMultiplier = ((double) col) / game.getColumnCount();
            final DoubleBinding xOffset = cellSizeBinding.divide(2);

            final DoubleBinding xBinding = gameBoard.widthProperty()
                    .multiply(xMultiplier).add(xOffset);

            for (int row : range(game.getRowCount())) {
                final double yMultiplier = ((double) row) / game.getRowCount();
                final DoubleBinding yOffset = cellSizeBinding.divide(2);

                final DoubleBinding yBinding = gameBoard.heightProperty()
                        .multiply(yMultiplier).add(yOffset);

                final GamePiece blankPiece = new GamePiece();
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
        final DoubleBinding offset = cellSizeBinding.divide(2);
        for (int column : range(game.getColumnCount())) {
            final double multiplier = ((double) column) / game.getColumnCount();

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

        // Initialize labels.
        updatePlayerScoreLabels();
        updateCurrentTurnLabel();

        // Set up key listeners.
        markerContainer.setOnMouseMoved(event -> updateMarkerPosition(event.getX()));

        gameBoard.setOnMouseMoved(event -> updateMarkerPosition(event.getX()));

        root.setOnMouseClicked(event -> placePiece());

        root.setOnKeyPressed(event -> {
            if (event.isShortcutDown())
                switch (event.getCode()) {
                    case Q -> handleReturnToStartScreen();
                    case N -> handleNewGame();
                }
        });

        root.requestFocus();
    }

    private boolean checkForWin() {
        final var lastMove = game.getLastMove();
        if (lastMove == null)
            return false;
        final Player player = lastMove.a();
        final int column = lastMove.b();
        final int row = lastMove.c();

        final Walker walker = new Walker(game, player, new Position(column, row));
        final Position[] positions = walker.findWinPosition();

        if (positions == null)
            return false;

        App.setWinner(player);
        winningPositions = positions;
        return true;
    }

    private boolean checkForTie() {
        return game.getColumnCount() * game.getRowCount() == game.getMoveCount();
    }

    private void gameWon() {
        setDisable(true);
        game.gameOver();
        App.getWinner().incrementScore();

        final var loadedData = SceneManager.loadFXMLAndController("GameFinished");
        final Node root = loadedData.a();
        final GameFinishedController controller = (GameFinishedController) loadedData.b();
        controller.setOnClose(() -> setDisable(false));

        SceneManager.addScene(root);
    }

    private void gameTie() {
        game.gameOver();
    }

    private void updateMarkerPosition(double mouseXPos) {
        if (!game.isActive())
            return;

        final double columnWidth = gameBoard.getMaxWidth() / game.getColumnCount();
        final int mouseColumn = (int) Math.floor(mouseXPos / columnWidth);

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
        if (game.isGameOver())
            return;
        if (!game.isActive() && !game.isGameOver()) {
            SceneManager.popup("Please return to the current move.");
            return;
        }

        final int column = game.getSelectedColumn();
        final int row = game.findNextFreeRow(column);

        // Column is full.
        if (row == -1)
            return;

        final GamePiece selectedPiece = game.getGamePiece(column, row);
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

        updateCurrentTurnLabel();
    }

    private void updatePlayerScoreLabels() {
        player1Score.setText(App.getPlayer1().getName() + ": " + App.getPlayer1().getScore());
        player2Score.setText(App.getPlayer2().getName() + ": " + App.getPlayer2().getScore());
    }

    public void updateCurrentTurnLabel() {
        currentTurn.setText(game.getCurrentPlayer().getName() + "'s turn");
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
        setDisable(true);

        final var loadedData = SceneManager.loadFXMLAndController("dialog/Confirm");
        final Node root = loadedData.a();
        final ConfirmController controller = (ConfirmController) loadedData.b();

        controller.setText("Are you sure you want to leave the game?");
        controller.setOnOk(() -> SceneManager.switchScenes("Start", Duration.millis(400)));
        controller.setOnCancel(() -> setDisable(false));

        SceneManager.addScene(root);
    }

    @FXML
    private void handleNewGame() {
        setDisable(true);

        final var loadedData = SceneManager.loadFXMLAndController("dialog/Confirm");
        final Node root = loadedData.a();
        final ConfirmController controller = (ConfirmController) loadedData.b();

        controller.setText("Are you sure you want to reset the game?");
        controller.setOnOk(() -> SceneManager.switchScenes("Game", Duration.millis(400)));
        controller.setOnCancel(() -> setDisable(false));

        SceneManager.addScene(root);
    }

    @Override
    public void setDisable(boolean disabled) {
        root.setDisable(disabled);
        root.setOpacity(disabled ? App.DISABLED_OPACITY : 1);
    }
}
