package scribble.models;

/**
 * Includes the letter and the score of a tile
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Tile {
    private final char letter;
    private final int score;

    public Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
    }    

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }
}
