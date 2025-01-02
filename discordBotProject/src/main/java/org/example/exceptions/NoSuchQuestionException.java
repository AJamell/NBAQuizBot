package org.example.exceptions;

public class NoSuchQuestionException extends RuntimeException {
    public NoSuchQuestionException(String question) {
        super("No such question: " + question);
    }
}

