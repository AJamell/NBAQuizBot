package edu.moravian;

import org.example.exceptions.StorageException;

import java.util.List;

public interface DatabaseManager {

    void addQuestion(String question, List<String> options, String answer) throws StorageException;

    String getQuestions(int question) throws StorageException;

    String getChoices(int index) throws StorageException;

    int getQuestionsCount() throws StorageException;

    String getAnswer(int index) throws StorageException;

    boolean checkAnswer(String question, String answer) throws StorageException;

    void addPlayer(String player) throws StorageException;

    List<String> getPlayers() throws StorageException;

    void addScore(String player, int score) throws StorageException;

    int getScore(String player) throws StorageException;

    void reset() throws StorageException;

}





