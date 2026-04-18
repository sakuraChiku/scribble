package com.kumoasobi.scribble.rules.strategy;

import java.io.Serializable;

import com.kumoasobi.scribble.models.GameState;
import com.kumoasobi.scribble.models.Player;

public class LimitedScoreGameEndStrategy implements GameEndStrategy, Serializable {
    private final int targetScore;

    public LimitedScoreGameEndStrategy(int targetScore) {
        this.targetScore = targetScore;
    }

    @Override
    public boolean isGameOver(GameState state) {
        for (Player p : state.getPlayers()) {
            if (p.getScore() >= targetScore) {
                return true;
            }
        }
        return false;
    }
}
