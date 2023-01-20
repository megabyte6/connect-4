package com.megabyte6.connect4.model;

import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class GamePiece extends Circle {

    private Player owner;
    private int column, row;

    private GridPane gameBoard = null;

    public GamePiece() {
        this(0, 0, 5);
    }

    public GamePiece(int column, int row, double radius) {
        this(Player.NONE, column, row, radius);
    }

    public GamePiece(Player owner, int column, int row, double radius) {
        this.owner = owner;
        this.column = column;
        this.row = row;
        setRadius(radius);

        setFill(owner.getColor());
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;

        this.setFill(owner.getColor());
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;

        if (gameBoard != null) {
            gameBoard.add(gameBoard, this.column, this.row);
        }
    }

    public int getRow() {
        return row;
    }

    public void setRow(int row) {
        this.row = row;

        if (gameBoard != null) {
            gameBoard.add(gameBoard, this.column, this.row);
        }
    }

    public double getX() {
        return getCenterX();
    }

    public void setX(double x) {
        setCenterX(x);
    }

    public double getY() {
        return getCenterY();
    }

    public void setY(double y) {
        setCenterY(y);
    }

    public void setGameBoard(GridPane gameBoard) {
        this.gameBoard = gameBoard;
    }

    public void addToGameBoard(GridPane gameBoard) {
        gameBoard.add(gameBoard, column, row);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj instanceof GamePiece other) {
            return owner.equals(other.owner)
                    && getCenterX() == other.getCenterX()
                    && getCenterY() == other.getCenterY()
                    && getRadius() == other.getRadius();
        }
        return false;
    }

}
