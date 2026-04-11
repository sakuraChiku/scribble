package com.kumoasobi.scribble.models;

import java.util.List;

/**
 * Get the result of a move, including the words, and whether the move is valid, and the total score
 */
public class MoveResult {
    private boolean isValidMove;
    private int totalScore;
    private List<String> words;
    private String info;

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
