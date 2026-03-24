package scribble.models;

import java.util.List;

/**
 * Serialize the players, bag, and board to save the game
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class GameState {
    List<Player> players;
    TileBag bag;
    Board board;
}
