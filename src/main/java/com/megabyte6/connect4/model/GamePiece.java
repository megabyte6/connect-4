package com.megabyte6.connect4.model;

import javafx.scene.shape.Circle;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.ToString;

@ToString(callSuper = true, includeFieldNames = true)
@EqualsAndHashCode(callSuper = true)
public class GamePiece extends Circle {

    private Player owner;

    public GamePiece() {
        this(0, 0, 5);
    }

    public GamePiece(int x, int y, double radius) {
        this(Player.NONE, x, y, radius);
    }

    public GamePiece(@NonNull Player owner, int x, int y, double radius) {
        this.owner = owner;
        setX(x);
        setY(y);
        setRadius(radius);

        setFill(owner.getColor());
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(@NonNull Player owner) {
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

}
