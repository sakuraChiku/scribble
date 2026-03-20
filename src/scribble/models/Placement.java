package scribble.models;

public class Placement {
    Tile tile;
    int row;
    int col;

    public Placement(Tile tile, int row, int col) {
        this.tile = tile;
        this.row = row;
        this.col = col;
    }
}
