package com.kumoasobi.scribble.models;

import java.io.Serializable;
import java.util.Objects;

/**
 * Includes the letter and the score of a tile
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class Tile implements Serializable{
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

    @Override
    public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Tile t)) return false;
    // 空白tile：只要score都是0，就视为同一种tile
    if (this.score == 0 && t.score == 0) return true;
    // 普通tile：字母和分数都必须一致
    return this.score == t.score && this.letter == t.letter;
}

    @Override
    public int hashCode() {
        return score == 0 ? 0 : Objects.hash(letter, score);
    }
}
