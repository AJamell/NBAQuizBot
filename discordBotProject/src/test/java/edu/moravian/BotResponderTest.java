package edu.moravian;

import org.example.exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class BotResponderTest {

    private BotResponder botResponder;
    
    @BeforeEach
    void setUp() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("question1", List.of("option1", "option2", "option3"),"answer1");
        TriviaGame triviaGame = new TriviaGame(memoryDatabase);
        botResponder = new BotResponder(triviaGame);
    }

    @Test
    public void testCanGetCommandsFromCallingHelp() throws Exception {
        String response = botResponder.respond("user1", "!help");
        assertEquals(BotResponses.help(), response);
    }

    @Test
    public void testCanStartGame() throws Exception {
        String response = botResponder.respond("user1", "!start");
        assertEquals(BotResponses.gameStarted(), response);
    }

    @Test
    public void testCanJoinGame() throws Exception {
        botResponder.respond("user1", "!start");
        String response = botResponder.respond("user1", "!join");
        assertEquals(BotResponses.playerJoined("user1"), response);
    }

    @Test
    public void testCanGetStatusWhenGameIsNotYetGoing() throws Exception{
        botResponder.respond("user1", "!start");
        botResponder.respond("user1", "!join");
        String response = botResponder.respond("user1", "!status");
        assertEquals(BotResponses.gameStarting(java.util.List.of("user1")), response);
    }

    @Test
    public void testCanQuitGameWhenGameIsNotGoing() throws Exception {
        botResponder.respond("user1", "!start");
        botResponder.respond("user1", "!join");
        String response = botResponder.respond("user1", "!quit");
        assertEquals("No game has been started.  Use `!start` to start a new game.", response);
    }

    @Test
    public void testCanQuitGameWhenGameIsGoing() throws Exception {
        botResponder.respond("user1", "!start");
        botResponder.respond("user1", "!join");
        botResponder.respond("user1", "!go");
        String response = botResponder.respond("user1", "!quit");
        HashMap<String,Integer> playerScores = new HashMap<>();
        playerScores.put("user1", 0);
        assertEquals(BotResponses.quitGame(playerScores), response);
    }

    @Test
    public void testCanGetStatusWhenGameIsGoing() throws Exception {
        botResponder.respond("user1", "!start");
        botResponder.respond("user1", "!join");
        botResponder.respond("user1", "!go");
        String response = botResponder.respond("user1", "!status");
        HashMap<String,Integer> playerScores = new HashMap<>();
        playerScores.put("user1", 0);
        assertEquals(BotResponses.gameInProgress(playerScores), response);
    }

    @Test
    public void testCanGetStatusWhenGameIsOver() throws Exception {
        botResponder.respond("user1", "!start");
        botResponder.respond("user1", "!join");
        botResponder.respond("user1", "!go");
        botResponder.respond("user1", "!quit");
        String response = botResponder.respond("user1", "!status");
        assertEquals(BotResponses.noGameStatus(), response);
    }

    @Test
    public void testCanGetStatusWhenGameIsStarting() throws Exception {
        botResponder.respond("user1", "!start");
        String response = botResponder.respond("user1", "!status");
        assertEquals(BotResponses.gameStarting(java.util.List.of()), response);
    }

    @Test
    public void testCanGetStatusWhenGameIsStartedButNotGoing() throws Exception {
        botResponder.respond("user1", "!start");
        botResponder.respond("user1", "!join");
        String response = botResponder.respond("user1", "!status");
        assertEquals(BotResponses.gameStarting(List.of("user1")), response);
    }

    @Test
    public void testUnknownCommandWhenGameNotStarted(){
        String response = botResponder.respond("user1", "!`join");
        assertEquals(BotResponses.invalidCommand(), response);
    }

    @Test
    public void testUnknownCommandWhenGameStarted(){
        botResponder.respond("user1","!start");
        String response = botResponder.respond("user1", "!unknown");
        assertEquals(BotResponses.invalidCommand(), response);
    }

    @Test
    public void testAnswerWhenPlayerNotInGame(){
        botResponder.respond("user1","!start");
        botResponder.respond("user1","!join");
        botResponder.respond("user1","!go");
        String response = botResponder.respond("user2","answer");
        assertEquals(BotResponses.playerNotInGame("user2"), response);
    }

    @Test
    public void testHelpWhenPlayerIsNotInTheGameWhileInProgress(){
        botResponder.respond("user1","!start");
        botResponder.respond("user1","!join");
        botResponder.respond("user1","!go");
        String response = botResponder.respond("user2","!help");
        assertEquals(BotResponses.playerNotInGame("user2"), response);
    }

    @Test
    public void testHandleGoWhenPlayerIsNotInTheGame(){
        botResponder.respond("user1","!start");
        botResponder.respond("user1","!join");
        String response = botResponder.respond("user2","!go");
        assertEquals(BotResponses.playerNotInGame("user2"), response);
    }

    @Test
    public void testGetInfoWhenPlayerIsNotInTheGame(){
        botResponder.respond("user1","!start");
        botResponder.respond("user1","!join");
        botResponder.respond("user1","!go");
        String response = botResponder.respond("user2","!info");
        assertEquals(BotResponses.playerNotInGame("user2"), response);
    }

    @Test
    public void testHandleUnknownCommandWhenGameIsStarting(){
        botResponder.respond("user1","!start");
        botResponder.respond("user1","!join");
        String response = botResponder.respond("user1","!joinn");
        assertEquals(BotResponses.invalidCommand(), response);
    }
}