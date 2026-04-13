package com.kumoasobi.scribble.models;

import java.io.Serializable;

/**
 * Create a 15 x 15 board, cells can be put on it.
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Board implements Serializable {
    private static final int SIZE = 15;
    private final Cell[][] grid;
    private final static BonusType[][] BONUS_MAP = {
        {BonusType.TW, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.TW},
        {BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE},
        {BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE},
        {BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.DL},
        {BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE},
        {BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE},
        {BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE},
        {BonusType.TW, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.TW},
        {BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE},
        {BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE},
        {BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.NONE},
        {BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.DL},
        {BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE},
        {BonusType.NONE, BonusType.DW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DW, BonusType.NONE},
        {BonusType.TW, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.TW, BonusType.NONE, BonusType.NONE, BonusType.NONE, BonusType.DL, BonusType.NONE, BonusType.NONE, BonusType.TW},
    };

    public Board() {
        // initialize the blank board
        grid = new Cell[SIZE][SIZE];

        /**
         * SET THE BONUS CELLS
         */
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                grid[i][j] = new Cell(BONUS_MAP[i][j]);
            }
        }
    }

    public Cell getCell(int row, int col) {
        //checkBounds(row, col);
        return grid[row-1][col-1];
    }

    public static int getSIZE() {
        return SIZE;
    }

    public boolean hasTile(int row, int col) {
        //checkBounds(row, col);
        return grid[row-1][col-1].isPlaced();
    }

    public Tile getTile(int row, int col) {
        //checkBounds(row, col);
        return grid[row-1][col-1].getTile();
    }

    public void placeMove(Move move) {
        for (Placement p : move.getPlacements()) {
            //checkBounds(p.getRow(), p.getCol());
            Cell cell = grid[p.getRow()-1][p.getCol()-1];
            cell.placeTile(p.getTile());
        }
    }

    public void recallMove(Move move) {
        for (Placement p : move.getPlacements()) {
            //checkBounds(p.getRow(), p.getCol());
            Cell cell = grid[p.getRow()-1][p.getCol()-1];
            cell.removeTile();
        }
    }

    public boolean hasAdjacentTile(int row, int col) {
        boolean left = hasTile(row, col-1);
        boolean right = hasTile(row, col+1);
        boolean up = hasTile(row-1, col);
        boolean down = hasTile(row+1, col);
        return left || right || up || down;
    }

    /* private void checkBounds(int row, int col) throws CellOutOfBoundException{
        if (row < 1 || row > SIZE || col < 1 || col > SIZE) {
            throw new CellOutOfBoundException("Invalid board position: (" + row + ", " + col + ")");
        }
    } */
}
