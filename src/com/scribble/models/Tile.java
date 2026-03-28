package com.scribble.models;

/**
 * Includes the letter and the score of a tile
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Tile {
    private final char letter;
    private final int score;
    private boolean isBonusUsed;

    public Tile(char letter, int score) {
        this.letter = letter;
        this.score = score;
        this.isBonusUsed = false;
    }    

    public char getLetter() {
        return letter;
    }

    public int getScore() {
        return score;
    }

    public boolean isBonusUsed() {
        return isBonusUsed;
    }

    public void useBonus() {
        isBonusUsed = true;
    }
    
}
