package com.kumoasobi.scribble.ai;

/**
 * AI difficulty levels.
 *
 * EASY   — picks a random valid move from all candidates.
 * MEDIUM — picks the best-scoring valid move, but only searches
 *          a limited subset of anchor positions (faster, less optimal).
 * HARD   — full board search, always plays the highest-scoring valid move.
 * 
 * @author Peixuan Ding, Yutong Xiao
 * @version 1.0
 */
public enum AIDifficulty {
    EASY,
    MEDIUM,
    HARD
}
