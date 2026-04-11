package com.kumoasobi.scribble.models;

public class WordInfo {
    private final String word;
    private final int score;

    public WordInfo(String word, int score) {
        this.word = word;
        this.score = score;
    }

    public String getWord() {
        return word;
    }

    public int getScore() {
        return score;
    }
}
