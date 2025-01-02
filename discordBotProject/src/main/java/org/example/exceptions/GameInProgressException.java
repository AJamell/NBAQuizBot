package org.example.exceptions;

public class GameInProgressException extends RuntimeException {
    public GameInProgressException() {
        super("Game already in progress");
    }
}
