package scribble.models;

public class WordInfo {
    String word;
    int score;

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
