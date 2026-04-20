package com.kumoasobi.scribble.ai;

import java.util.UUID;

import com.kumoasobi.scribble.models.Player;

/**
 * An AI-controlled player.
 *
 * Extends Player so it can be stored in GameState alongside human players
 * without any changes to existing model classes.
 *
 * The ScrabbleAI engine is NOT stored here (it is not Serializable and
 * doesn't need to be saved). GameController recreates it from difficulty.
 * 
 * @author Peixuan Ding
 */
public class AIPlayer extends Player {

    private static final long serialVersionUID = 1L;

    private final AIDifficulty difficulty;

    public AIPlayer(String name, AIDifficulty difficulty) {
        super(name, UUID.randomUUID(), 0);
        this.difficulty = difficulty;
    }

    public AIDifficulty getDifficulty() { return difficulty; }

    /** Convenience: name displayed in UI. */
    @Override
    public String getName() {
        return super.getName() + " [AI-" + difficulty.name().charAt(0) + "]";
    }

    @Override
    public boolean isAI() { return true; }
}
