package com.kumoasobi.scribble.ai;

import com.kumoasobi.scribble.models.Move;

/**
 * A candidate move found by the AI, bundled with its projected score.
 * 
 * @author Peixuan Ding
 */
public class AIMove {
    private final Move move;
    private final int  score;

    public AIMove(Move move, int score) {
        this.move  = move;
        this.score = score;
    }

    public Move getMove()  { return move; }
    public int  getScore() { return score; }
}
