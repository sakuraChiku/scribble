package scribble.logic;

import java.util.List;
import scribble.exceptions.GameException;
import scribble.exceptions.TileNotEnoughException;
import scribble.models.Move;
import scribble.models.Placement;
import scribble.models.Player;
import scribble.models.Tile;

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
