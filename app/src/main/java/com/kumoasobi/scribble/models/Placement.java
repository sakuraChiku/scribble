package com.kumoasobi.scribble.models;

/**
 * Includes the tile, the row, and the column information
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Placement {
    private final Tile tile;
    private final int row;
    private final int col;

    public Placement(Tile tile, int row, int col) {
        this.tile = tile;
        this.row = row;
        this.col = col;
    }

    public Tile getTile() {
        return tile;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
