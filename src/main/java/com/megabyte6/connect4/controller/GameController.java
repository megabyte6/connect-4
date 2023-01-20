package com.megabyte6.connect4.controller;

import static com.megabyte6.connect4.util.Range.range;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.Game;
import com.megabyte6.connect4.model.GamePiece;

import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.HPos;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;

public class GameController {

    private Game game = new Game("John", "James");

    private Circle marker;
    private double markerLocation;
    private double[] markerLocations;

    @FXML
    private BorderPane rootPane;

    @FXML
    private GridPane gameBoard;
    private Dimension2D gameBoardContainerDimensions;

    @FXML
    private void initialize() {
        markerLocations = new double[gameBoard.getColumnCount()];
        for (int i : range(gameBoard.getColumnCount() + 1)) {
            // markerLocations[i] = rootPane.getWidth() * ();
        }

        // Initialize gameBoard.
        StackPane gameBoardContainer = (StackPane) gameBoard.getParent();
        gameBoardContainerDimensions = new Dimension2D(
                gameBoardContainer.getWidth(), gameBoardContainer.getHeight());
        gameBoardContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            gameBoardContainerDimensions = new Dimension2D(
                    newValue.doubleValue(), gameBoardContainerDimensions.getHeight());
            updateGameBoardSize();
        });
        gameBoardContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            gameBoardContainerDimensions = new Dimension2D(
                    gameBoardContainerDimensions.getWidth(), newValue.doubleValue());
            updateGameBoardSize();
        });

        // Add circles.
        final int border = 5;
        DoubleBinding circleRadiusBinding = gameBoard.heightProperty()
                .divide(gameBoard.getRowCount()).divide(2).subtract(border);
        for (int row : range(gameBoard.getRowCount())) {
            for (int col : range(gameBoard.getColumnCount())) {
                GamePiece blankPiece = new GamePiece();
                blankPiece.setColumn(col);
                blankPiece.setRow(row);
                blankPiece.setFill(App.BACKGROUND_COLOR);
                blankPiece.radiusProperty().bind(circleRadiusBinding);
                GridPane.setHalignment(blankPiece, HPos.CENTER);
                blankPiece.addToGameBoard(gameBoard);
            }
        }

        // Initialize marker.
        marker = new GamePiece();
        marker.radiusProperty().bind(circleRadiusBinding);
        marker.setFill(game.getCurrentPlayer().getColor());

        rootPane.setTop(marker);
        marker.setTranslateX((rootPane.getWidth() / 2) - marker.getRadius()
                - rootPane.getPadding().getLeft());
    }

    // Update gameBoard size because the GridPane doesn't resize automatically
    // when circles are added.
    private void updateGameBoardSize() {
        final double size = Math.min(
                gameBoardContainerDimensions.getWidth() / gameBoard.getColumnCount(),
                gameBoardContainerDimensions.getHeight() / gameBoard.getRowCount());
        gameBoard.setMaxSize(size * gameBoard.getColumnCount(), size * gameBoard.getRowCount());

        marker.setTranslateX((rootPane.getWidth() / 2) - marker.getRadius()
                - rootPane.getPadding().getLeft());
    }

}
