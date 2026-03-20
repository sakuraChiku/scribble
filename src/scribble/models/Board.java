package scribble.models;

import java.io.Serializable;
import java.util.List;

public class Board implements Serializable{
    Cell[][] grid;
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
        grid = new Cell[15][15];

        /**
         * SET THE BONUS CELLS
         */
        for (int i = 0; i < 15; i++) {
            for (int j = 0; j < 15; j++) {
                grid[i][j] = new Cell(BONUS_MAP[i][j]);
            }
        }
    }

    public boolean hasTile(int row, int col) {
        return grid[row-1][col-1].isPlaced();
    }

    public Tile getTile(int row, int col) {
        return grid[row-1][col-1].getTile();
    }

    public int getScore(int row, int col) {
        return grid[row-1][col-1].getTile().getScore();
    }

    public void placeMove(Move move) {
        List<Placement> placements = move.getPlacements();
        for (Placement p : placements) {
            grid[p.row-1][p.col-1].placeTile(p.tile);
        }
    }
}
