package com.kumoasobi.scribble.rules.config;

/**
 * Four end modes:
 * 1. end when the maximum score reach a limit
 * 2. end when the tiles in tile bag and rack run out
 * 3. end when reaching maximum time
 * 4. end when reaching maximum turns
 * 
 * @author Yicheng Ying
 * @version 1.0
 */
public enum EndMode {
    SCORE_LIMIT,
    TILE_LIMIT,
    TIME_LIMIT,
    TURN_LIMIT;
}
