package com.kumoasobi.scribble.rules.config;

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

    public int playerNum;
}
