package edu.moravian;

import org.example.exceptions.*;
import java.util.List;

public class TriviaGame {

    private final DatabaseManager storage;
    private boolean inProgress;
    private int currentIndexOfQuestion = 0;

    public TriviaGame(DatabaseManager storage) {
        this.storage = storage;
        inProgress = false;
        currentIndexOfQuestion = 0;
    }

    public void startGame() throws InternalServerException, StorageException {
        if (gameInProgress()) {
            throw new GameInProgressException();
        }
        if (storage.getQuestionsCount() == 0) {
            throw new InternalServerException("Cannot start a game without questions.");
        }
        try {
            storage.reset();
            inProgress = true;
            currentIndexOfQuestion = 0;
        } catch (StorageException e) {
            throw new InternalServerException("Error while starting game");
        }
    }

    public boolean gameInProgress() {
        return inProgress;
    }

    public void addPlayer(String player) throws InternalServerException {
        if (!gameInProgress())
            throw new NoGameInProgressException();

        try {
            storage.addPlayer(player);
        } catch (StorageException e) {
            throw new InternalServerException("Error while adding player");
        }
    }


    public List<String> getPlayers() throws InternalServerException {
        try {
            return storage.getPlayers();
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting players");
        }
    }

    public int getPlayersScores(String player) throws InternalServerException{
        if (!gameInProgress())
            throw new NoGameInProgressException();
        try {

            return storage.getScore(player);
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting players scores");
        }
    }

    public void handleAnswer(String player, String answer) throws NoGameInProgressException, InternalServerException, StorageException {
        if (!gameInProgress()) {
            throw new NoGameInProgressException();
        }
        if (!isAnswerInChoices(answer)) {
            throw new NoSuchAnswerException(answer);
        }
        if (!storage.getPlayers().contains(player)) {
            throw new NoSuchPlayerException(player);
        }

        if(storage.getPlayers().size() == 1)
        {
            handleSinglePLayerScore(player, answer);
        }
        else
        {
            handleMultiplayerScore(player, answer);
        }

    }


    private void handleSinglePLayerScore(String player, String answer) throws StorageException {
        String correctAnswer = storage.getAnswer(currentIndexOfQuestion);
        correctAnswer = correctAnswer.toLowerCase().substring(0, 1);
        if(answer.toLowerCase().startsWith(correctAnswer))
        {
            storage.addScore(player, 1);
            currentIndexOfQuestion++;
        }
        else
        {
            storage.addScore(player, 0);
            currentIndexOfQuestion++;
        }
    }

    private void handleMultiplayerScore(String player, String answer) throws StorageException{
        String correctAnswer = storage.getAnswer(currentIndexOfQuestion);
        correctAnswer = correctAnswer.toLowerCase().substring(0, 1);
        if(answer.toLowerCase().startsWith(correctAnswer))
        {
            storage.addScore(player, 1);
            currentIndexOfQuestion++;
        }
    }

    public int getCurrentIndexOfQuestion() {
        return currentIndexOfQuestion;
    }


    public void reset() throws InternalServerException {
        try {
            storage.reset();
            inProgress = false;
            currentIndexOfQuestion = 0;
        } catch (StorageException e) {
            throw new InternalServerException("Error while resetting");
        }
    }


    public boolean isAnswerInChoices(String answer) throws InternalServerException {

        if (answer == null || answer.length() != 1) {
            return false;
        }
        String[] choices  = {"a", "b", "c", "d"};
        for (String choice : choices) {
            if (answer.toLowerCase().startsWith(choice)) {
                return true;
            }
        }
        return false;
    }

    public String getCurrentQuestion() throws InternalServerException {
        try {
            return storage.getQuestions(currentIndexOfQuestion);
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting current question");
        }
    }


    public int getQuestionsCount() throws InternalServerException {
        try {
            return storage.getQuestionsCount();
        } catch (StorageException e) {
            throw new InternalServerException("Error while getting questions count");
        }
    }

    public String getCurrentQuestionAndChoices(int index) throws InternalServerException {
        try {
            String question = storage.getQuestions(index);
            String choices = storage.getChoices(index);
            for (int i = 0; i < choices.length(); i++) {
                if (choices.charAt(i) == '*') {
                    choices = choices.substring(0, i) + " " + choices.substring(i + 1);
                }
            }
            return question + "\n" + choices.replaceAll("[\\[\\]]", "").replace(", ", "\n");
        } catch (StorageException e) {
            throw new RuntimeException(e);
        }

    }
}