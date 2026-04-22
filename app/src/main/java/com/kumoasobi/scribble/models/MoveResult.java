package com.kumoasobi.scribble.models;

import java.util.List;

/**
 * Get the result of a move, including the words, and whether the move is valid, and the total score
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public class MoveResult {
    private final boolean isValidMove;
    private final int totalScore;
    private final List<String> words;
    private final String info;

    public MoveResult(boolean isValidMove, int totalScore, List<String> words, String info) {
        this.isValidMove = isValidMove;
        this.totalScore = totalScore;
        this.words = words;
        this.info = info;
    }

    public boolean isValidMove() {
        return isValidMove;
    }

    public int getTotalScore() {
        return totalScore;
    }

    public List<String> getWords() {
        return words;
    }
    
    public String getInfo() {
        return info;
    }
}
