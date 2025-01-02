package edu.moravian;

import org.example.exceptions.StorageException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import redis.clients.jedis.Jedis;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class RedisDatabaseTest {

    @BeforeEach
    public void setUp() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        Jedis jedis = new Jedis("localhost", 6379);
        redisDatabase.reset();
        jedis.flushAll();
    }


    @Test
    public void testNothing() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.testConnection();
        redisDatabase.reset();
    }

    @Test
    public void testAddingAQuestionAndCheckingTheAnswer() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin"), "Paris");
        assertEquals("Paris", redisDatabase.getAnswer(0));
        assertTrue(redisDatabase.checkAnswer("What is the capital of France?", "Paris"));
        assertFalse(redisDatabase.checkAnswer("What is the capital of France?", "London"));
        redisDatabase.reset();
    }

    @Test
    public void testCheckingOptionsForAQuestions() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin"), "Paris");
        assertEquals("[Paris, London, Berlin]", redisDatabase.getChoices(0));
        redisDatabase.reset();
    }

    @Test
    public void testAddingAPlayerAndCheckingNewScore() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addPlayer("Alice");
        redisDatabase.addScore("Alice", 10);
        assertEquals(10, redisDatabase.getScore("Alice"));
        redisDatabase.addScore("Alice", 5);
        assertEquals(15, redisDatabase.getScore("Alice"));
        redisDatabase.reset();
    }

    @Test
    public void testAddingAPlayerAndCheckingAllPlayersWereAdded() throws StorageException{
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addPlayer("Alice");
        redisDatabase.addPlayer("Bob");
        redisDatabase.addPlayer("Charlie");
        assertEquals(List.of("Bob", "Alice", "Charlie"), redisDatabase.getPlayers());
        redisDatabase.reset();
    }

    @Test
    public void testGetQuestionCountWhileAddingMultipleQuestions() throws StorageException{
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin"), "Paris");
        redisDatabase.addQuestion("What is the capital of Germany?", List.of("Paris", "London", "Berlin"), "Berlin");
        redisDatabase.addQuestion("What is the capital of England?", List.of("Paris", "London", "Berlin"), "London");
        assertEquals(3, redisDatabase.getQuestionsCount());
        assertEquals("Berlin",redisDatabase.getAnswer(1));
        assertFalse(redisDatabase.checkAnswer("What is the capital of France?", "Berlin"));
        redisDatabase.reset();
    }

    @Test
    public void testReset() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin"), "Paris");
        redisDatabase.addPlayer("Alice");
        redisDatabase.addScore("Alice", 10);
        redisDatabase.reset();
        assertEquals(0, redisDatabase.getPlayers().size());
    }

    @Test
    public void testGetQuestions() throws StorageException {
        RedisDatabase redisDatabase = new RedisDatabase("localhost", 6379);
        redisDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin"), "Paris");
        assertEquals("What is the capital of France?", redisDatabase.getQuestions(0));
        redisDatabase.reset();
    }

}