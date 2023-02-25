package com.megabyte6.connect4.model;

import javafx.scene.shape.Circle;

public class GamePiece extends Circle {

    private Player owner;

    public GamePiece() {
        this(0, 0, 5);
    }

    public GamePiece(int x, int y, double radius) {
        this(Player.NONE, x, y, radius);
    }

    public GamePiece(Player owner, int x, int y, double radius) {
        this.owner = owner;
        setX(x);
        setY(y);
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
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((owner == null) ? 0 : owner.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        GamePiece other = (GamePiece) obj;
        if (owner == null) {
            if (other.owner != null)
                return false;
        } else if (!owner.equals(other.owner))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "GamePiece [owner=" + owner + "]";
    }

}
