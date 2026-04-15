package com.kumoasobi.scribble.rules.strategy;

import java.io.Serializable;
import java.util.List;

import com.kumoasobi.scribble.models.Tile;
import com.kumoasobi.scribble.models.TileBag;

public class LimitedDrawStrategy implements DrawStrategy, Serializable {
    @Override
    public Tile draw(TileBag bag) {
        return bag.getLetterPool().removeFirst();
    }

    @Override
    public void flowback(TileBag bag, List<Tile> playerRack) {
        for (Tile playerTile : playerRack) {
            bag.getLetterPool().add(playerTile);
        }
    }
}
