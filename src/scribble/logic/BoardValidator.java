package scribble.logic;

import scribble.exceptions.EmptyMoveException;
import scribble.exceptions.GameException;
import scribble.exceptions.MoveNotContinuousException;
import scribble.exceptions.MoveNotInLineException;
import scribble.models.Board;
import scribble.models.Direction;
import scribble.models.Move;

public class BoardValidator {
    public void structureValidator(Move move, Board board) throws GameException {
        if (move.isEmpty())
            throw new EmptyMoveException("The move cannot be empty!");
        if (!move.isSameCol() || !move.isSameRow()) {
            throw new MoveNotInLineException("The move must be in a line!");
        }

        if (move.getPlacements().size() == 1) {
            return;
        } else if (move.isSameCol()) {
            validateDirection(move, board, Direction.VERTICAL);
        } else if (move.isSameRow()) {
            validateDirection(move, board, Direction.HORIZONTAL);
        }
    }

    public void validateDirection(Move move, Board board, Direction direction) throws GameException {
        switch (direction) {
            case HORIZONTAL: {
                for (int col = move.getMinCol(); col <= move.getMaxCol(); col++) {
                    if (!move.hasPlacement(move.getSameRow(), col) && !board.hasTile(move.getSameRow(), col)) {
                        throw new MoveNotContinuousException("The move is not continuous (" + move.getSameRow() + ", " + col + ")");
                    }
                }
            }

            case VERTICAL: {
                for (int row = move.getMinRow(); row <= move.getMaxRow(); row++) {
                    if (!move.hasPlacement(row, move.getSameCol()) && !board.hasTile(row, move.getSameCol())) {
                        throw new MoveNotContinuousException("The move is not continuous at (" + row + ", " + move.getSameCol() + ")");
                    }
                }
            }
        }
    }
}
