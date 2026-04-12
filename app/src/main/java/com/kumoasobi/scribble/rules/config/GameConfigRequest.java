package com.kumoasobi.scribble.rules.config;

import java.util.List;

import com.kumoasobi.scribble.models.Player;

/**
 * The UI should fill the request like:
 * GameConfigRequest request = new GameConfigRequest()
 * 
 * request.endMode = ... (select)
 * request.scoreLimit = ... (user input)
 * ......
 */
public class GameConfigRequest {
    public EndMode endMode;

    public int scoreLimit;
    public int turnLimit;
    public long timeLimitMillis;

    public DrawMode drawMode;

    public List<Player> allPlayers;
}
