package com.kumoasobi.scribble.rules.strategy;

import java.io.Serializable;
import java.util.List;
import java.util.Random;

import com.kumoasobi.scribble.models.Tile;
import com.kumoasobi.scribble.models.TileBag;

public class UnlimitedDrawStrategy implements DrawStrategy, Serializable {
    private final Random rand = new Random();
    @Override
    public Tile draw(TileBag bag) {
        int index = rand.nextInt(bag.tilesRemaining());
        return bag.getLetterPool().get(index);
    }

    @Override
    public void flowback(TileBag bag, List<Tile> playerRack) {
    }
}
