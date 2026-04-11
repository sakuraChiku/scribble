package com.kumoasobi.scribble.rules.strategy;

import com.kumoasobi.scribble.models.GameState;

public class LimitedTurnGameEndStrategy implements GameEndStrategy {
    private final int targetTurn;

    public LimitedTurnGameEndStrategy(int targetTurn) {
        this.targetTurn = targetTurn;
    }
    @Override
    public boolean isGameOver(GameState gs) {
        return gs.getTurns() >= targetTurn;
    }
}
