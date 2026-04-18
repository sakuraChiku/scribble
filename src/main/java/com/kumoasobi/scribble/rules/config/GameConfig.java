package com.kumoasobi.scribble.rules.config;

import java.util.List;

import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.rules.strategy.DrawStrategy;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;

public class GameConfig {
    public DrawStrategy drawStrategy;
    public GameEndStrategy endStrategy;
    public List<Player> players;

    public GameConfig(DrawStrategy ds, GameEndStrategy es, List<Player> players) {
        drawStrategy = ds;
        endStrategy = es;
        this.players = players;
    }
}
