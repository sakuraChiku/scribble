package scribble.models;

import java.io.Serializable;

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

    public boolean hasTile(int row, int col) {
        checkBounds(row, col);
        return grid[row-1][col-1].isPlaced();
    }

    public Tile getTile(int row, int col) {
        checkBounds(row, col);
        return grid[row-1][col-1].getTile();
    }

    public int getScore(int row, int col) {
        checkBounds(row, col);
        return grid[row-1][col-1].getTile().getScore();
    }

    public void placeMove(Move move) {
        for (Placement p : move.getPlacements()) {
            checkBounds(p.getRow(), p.getCol());
            Cell cell = grid[p.getRow()-1][p.getCol()-1];
            if (cell.isPlaced()) {
                throw new IllegalStateException("Cell already occupied at (" + p.getRow() + ", " + p.getCol() + ")");
            }
            cell.placeTile(p.getTile());
        }
    }

    public Cell getCell(int row, int col) {
        checkBounds(row, col);
        return grid[row-1][col-1];
    }

    private void checkBounds(int row, int col) {
        if (row < 1 || row > SIZE || col < 1 || col > SIZE) {
            throw new IndexOutOfBoundsException("Invalid board position: (" + row + ", " + col + ")");
        }
    }
}
