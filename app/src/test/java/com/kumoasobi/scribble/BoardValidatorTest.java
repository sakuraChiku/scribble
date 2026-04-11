package com.kumoasobi.scribble;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.kumoasobi.scribble.exceptions.CellOccupiedException;
import com.kumoasobi.scribble.exceptions.EmptyMoveException;
import com.kumoasobi.scribble.exceptions.FirstMoveNotThroughCenter;
import com.kumoasobi.scribble.exceptions.MoveNotContinuousException;
import com.kumoasobi.scribble.exceptions.MoveNotInLineException;
import com.kumoasobi.scribble.exceptions.WordNotConnectedToExistingTile;
import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.Direction;
import com.kumoasobi.scribble.models.Move;
import com.kumoasobi.scribble.models.Placement;
import com.kumoasobi.scribble.models.Tile;
import com.kumoasobi.scribble.rules.validator.BoardValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class BoardValidatorTest {

    private Board board;
    private BoardValidator validator;
    private Tile tileA;

    @BeforeEach
    void setup() {
        board = new Board();
        validator = new BoardValidator();
        tileA = new Tile('A', 1);
    }

    @Test
    void haveOccupied_shouldThrowWhenCellAlreadyHasTile() {
        board.placeMove(createMove(8, 8));
        Move move = createMove(8, 8);

        assertThrows(CellOccupiedException.class, () -> validator.haveOccupied(move, board));
    }

    @Test
    void haveOccupied_shouldNotThrowWhenCellIsEmpty() {
        Move move = createMove(5, 5);

        assertDoesNotThrow(() -> validator.haveOccupied(move, board));
    }

    @Test
    void structureValidator_shouldThrowOnEmptyMove() {
        Move move = new Move();

        assertThrows(EmptyMoveException.class, () -> validator.structureValidator(move, board));
    }

    @Test
    void structureValidator_shouldThrowOnNonLinearMove() {
        Move move = new Move();
        move.addPlacement(new Placement(tileA, 7, 7));
        move.addPlacement(new Placement(tileA, 8, 8));

        assertThrows(MoveNotInLineException.class, () -> validator.structureValidator(move, board));
    }

    @Test
    void structureValidator_shouldNotThrowOnValidLinearMove() {
        Move move = new Move();
        move.addPlacement(new Placement(tileA, 7, 7));
        move.addPlacement(new Placement(tileA, 7, 8));
        move.sameRow();

        assertDoesNotThrow(() -> validator.structureValidator(move, board));
        assertEquals(Direction.HORIZONTAL, move.getDirection());
    }

    @Test
    void validateDirection_shouldThrowWhenMoveNotContinuousAndNoBridgingTile() {
        Move move = new Move();
        move.addPlacement(new Placement(tileA, 6, 6));
        move.addPlacement(new Placement(tileA, 6, 8));
        move.sameRow();

        assertThrows(MoveNotContinuousException.class, () -> validator.validateDirection(move, board, move.getDirection()));
    }

    @Test
    void validateDirection_shouldNotThrowWhenMoveIsBridgedByExistingTile() {
        board.placeMove(createMove(6, 7));

        Move move = new Move();
        move.addPlacement(new Placement(tileA, 6, 6));
        move.addPlacement(new Placement(tileA, 6, 8));
        move.sameRow();

        assertDoesNotThrow(() -> validator.validateDirection(move, board, move.getDirection()));
    }

    @Test
    void connectToWords_shouldThrowWhenNoAdjacentTile() {
        Move move = createMove(3, 3);

        assertThrows(WordNotConnectedToExistingTile.class, () -> validator.connectToWords(move, board));
    }

    @Test
    void connectToWords_shouldNotThrowWhenHasAdjacentTile() {
        board.placeMove(createMove(5, 5));
        Move move = createMove(5, 6);

        assertDoesNotThrow(() -> validator.connectToWords(move, board));
    }

    @Test
    void throughCenter_shouldThrowWhenNotThroughCenter() {
        Move move = createMove(1, 1);

        assertThrows(FirstMoveNotThroughCenter.class, () -> validator.throughCenter(move, board));
    }

    @Test
    void throughCenter_shouldNotThrowWhenThroughCenter() {
        Move move = createMove(8, 8);

        assertDoesNotThrow(() -> validator.throughCenter(move, board));
    }

    private Move createMove(int row, int col) {
        Move m = new Move();
        m.addPlacement(new Placement(tileA, row, col));
        return m;
    }
}
