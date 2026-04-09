package com.kumoasobi.scribble.models;

import java.io.Serializable;
import java.util.List;

/**
 * Serialize the players, bag, and board to save the game
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class GameState implements Serializable {
    private List<Player> players;
    private TileBag bag;
    private Board board;
    private int currentPlayerIndex;
}