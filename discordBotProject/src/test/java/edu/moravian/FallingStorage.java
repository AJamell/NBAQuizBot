package edu.moravian;

import org.example.exceptions.StorageException;

import java.util.List;

public class FallingStorage implements DatabaseManager{
    @Override
    public void addQuestion(String question, List<String> options, String answer) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public String getQuestions(int question) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public String getChoices(int index) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public int getQuestionsCount() throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public String getAnswer(int index) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public boolean checkAnswer(String question, String answer) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public void addPlayer(String player) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public List<String> getPlayers() throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public void addScore(String player, int score) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public int getScore(String player) throws StorageException {
        throw new StorageException("Failed to reset storage");
    }

    @Override
    public void reset() throws StorageException {
        throw new StorageException("Failed to reset storage");
    }
}
