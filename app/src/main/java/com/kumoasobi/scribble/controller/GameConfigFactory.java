package com.kumoasobi.scribble.controller;


import com.kumoasobi.scribble.rules.config.GameConfig;
import com.kumoasobi.scribble.rules.config.GameConfigRequest;
import com.kumoasobi.scribble.rules.strategy.DrawStrategy;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedDrawStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedScoreGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedTileGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedTimeGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedTurnGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.UnlimitedDrawStrategy;

public class GameConfigFactory {
    public GameEndStrategy selectEndStrategy(GameConfigRequest request) {
        switch (request.endMode) {
            case TURN_LIMIT -> {
                return new LimitedTurnGameEndStrategy(request.turnLimit);
            }
            case SCORE_LIMIT -> {
                return new LimitedScoreGameEndStrategy(request.scoreLimit);
            }
            case TIME_LIMIT -> {
                return new LimitedTimeGameEndStrategy(request.timeLimitMillis);
            }
            case TILE_LIMIT -> {
                return new LimitedTileGameEndStrategy();
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public DrawStrategy selectDrawStrategy(GameConfigRequest request) {
        switch (request.drawMode) {
            case LIMITED -> {
                return new LimitedDrawStrategy();
            }
            case UNLIMITED -> {
                return new UnlimitedDrawStrategy();
            }
            default -> throw new IllegalArgumentException();
        }
    }

    public GameConfig createConfig(GameConfigRequest request) {
        return new GameConfig(selectDrawStrategy(request), selectEndStrategy(request));
    }
}
