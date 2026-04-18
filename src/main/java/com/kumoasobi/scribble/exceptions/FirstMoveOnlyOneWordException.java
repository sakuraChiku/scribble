package com.kumoasobi.scribble.exceptions;
/**
 * Exception thrown when the first move is only one word
 */

public class FirstMoveOnlyOneWordException extends GameException {
    public FirstMoveOnlyOneWordException(String message) {
        super(message);
    }
}
