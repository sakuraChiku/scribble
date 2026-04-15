package com.kumoasobi.scribble.rules.strategy;

import java.io.Serializable;

import com.kumoasobi.scribble.models.GameState;
import com.kumoasobi.scribble.models.Player;

public class LimitedTileGameEndStrategy implements GameEndStrategy, Serializable {
    @Override
    public boolean isGameOver(GameState gs) {
        if (gs.getBag().tilesRemaining() == 0) {
            for (Player p : gs.getPlayers()) {
                if (p.getRack().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }
}