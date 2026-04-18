package com.kumoasobi.scribble.rules.strategy;

import com.kumoasobi.scribble.models.GameState;

public interface GameEndStrategy {
    boolean isGameOver(GameState gs);
}
