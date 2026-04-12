package com.kumoasobi.scribble.rules.strategy;

import com.kumoasobi.scribble.models.GameState;

public class LimitedTimeGameEndStrategy implements GameEndStrategy {
    private final long timeLimit;
    public LimitedTimeGameEndStrategy(long timeLimit) {
        this.timeLimit = timeLimit;
    }
    @Override
    public boolean isGameOver(GameState gs) {
        long now = System.currentTimeMillis();
        return now - gs.getStartTime() >= timeLimit;
    }
}
