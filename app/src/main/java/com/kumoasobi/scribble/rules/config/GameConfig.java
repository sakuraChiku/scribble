package com.kumoasobi.scribble.rules.config;

import java.util.List;

import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.rules.strategy.DrawStrategy;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;

/**
 * The config for the game, including:
 * 
 * 1. draw strategy
 * 2. end strategy
 * 3. player information
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class GameConfig {
    private DrawStrategy drawStrategy;
    private GameEndStrategy endStrategy;
    private List<Player> players;

    public GameConfig(DrawStrategy ds, GameEndStrategy es, List<Player> players) {
        drawStrategy = ds;
        endStrategy = es;
        this.players = players;
    }

    public DrawStrategy getDrawStrategy() {
        return drawStrategy;
    }

    public void setDrawStrategy(DrawStrategy drawStrategy) {
        this.drawStrategy = drawStrategy;
    }

    public GameEndStrategy getEndStrategy() {
        return endStrategy;
    }

    public void setEndStrategy(GameEndStrategy endStrategy) {
        this.endStrategy = endStrategy;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }
}
