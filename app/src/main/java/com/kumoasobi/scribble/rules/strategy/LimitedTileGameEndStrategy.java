package com.kumoasobi.scribble.rules.strategy;

import com.kumoasobi.scribble.models.GameState;

public class LimitedTileGameEndStrategy implements GameEndStrategy {
    @Override
    public boolean isGameOver(GameState gs) {
        return gs.getBag().tilesRemaining() == 0;
    }
}
