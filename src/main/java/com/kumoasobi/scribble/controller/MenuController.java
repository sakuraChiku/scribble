package com.kumoasobi.scribble.controller;

import java.util.Set;

import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.GameState;
import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.models.TileBag;
import com.kumoasobi.scribble.rules.config.GameConfig;
import com.kumoasobi.scribble.rules.config.GameConfigFactory;
import com.kumoasobi.scribble.rules.config.GameConfigRequest;
import com.kumoasobi.scribble.util.DictionaryLoader;

public class MenuController {

    /**
     * Convert a raw UI request into an immutable GameConfig.
     */
    public GameConfig submitRequest(GameConfigRequest request) {
        return new GameConfig(
            GameConfigFactory.selectDrawStrategy(request),
            GameConfigFactory.selectEndStrategy(request),
            GameConfigFactory.getValidPlayers(request)
        );
    }

    /**
     * Create a fresh GameState from a GameConfig.
     * Initial tile dealing is done here so callers don't have to repeat it.
     */
    public GameState newGame(GameConfig config) {
        GameState gs = new GameState();
        gs.setPlayers(config.players);
        gs.setBag(new TileBag(config.drawStrategy));
        gs.setBoard(new Board());
        gs.setCurrentPlayerIndex(0);
        gs.setTurns(0);
        gs.setStartTime(System.currentTimeMillis());

        for (Player p : gs.getPlayers()) {
            p.addTiles(gs.getBag().drawTiles(Player.getRackSize()));
        }
        return gs;
    }

    public Set<String> loadDictionary(String filePath) {
        return DictionaryLoader.loadDictionarySet(filePath);
    }
}
