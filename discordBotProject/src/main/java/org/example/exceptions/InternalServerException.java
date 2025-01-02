package org.example.exceptions;

public class InternalServerException extends Exception {
    public InternalServerException(String message) {
        super("Internal Server Error: " + message);
    }
}

