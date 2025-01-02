package edu.moravian;


import org.example.exceptions.StorageException;

import java.util.*;

public class MemoryDatabase implements DatabaseManager {

    List<String> questions;
    List<String> answers;
    List<String> options;
    List<String> players;
    HashMap<String,Integer> playersScore;


    public MemoryDatabase() {
        questions = new ArrayList<>();
        answers = new ArrayList<>();
        options = new ArrayList<>();
        players = new ArrayList<>();
        playersScore = new HashMap<>();
    }

    @Override
    public void addQuestion(String question, List<String> options, String answer) throws StorageException {
        this.questions.add(question);
        this.options.add(options.toString());
        this.answers.add(answer);
    }

    @Override
    public String getAnswer(int index) throws StorageException {
        return answers.get(index);
    }
    @Override
    public String getChoices(int index) throws StorageException {
        return options.get(index);
    }

    @Override
    public int getQuestionsCount() throws StorageException {
        return questions.size();
    }

    @Override
    public boolean checkAnswer(String question, String answer) throws StorageException {
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).equals(question)) {
                return answers.get(i).equals(answer);
            }
        }
        return false;
    }

    @Override
    public String getQuestions(int questionIndex) throws StorageException {
        return questions.get(questionIndex);
    }

    @Override
    public void addPlayer(String player) throws StorageException {
        playersScore.put(player,0);
        players.add(player);
    }

    @Override
    public List<String> getPlayers() throws StorageException {
        return players;
    }

    @Override
    public void addScore(String player, int score) throws StorageException {
        playersScore.put(player,playersScore.get(player)+score);
    }

    @Override
    public int getScore(String player) throws StorageException {
        return playersScore.get(player);
    }
    @Override
    public void reset() throws StorageException {

        players.clear();
        playersScore.clear();
    }


}
