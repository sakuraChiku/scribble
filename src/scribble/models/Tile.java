package scribble.models;

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
