package org.example.exceptions;

public class NoMoreQuestionException extends RuntimeException{
    public NoMoreQuestionException() {
        super("No more questions available");
    }
}