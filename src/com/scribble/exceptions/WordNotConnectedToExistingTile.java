package com.scribble.exceptions;

public class WordNotConnectedToExistingTile extends GameException {
    public WordNotConnectedToExistingTile(String message) {
        super(message);
    }
}
