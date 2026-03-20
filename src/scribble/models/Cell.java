package scribble.models;

public class Cell {
    Tile tile;
    BonusType bonus;

    public Cell(Tile tile, BonusType bonus) {
        this.tile = tile;
        this.bonus = bonus;
    }
}
