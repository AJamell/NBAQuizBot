package edu.moravian;

import org.example.exceptions.*;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


public class TriviaGameTest {

    @Test
    public void testNewInstanceNoGame() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        assertFalse(triviaGame.gameInProgress());
    }

    @Test
    public void testStartGame() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        triviaGame.startGame();
        assertTrue(triviaGame.gameInProgress());
    }

    @Test
    public void testStartGameTwice() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        triviaGame.startGame();
        assertThrows(GameInProgressException.class, () -> triviaGame.startGame());
    }
    @Test
    public void testStartingAGameWithoutQuestions() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        assertThrows(InternalServerException.class, () -> triviaGame.startGame());
    }

    @Test
    public void testAddPlayerAndGetPlayersAfterStartingAGame() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        triviaGame.startGame();
        triviaGame.addPlayer("player1");
        assertEquals(List.of("player1"), triviaGame.getPlayers());
    }

    @Test
    public void testAddPlayerAndGetPlayersBeforeStartingAGame() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        assertThrows(NoGameInProgressException.class, () -> triviaGame.addPlayer("player1"));
    }

    @Test
    public void testGetPlayersScoresAfterStartingAGame() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        triviaGame.startGame();
        triviaGame.addPlayer("player1");
        triviaGame.addPlayer("player2");
        triviaGame.addPlayer("player3");
        assertEquals(0, triviaGame.getPlayersScores("player1"));
    }

    @Test
    public void testGetPlayersScoresBeforeStartingAGame() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        assertThrows(NoGameInProgressException.class, () -> triviaGame.getPlayersScores("player1"));
    }

    @Test
    public void testHandleAnswerNotInChoices() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        triviaGame.startGame();
        triviaGame.addPlayer("player1");
        assertFalse(triviaGame.isAnswerInChoices("option4"));
    }

    @Test
    public void testHandleAnswerInChoices() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("a.option1", "b.option2", "c.option3"),"answer1");
        triviaGame.startGame();
        triviaGame.addPlayer("player1");
        assertTrue(triviaGame.isAnswerInChoices("a"));
    }

    @Test
    public void testHandleAnswerCorrect() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("a.option1", "b.option2", "c.option3"),"a");
        triviaGame.startGame();
        triviaGame.addPlayer("player1");
        triviaGame.handleAnswer("player1", "a");
        assertEquals(1, triviaGame.getPlayersScores("player1"));
    }

    @Test
    public void testHandleAnswerIncorrect() throws InternalServerException, StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        memoryDatabase.addQuestion("question1", List.of("a.option1", "b.option2", "c.option3"),"a");
        triviaGame.startGame();
        triviaGame.addPlayer("player1");
        triviaGame.handleAnswer("player1", "b");
        assertEquals(0, triviaGame.getPlayersScores("player1"));
    }

}