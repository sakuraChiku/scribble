package com.kumoasobi.scribble.rules.validator;

import com.kumoasobi.scribble.exceptions.CellOccupiedException;
import com.kumoasobi.scribble.exceptions.EmptyMoveException;
import com.kumoasobi.scribble.exceptions.FirstMoveNotThroughCenter;
import com.kumoasobi.scribble.exceptions.FirstMoveOnlyOneWordException;
import com.kumoasobi.scribble.exceptions.GameException;
import com.kumoasobi.scribble.exceptions.MoveNotContinuousException;
import com.kumoasobi.scribble.exceptions.MoveNotInLineException;
import com.kumoasobi.scribble.exceptions.WordNotConnectedToExistingTile;
import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.Direction;
import com.kumoasobi.scribble.models.Move;
import com.kumoasobi.scribble.models.Placement;

/**
 * Provide 5 methods to validate the structure and the location of a move
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class BoardValidator {
    public static void validateBoard(Move move, Board board) throws GameException {
        haveOccupied(move, board);
        validateStructure(move);
        validateDirection(move, board, move.getDirection());
        if (isFirstStep(board)) {
            oneLetter(move);
            throughCenter(move);
        } else {
            connectToWords(move, board);
        }
    }
    /**
     * First, validate if the move have occupied existing cells
     * @param move
     * @param board
     * @throws CellOccupiedException
     */
    private static void haveOccupied(Move move, Board board) throws CellOccupiedException {
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
     * @throws GameException
     */
    private static void validateStructure(Move move) throws GameException {
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
    private static void validateDirection(Move move, Board board, Direction direction) throws GameException {
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
    private static void connectToWords(Move move, Board board) throws GameException {
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
    private static void throughCenter(Move move) throws GameException {
        for (Placement p : move.getPlacements()) {
            if ((p.getRow() == (1+Board.getSIZE())/2) && (p.getCol() == (1+Board.getSIZE())/2)) {
                return;
            }
        }
        throw new FirstMoveNotThroughCenter("First move must go through the center!");
    }

    private static void oneLetter(Move move) throws GameException {
        if (move.getPlacements().size() == 1) throw new FirstMoveOnlyOneWordException("Please place at least two letters in the first step!");
    }

    private static boolean isFirstStep(Board board) {
        for (int i = 1; i <= Board.getSIZE(); i++) {
            for (int j = 1; j <= Board.getSIZE(); j++) {
                if (board.hasTile(i, j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
