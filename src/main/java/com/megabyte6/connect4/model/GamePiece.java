package com.megabyte6.connect4.model;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

public class GamePiece extends Circle {

    private Player owner;

    public GamePiece() {
        this(0, 0, 5);
    }

    public GamePiece(int x, int y, int radius) {
        this(new Player(), x, y, radius);
    }

    public GamePiece(Color color, int x, int y, int radius) {
        this(new Player(), color, x, y, radius);
    }

    public GamePiece(Player owner, int x, int y, int radius) {
        this(owner, owner.getColor(), x, y, radius);
    }

    public GamePiece(Player owner, Color color, double x, double y, double radius) {
        this.owner = owner;
        setColor(color);
        setX(x);
        setY(y);
        setRadius(radius);
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Paint getColor() {
        return this.getFill();
    }

    public void setColor(Paint color) {
        setFill(color);
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

}
