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
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class GameConfigRequest {
    private EndMode endMode;

    private int scoreLimit;
    private int turnLimit;
    private long timeLimitMillis;

    private DrawMode drawMode;

    private List<Player> allPlayers;

    public EndMode getEndMode() {
        return endMode;
    }

    public void setEndMode(EndMode endMode) {
        this.endMode = endMode;
    }

    public int getScoreLimit() {
        return scoreLimit;
    }

    public void setScoreLimit(int scoreLimit) {
        this.scoreLimit = scoreLimit;
    }

    public int getTurnLimit() {
        return turnLimit;
    }

    public void setTurnLimit(int turnLimit) {
        this.turnLimit = turnLimit;
    }

    public long getTimeLimitMillis() {
        return timeLimitMillis;
    }

    public void setTimeLimitMillis(long timeLimitMillis) {
        this.timeLimitMillis = timeLimitMillis;
    }

    public DrawMode getDrawMode() {
        return drawMode;
    }

    public void setDrawMode(DrawMode drawMode) {
        this.drawMode = drawMode;
    }

    public List<Player> getAllPlayers() {
        return allPlayers;
    }

    public void setAllPlayers(List<Player> allPlayers) {
        this.allPlayers = allPlayers;
    }
}
