package com.megabyte6.connect4.controller;

import static com.megabyte6.connect4.util.Range.range;

import com.megabyte6.connect4.model.Game;

import javafx.beans.binding.DoubleBinding;
import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.HPos;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameController implements Controller {

    private Game game = new Game("John", "James");

    private Circle marker;
    private double markerLocation;
    private double[] markerLocations;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private GridPane gameBoard;

    @FXML
    private StackPane gameBoardContainer;
    private Dimension2D containerDimensions;

    @Override
    public void initialize() {
        markerLocations = new double[gameBoard.getColumnCount()];
        for (int i : range(gameBoard.getColumnCount() + 1)) {
            // markerLocations[i] = rootPane.getWidth() * ();
        }

        // Initialize gameBoard.
        containerDimensions = new Dimension2D(
                gameBoardContainer.getWidth(), gameBoardContainer.getHeight());
        gameBoardContainer.widthProperty().addListener((observable, oldValue, newValue) -> {
            containerDimensions = new Dimension2D(
                    newValue.doubleValue(), containerDimensions.getHeight());
            updateGameBoardSize();
        });
        gameBoardContainer.heightProperty().addListener((observable, oldValue, newValue) -> {
            containerDimensions = new Dimension2D(
                    containerDimensions.getWidth(), newValue.doubleValue());
            updateGameBoardSize();
        });

        // Add circles.
        final int border = 5;
        DoubleBinding circleRadiusBinding = gameBoard.heightProperty()
                .divide(gameBoard.getRowCount()).divide(2).subtract(border);
        for (int row : range(gameBoard.getRowCount())) {
            for (int col : range(gameBoard.getColumnCount())) {
                Circle circle = new Circle();
                circle.setFill(Color.WHITE);
                circle.radiusProperty().bind(circleRadiusBinding);
                GridPane.setHalignment(circle, HPos.CENTER);

                gameBoard.add(circle, col, row);
            }
        }

        marker = new Circle();
        marker.radiusProperty().bind(circleRadiusBinding);
        marker.setFill(game.getCurrentPlayer().getColor());

        rootPane.getChildren().add(marker);
        AnchorPane.setTopAnchor(marker, 50 - marker.getRadius());
        AnchorPane.setLeftAnchor(marker, (rootPane.getWidth() / 2) - marker.getRadius());
    }

    // Update gameBoard size because the GridPane doesn't resize automatically
    // when circles are added.
    private void updateGameBoardSize() {
        final double size = Math.min(
                containerDimensions.getWidth() / gameBoard.getColumnCount(),
                containerDimensions.getHeight() / gameBoard.getRowCount());
        gameBoard.setMaxSize(size * gameBoard.getColumnCount(), size * gameBoard.getRowCount());

        AnchorPane.setTopAnchor(marker, 50 - marker.getRadius());
        AnchorPane.setLeftAnchor(marker, (rootPane.getWidth() / 2) - marker.getRadius());
    }

}
