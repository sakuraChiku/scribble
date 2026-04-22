package com.kumoasobi.scribble.models;

import java.io.Serializable;
import java.util.List;

import com.kumoasobi.scribble.rules.strategy.GameEndStrategy;

/**
 * Serialize the players, bag, and board to save the game
 * 
 * @author Yicheng Ying, Chuhui Gu
 * @version 1.0
 */
public class GameState implements Serializable {
    private List<Player> players;
    private TileBag bag;
    private Board board;
    private int currentPlayerIndex;
    private int turns;
    private long startTime;
    private GameEndStrategy endStrategy;
    
    public List<Player> getPlayers() {
        return players;
    }
    public TileBag getBag() {
        return bag;
    }
    public Board getBoard() {
        return board;
    }
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public int getTurns() {
        return turns;
    }
    
    public void setPlayers(List<Player> players) {
        this.players = players;
    }
    public void setBag(TileBag bag) {
        this.bag = bag;
    }
    public void setBoard(Board board) {
        this.board = board;
    }
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
    public void setTurns(int turns) {
        this.turns = turns;
    }

    public long getStartTime() {
        return startTime;
    }
    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }
    
    public GameEndStrategy getEndStrategy() {
        return endStrategy;
    }
    public void setEndStrategy(GameEndStrategy endStrategy) {
        this.endStrategy = endStrategy;
    }
}