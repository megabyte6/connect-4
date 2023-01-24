package com.megabyte6.connect4.controller;

import static com.megabyte6.connect4.util.Range.range;

import java.util.stream.Stream;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.Game;
import com.megabyte6.connect4.model.GamePiece;

import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

public class GameController {

    private Game game = new Game("John", "James");

    @FXML
    private AnchorPane root;

    @FXML
    private Pane markerContainer;

    private GamePiece marker;
    private int markerBindingIndex = game.getColumnCount() / 2;
    private final DoubleBinding[] markerBindings = new DoubleBinding[game.getColumnCount()];
    private final Line[] columnSeparators = new Line[game.getColumnCount() - 1];

    @FXML
    private Pane gameBoard;
    private Dimension2D gameBoardContainerDimensions;

    @FXML
    private void initialize() {
        // Initialize gameBoard width and height.
        StackPane gameBoardContainer = (StackPane) gameBoard.getParent();
        // gameBoardContainer.setStyle("-fx-background-color: ređ");
        gameBoardContainerDimensions = new Dimension2D(
                gameBoardContainer.getWidth(), gameBoardContainer.getHeight());

        gameBoardContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            gameBoardContainerDimensions = new Dimension2D(
                    newValue.doubleValue(), gameBoardContainerDimensions.getHeight());
            handleWindowSizeChanged();
        });
        gameBoardContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            gameBoardContainerDimensions = new Dimension2D(
                    gameBoardContainerDimensions.getWidth(), newValue.doubleValue());
            handleWindowSizeChanged();
        });

        // Draw horizontal grid lines.
        for (int i : range(game.getRowCount() + 1)) {
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
        Line[] verticalLines = new Line[game.getColumnCount()];
        for (int i : range(game.getColumnCount() + 1)) {
            double multiplier = ((double) i) / game.getColumnCount();
            DoubleBinding x = gameBoard.widthProperty().multiply(multiplier);

            Line line = new Line();
            line.setStroke(Color.WHITE);
            line.startXProperty().bind(x);
            line.endXProperty().bind(x);
            line.endYProperty().bind(gameBoard.heightProperty());

            gameBoard.getChildren().add(line);

            verticalLines[i] = line;
        }

        // Populate columnSeparators.
        for (int i : range(columnSeparators.length)) {
            columnSeparators[i] = verticalLines[i + 1];
        }

        // Create GamePieces.
        DoubleBinding cellSizeBinding = gameBoard.heightProperty().divide(game.getRowCount());
        final int border = 5;
        DoubleBinding radiusBinding = cellSizeBinding.divide(2).subtract(border);
        for (double col : range(game.getColumnCount())) {
            double xMultiplier = ((double) col) / game.getColumnCount();
            DoubleBinding xOffset = cellSizeBinding.divide(2);

            DoubleBinding xBinding = gameBoard.widthProperty()
                    .multiply(xMultiplier).add(xOffset);

            for (double row : range(game.getRowCount())) {
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

                game.setGamePiece(blankPiece, (int) col, (int) row);
            }
        }

        // Draw GamePieces.
        Stream.of(game.getGameBoard())
                .flatMap(Stream::of)
                .filter(gamePiece -> gamePiece != null)
                .forEach(gamePiece -> gameBoard.getChildren().add(gamePiece));

        // Initialize marker container.
        markerContainer.maxWidthProperty().bind(gameBoard.widthProperty());

        // Define marker locations
        DoubleBinding offset = cellSizeBinding.divide(2);
        for (int column : range(game.getColumnCount())) {
            double multiplier = ((double) column) / game.getColumnCount();

            markerBindings[column] = markerContainer.widthProperty()
                    .multiply(multiplier).add(offset);
        }

        // Initialize marker.
        marker = new GamePiece();
        marker.layoutXProperty().bind(markerBindings[markerBindingIndex]);
        marker.layoutYProperty().bind(markerContainer.heightProperty().divide(2));
        marker.radiusProperty().bind(radiusBinding);
        marker.setFill(game.getCurrentPlayer().getColor());

        markerContainer.getChildren().add(marker);

        // Set up key listeners.
        root.setOnMouseMoved(event -> updateMarkerPosition(event.getSceneX()));
    }

    private void updateMarkerPosition(double mouseXPosition) {
        double absoluteMarkerX = marker.localToScene(marker.getBoundsInLocal()).getCenterX();
    }

    private void moveMarkerLeft() {
        if (markerBindingIndex == 0)
            return;
        markerBindingIndex--;
        updateMarkerBindings();
    }

    private void moveMarkerRight() {
        if (markerBindingIndex == markerBindings.length - 1)
            return;
        markerBindingIndex++;
        updateMarkerBindings();
    }

    private void moveMarkerToIndex(int index) {
        markerBindingIndex = index;
        updateMarkerBindings();
    }

    private void updateMarkerBindings() {
        marker.layoutXProperty().bind(markerBindings[markerBindingIndex]);
    }

    // Update gameBoard size because the GridPane doesn't resize automatically
    // when circles are added.
    private void handleWindowSizeChanged() {
        final double size = Math.min(
                gameBoardContainerDimensions.getWidth() / game.getColumnCount(),
                gameBoardContainerDimensions.getHeight() / game.getRowCount());
        gameBoard.setMaxSize(size * game.getColumnCount(), size * game.getRowCount());
    }

}
