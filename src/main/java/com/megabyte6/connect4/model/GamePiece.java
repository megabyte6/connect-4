package com.megabyte6.connect4.model;

import javafx.scene.shape.Circle;

public class GamePiece extends Circle {

    private Player owner;
    private int x, y;

    public GamePiece() {
        this(0, 0, 5);
    }

    public GamePiece(int x, int y, double radius) {
        this(Player.NONE, x, y, radius);
    }

    public GamePiece(Player owner, int x, int y, double radius) {
        this.owner = owner;
        this.x = x;
        this.y = y;
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

    @Override
    public String toString() {
        return "GamePiece[" +
                "owner=" + owner + ", " +
                "column=" + x + ", " +
                "row=" + y + ", " +
                "x=" + getCenterX() + ", " +
                "y=" + getCenterY() + ", " +
                "radius=" + getRadius() +
                "]";
    }

}
