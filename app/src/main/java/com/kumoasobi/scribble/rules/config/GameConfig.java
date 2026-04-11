package com.kumoasobi.scribble.rules.config;

import com.kumoasobi.scribble.rules.strategy.DrawStrategy;
import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;

public class GameConfig {
    public DrawStrategy drawStrategy;
    public GameEndStrategy endStrategy;

    public GameConfig(DrawStrategy ds, GameEndStrategy es) {
        drawStrategy = ds;
        endStrategy = es;
    }
}
