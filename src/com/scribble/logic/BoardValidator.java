package com.scribble.logic;

import com.scribble.exceptions.CellOccupiedException;
import com.scribble.exceptions.EmptyMoveException;
import com.scribble.exceptions.FirstMoveNotThroughCenter;
import com.scribble.exceptions.GameException;
import com.scribble.exceptions.MoveNotContinuousException;
import com.scribble.exceptions.MoveNotInLineException;
import com.scribble.exceptions.WordNotConnectedToExistingTile;
import com.scribble.models.Board;
import com.scribble.models.Direction;
import com.scribble.models.Move;
import com.scribble.models.Placement;

/**
 * Provide 5 methods to validate the structure and the location of a move
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class BoardValidator {
    /**
     * First, validate if the move have occupied existing cells
     * @param move
     * @param board
     * @throws CellOccupiedException
     */
    public void haveOccupied(Move move, Board board) throws CellOccupiedException {
        for (Placement p : move.getPlacements()) {
            if (board.getCell(p.getRow(), p.getCol()).isPlaced()) {
                throw new CellOccupiedException("Cell already occupied at (" + p.getRow() + ", " + p.getCol() + ")");
            }
        }
    }

    /**
     * Second, validate the structure of the move including:
     * 1. Is the move empty?
     * 2. Is the move in a line?
     * @param move
     * @param board
     * @throws GameException
     */
    public void structureValidator(Move move, Board board) throws GameException {
        if (move.isEmpty())
            throw new EmptyMoveException("The move cannot be empty!");
        if (move.getDirection() == null) {
            throw new MoveNotInLineException("The move must be in a line!");
        }

        if (move.getPlacements().size() == 1) {
        }
    }

    /**
     * Third, validate if the move is continuous
     * @param move
     * @param board
     * @param direction
     * @throws GameException
     */
    public void validateDirection(Move move, Board board, Direction direction) throws GameException {
        switch (direction) {
            case HORIZONTAL -> {
                for (int col = move.getMinCol(); col <= move.getMaxCol(); col++) {
                    if (!move.hasPlacement(move.getSameRow(), col) && !board.hasTile(move.getSameRow(), col)) {
                        throw new MoveNotContinuousException("The move is not continuous (" + move.getSameRow() + ", " + col + ")");
                    }
                }
            }

            case VERTICAL -> {
                for (int row = move.getMinRow(); row <= move.getMaxRow(); row++) {
                    if (!move.hasPlacement(row, move.getSameCol()) && !board.hasTile(row, move.getSameCol())) {
                        throw new MoveNotContinuousException("The move is not continuous at (" + row + ", " + move.getSameCol() + ")");
                    }
                }
            }
        }
    }
    
    /**
     * If the move is not the first step, validate if the word is connected to an existing tile.
     * @param move
     * @param board
     * @throws GameException
     */
    public void connectToWords(Move move, Board board) throws GameException {
        for (Placement p : move.getPlacements()) {
            if (board.hasAdjacentTile(p.getRow(), p.getCol())) {
                return;
            }
        }
        throw new WordNotConnectedToExistingTile("The word must be connected to an existing tile!");
    }

    /**
     * If the move is the first step, validate if the word goes through center
     * @param move
     * @param board
     * @throws GameException
     */
    public void throughCenter(Move move, Board board) throws GameException {
        for (Placement p : move.getPlacements()) {
            if ((p.getRow() == (1+Board.getSIZE())/2) && (p.getCol() == (1+Board.getSIZE())/2)) {
                return;
            }
        }
        throw new FirstMoveNotThroughCenter("First move must go through the center!");
    }
}
