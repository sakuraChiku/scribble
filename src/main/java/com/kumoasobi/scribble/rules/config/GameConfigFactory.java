package com.kumoasobi.scribble.rules.config;


import java.util.ArrayList;
import java.util.List;

import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.rules.strategy.DrawStrategy;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedDrawStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedScoreGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedTileGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedTimeGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.LimitedTurnGameEndStrategy;
import com.kumoasobi.scribble.rules.strategy.UnlimitedDrawStrategy;

public class GameConfigFactory {
    public static GameEndStrategy selectEndStrategy(GameConfigRequest request) {
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

    public static DrawStrategy selectDrawStrategy(GameConfigRequest request) {
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

    public static List<Player> getValidPlayers(GameConfigRequest request) {
        List<Player> players = new ArrayList<>();
        for (Player p : request.allPlayers) {
            if (p.getName() != null) {
                players.add(p);
            }
        }
        return players;
    }
}
