package scribble.models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Move {
    private List<Placement> placements = new ArrayList<>();

    public void addPlacement(Placement p) throws IllegalArgumentException{
        if (hasPlacement(p.getRow(), p.getCol())) throw new IllegalArgumentException("Duplicated move!");
        placements.add(p);
    }

    public List<Placement> getPlacements() {
        return Collections.unmodifiableList(placements); // make sure player can not change the placements by move.getPlacements().clear()
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
}
