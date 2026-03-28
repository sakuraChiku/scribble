package com.kumoasobi.scribble.models;

import java.util.List;

public class MoveResult {
    private boolean isValidMove;
    private int totalScore;
    private List<String> words;

    public MoveResult(boolean isValidMove, int totalScore, List<String> words) {
        this.isValidMove = isValidMove;
        this.totalScore = totalScore;
        this.words = words;
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
}
