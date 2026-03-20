package scribble.models;

import java.util.ArrayList;
import java.util.List;

public class Move {
    private List<Placement> placements = new ArrayList<>();

    public void addPlacement(Placement p) {
        placements.add(p);
    }

    public List<Placement> getPlacements() {
        return placements;
    }

    public boolean hasPlacement(int row, int col) {
        for (Placement p : placements) {
            if (p.row == row && p.col == col) {
                return true;
            }
        }
        return false;
    }
}
