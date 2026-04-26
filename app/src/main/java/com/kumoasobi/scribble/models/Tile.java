package com.kumoasobi.scribble.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Includes the letter and the score of a tile
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Tile implements Serializable {
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

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tile t)) return false;
    if (this.score == 0 && t.score == 0) return true;
    return this.score == t.score && this.letter == t.letter;
}

    @Override
    public int hashCode() {
        return score == 0 ? 0 : Objects.hash(letter, score);
    }
}
