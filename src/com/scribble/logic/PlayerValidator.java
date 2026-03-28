package com.scribble.logic;

import java.util.List;

import com.scribble.exceptions.GameException;
import com.scribble.exceptions.TileNotEnoughException;
import com.scribble.models.Move;
import com.scribble.models.Placement;
import com.scribble.models.Player;
import com.scribble.models.Tile;

/**
 * Validate if player has enough tiles to place the move
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class PlayerValidator {
    public void canMakeWord(Move move, Player player) throws GameException{
        List<Tile> rack = player.getRack();
        for (Placement p : move.getPlacements()) {
            Tile tile = p.getTile();
            if (!rack.contains(tile)) {
                throw new TileNotEnoughException("Tiles are not enough!");
            }
        }
    }
}
