package com.megabyte6.connect4.model;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class GamePiece {

    private Player owner;
    private Color color;
    private int x;
    private int y;
    private int radius;

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

    public GamePiece(Player owner, Color color, int x, int y, int radius) {
        this.owner = owner;
        this.color = color;
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    public Circle getCircle() {
        Circle circle = new Circle();
        circle.setCenterX(x);
        circle.setCenterY(y);
        circle.setRadius(radius);
        circle.setFill(color);

        return circle;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

}
