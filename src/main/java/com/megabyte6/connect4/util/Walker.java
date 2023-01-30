package com.megabyte6.connect4.util;

import static com.megabyte6.connect4.util.tuple.Tuple.of;

import java.util.LinkedList;
import java.util.List;

import com.megabyte6.connect4.App;
import com.megabyte6.connect4.model.Game;
import com.megabyte6.connect4.model.Player;
import com.megabyte6.connect4.util.tuple.Pair;

public class Walker {

    enum Direction {
        UP,
        UPPER_RIGHT,
        RIGHT,
        LOWER_RIGHT,
        DOWN,
        LOWER_LEFT,
        LEFT,
        UPPER_LEFT
    }

    private final Game game;
    private final Player player;
    private final Position startingPos;

    public Walker(Game game, Player player, Position startingPos) {
        this.game = game;
        this.player = player;
        this.startingPos = startingPos;
    }

    /**
     * @return {@code null} if there was no win or an array of Positions if
     * there was.
     */
    public Position[] findWinPosition() {
        final LinkedList<Pair<Direction, Position>> queue = new LinkedList<>();
        // Add initial 3x3 grid of cells around the starting position.
        queue.add(of(Direction.UP,
                new Position(startingPos.column(), startingPos.row() - 1)));
        queue.add(of(Direction.UPPER_RIGHT,
                new Position(startingPos.column() + 1, startingPos.row() - 1)));
        queue.add(of(Direction.RIGHT,
                new Position(startingPos.column() + 1, startingPos.row())));
        queue.add(of(Direction.LOWER_RIGHT,
                new Position(startingPos.column() + 1, startingPos.row() + 1)));
        queue.add(of(Direction.DOWN,
                new Position(startingPos.column(), startingPos.row() + 1)));
        queue.add(of(Direction.LOWER_LEFT,
                new Position(startingPos.column() - 1, startingPos.row() + 1)));
        queue.add(of(Direction.LEFT,
                new Position(startingPos.column() - 1, startingPos.row())));
        queue.add(of(Direction.UPPER_LEFT,
                new Position(startingPos.column() - 1, startingPos.row() - 1)));

        final List<Position> horizontal = new LinkedList<>();
        horizontal.add(startingPos);
        final List<Position> vertical = new LinkedList<>();
        vertical.add(startingPos);
        final List<Position> ascendingDiagonal = new LinkedList<>();
        ascendingDiagonal.add(startingPos);
        final List<Position> descendingDiagonal = new LinkedList<>();
        descendingDiagonal.add(startingPos);

        while (queue.size() > 0) {
            final var queueElement = queue.get(0);
            final var direction = queueElement.a();
            final var pos = queueElement.b();

            queue.remove(0);

            if (game.isOutOfBounds(pos.column(), pos.row()))
                continue;

            final var gamePiece = game.getGamePiece(pos.column(), pos.row());
            if (!gamePiece.getOwner().equals(player))
                continue;

            switch (direction) {
                case UP -> {
                    queue.add(of(direction, new Position(pos.column(), pos.row() - 1)));
                    vertical.add(pos);
                }
                case UPPER_RIGHT -> {
                    queue.add(of(direction, new Position(pos.column() + 1, pos.row() - 1)));
                    ascendingDiagonal.add(pos);
                }
                case RIGHT -> {
                    queue.add(of(direction, new Position(pos.column() + 1, pos.row())));
                    horizontal.add(pos);
                }
                case LOWER_RIGHT -> {
                    queue.add(of(direction, new Position(pos.column() + 1, pos.row() + 1)));
                    descendingDiagonal.add(pos);
                }
                case DOWN -> {
                    queue.add(of(direction, new Position(pos.column(), pos.row() + 1)));
                    vertical.add(pos);
                }
                case LOWER_LEFT -> {
                    queue.add(of(direction, new Position(pos.column() - 1, pos.row() + 1)));
                    ascendingDiagonal.add(pos);
                }
                case LEFT -> {
                    queue.add(of(direction, new Position(pos.column() - 1, pos.row())));
                    horizontal.add(pos);
                }
                case UPPER_LEFT -> {
                    queue.add(of(direction, new Position(pos.column() - 1, pos.row() - 1)));
                    descendingDiagonal.add(pos);
                }
            }

            if (horizontal.size() == App.WIN_REQUIREMENT)
                return horizontal.toArray(Position[]::new);
            if (vertical.size() == App.WIN_REQUIREMENT)
                return vertical.toArray(Position[]::new);
            if (ascendingDiagonal.size() == App.WIN_REQUIREMENT)
                return ascendingDiagonal.toArray(Position[]::new);
            if (descendingDiagonal.size() == App.WIN_REQUIREMENT)
                return descendingDiagonal.toArray(Position[]::new);
        }

        return null;
    }

}
