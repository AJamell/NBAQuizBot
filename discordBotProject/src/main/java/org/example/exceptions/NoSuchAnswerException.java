package org.example.exceptions;

public class NoSuchAnswerException extends RuntimeException {
    public NoSuchAnswerException(String answer) {
        super("No such answer: " + answer);
    }
}
