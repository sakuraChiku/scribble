package com.kumoasobi.scribble.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import com.kumoasobi.scribble.models.Board;
import com.kumoasobi.scribble.models.GameState;
import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.models.TileBag;
import com.kumoasobi.scribble.util.DictionaryLoader;

public class MenuController {
    public GameState newGame(int playerNum) {
        List<Player> players = new ArrayList<>();
        for (int i = 0; i < playerNum; i++) {
            Player player = new Player("Sakura", UUID.randomUUID());
            players.add(player);
        }
        GameState gs = new GameState();
        gs.setPlayers(players);
        gs.setBag(new TileBag());
        gs.setBoard(new Board());
        gs.setCurrentPlayerIndex(0);

        return gs;
    }
    public Set<String> loadDictionary(String filePath) {
        return DictionaryLoader.loadDictionarySet(filePath);
    }
}
