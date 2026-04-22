package com.kumoasobi.scribble.rules.validator;

import java.util.List;

import com.kumoasobi.scribble.exceptions.GameException;
import com.kumoasobi.scribble.exceptions.TileNotEnoughException;
import com.kumoasobi.scribble.models.Move;
import com.kumoasobi.scribble.models.Placement;
import com.kumoasobi.scribble.models.Player;
import com.kumoasobi.scribble.models.Tile;

/**
 * Validate if player has enough tiles to place the move
 * 
 * @author Yicheng Ying, Chuhui Gu
 * @version 1.0
 */
public class PlayerValidator {
    public static void canMakeWord(Move move, Player player) throws GameException{
        List<Tile> rack = player.getRack();
        for (Placement p : move.getPlacements()) {
            Tile tile = p.getTile();
            if (!rack.contains(tile)) {
                throw new TileNotEnoughException("Tiles are not enough!");
            }
        }
    }
}
