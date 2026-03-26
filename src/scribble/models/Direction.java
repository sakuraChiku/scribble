package scribble.models;

/**
 * Enumerate the direction of a word
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public enum Direction {
    HORIZONTAL(1, 0), // it means that the placement in a Horizontal word changes it's column, not row
    VERTICAL(0, 1); // it means that the placement in a Vertical word changes it's row, not column
    int dx, dy;

    Direction(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }
    
}
