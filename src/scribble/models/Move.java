package scribble.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A list of placements with some methods to modify it, and return some basic information
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Move {
    private List<Placement> placements = new ArrayList<>();
    private Direction direction;

    public void addPlacement(Placement p) throws IllegalArgumentException{
        if (hasPlacement(p.getRow(), p.getCol())) throw new IllegalArgumentException("Duplicated move!");
        placements.add(p);
    }

    public List<Placement> getPlacements() {
        return Collections.unmodifiableList(placements); // make sure player can not change the placements by move.getPlacements().clear()
    }

    public Direction getDirection() {
        return direction;
    }

    public boolean isEmpty() {
        return placements.isEmpty();
    }

    public boolean hasPlacement(int row, int col) {
        for (Placement p : placements) {
            if (p.getRow() == row && p.getCol() == col) {
                return true;
            }
        }
        return false;
    }

    public void recallPlacement() {
        if (!placements.isEmpty()) {
            placements.remove(placements.size()-1);
        }
    }
    
    /**
     * Basic logical judgements
     */
    public int getMaxCol() {
        List<Integer> column = new ArrayList<>();
        for (Placement p : placements) {
            column.add(p.getCol());
        }
        return Collections.max(column);
    }
    public int getMinCol() {
        List<Integer> column = new ArrayList<>();
        for (Placement p : placements) {
            column.add(p.getCol());
        }
        return Collections.min(column);
    }
    public int getMaxRow() {
        List<Integer> row = new ArrayList<>();
        for (Placement p : placements) {
            row.add(p.getRow());
        }
        return Collections.max(row);
    }
    public int getMinRow() {
        List<Integer> row = new ArrayList<>();
        for (Placement p : placements) {
            row.add(p.getRow());
        }
        return Collections.min(row);
    }
    public int getSameCol() {
        return placements.get(0).getCol();
    }
    public int getSameRow() {
        return placements.get(0).getRow();
    }

    // judge the direction of the move
    public void isSameRow() {
        int stdRow = placements.get(0).getRow();
        for (Placement p : placements) {
            if (p.getRow() != stdRow) {
                return;
            }
        }
        direction = Direction.HORIZONTAL;
    }
    public void isSameCol() {
        int stdCol = placements.get(0).getCol();
        for (Placement p : placements) {
            if (p.getCol() != stdCol) {
                return;
            }
        }
        direction = Direction.VERTICAL;
    }

}
