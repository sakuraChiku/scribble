package com.kumoasobi.scribble.logic;

import java.util.List;

import com.kumoasobi.scribble.models.Tile;
import com.kumoasobi.scribble.models.TileBag;

public interface DrawStrategy {
    Tile draw(TileBag bag);
    void flowback(TileBag bag, List<Tile> playerRack);
}
