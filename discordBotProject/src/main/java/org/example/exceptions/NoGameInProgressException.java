package org.example.exceptions;

public class NoGameInProgressException extends RuntimeException {
    public NoGameInProgressException() {
        super("No game in progress");
    }

}