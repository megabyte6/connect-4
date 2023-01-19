package com.megabyte6.connect4.controller;

import static com.megabyte6.connect4.util.Range.range;

import javafx.fxml.FXML;
import javafx.geometry.Dimension2D;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GameController implements Controller {

    @FXML
    private GridPane gameBoard;

    @FXML
    private StackPane gameBoardContainer;
    private Dimension2D containerDimensions;

    @Override
    public void initialize() {
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
        for (int row : range(gameBoard.getRowCount())) {
            for (int col : range(gameBoard.getColumnCount())) {
                Circle circle = new Circle();
                circle.setFill(Color.WHITE);
                circle.radiusProperty().bind(gameBoard.heightProperty()
                        .divide(gameBoard.getRowCount()).divide(2).subtract(border));
                GridPane.setHalignment(circle, HPos.CENTER);
                GridPane.setValignment(circle, VPos.CENTER);
                gameBoard.add(circle, col, row);
            }
        }
    }

    // Update gameBoard size because the GridPane doesn't resize automatically
    // when circles are added.
    public void updateGameBoardSize() {
        final double size = Math.min(
                containerDimensions.getWidth() / gameBoard.getColumnCount(),
                containerDimensions.getHeight() / gameBoard.getRowCount());
        gameBoard.setMaxSize(size * gameBoard.getColumnCount(), size * gameBoard.getRowCount());
    }

}
