package com.kumoasobi.scribble.models;

import java.io.Serializable;

/**
 * Define the cell which includes the tile and the bonus type
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Cell implements Serializable {
    private Tile tile;
    private final BonusType bonus;

    public Cell(BonusType bonus) {
        this.tile = null;
        this.bonus = bonus;
    }

    public void placeTile(Tile tile) {
        this.tile = tile;
    }

    public void removeTile() {
        this.tile = null;
    }

    public Tile getTile() {
        return tile;
    }

    public BonusType getBonus() {
        return bonus;
    }

    public boolean isPlaced() {
        return tile != null;
    }
}
