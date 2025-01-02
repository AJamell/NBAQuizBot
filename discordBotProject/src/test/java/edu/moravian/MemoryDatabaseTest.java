package edu.moravian;

import org.example.exceptions.StorageException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class MemoryDatabaseTest {
    @Test
    public void testAddAQuestionAndGetCount() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris");
        assertEquals(1, memoryDatabase.getQuestionsCount());
    }

    @Test
    public void testAddAQuestionAndGetAnswer() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris");
        assertEquals("Paris", memoryDatabase.getAnswer(0));
        memoryDatabase.addQuestion("What is the capital of Spain?", List.of("Paris", "London", "Berlin", "Madrid"), "Madrid");
        assertEquals("Madrid", memoryDatabase.getAnswer(1));
    }

    @Test
    public void testAddAQuestionAndGetChoices() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris");
        assertEquals("[Paris, London, Berlin, Madrid]", memoryDatabase.getChoices(0));
        memoryDatabase.addQuestion("What is the capital of Spain?", List.of("Paris", "London", "Berlin", "Madrid"), "Madrid");
        assertEquals("[Paris, London, Berlin, Madrid]", memoryDatabase.getChoices(1));
    }

    @Test
    public void testAddPlayerAndGetPlayers() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addPlayer("Alice");
        memoryDatabase.addPlayer("Bob");
        assertArrayEquals(new String[]{"Alice", "Bob"}, memoryDatabase.getPlayers().toArray());
    }

    @Test
    public void testAddPlayerAndGetScores() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addPlayer("Bob");
        memoryDatabase.addPlayer("Alice");
        memoryDatabase.addScore("Bob", 10);
        assertEquals(10, memoryDatabase.getScore("Bob"));
        memoryDatabase.addScore("Alice", 20);
        assertEquals(20, memoryDatabase.getScore("Alice"));
        memoryDatabase.addScore("Bob", 30);
    }

    @Test
    public void testCheckAnswer() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris");
        assertTrue(memoryDatabase.checkAnswer("What is the capital of France?", "Paris"));
        assertFalse(memoryDatabase.checkAnswer("What is the capital of France?", "London"));
    }

    @Test
    public void testGetQuestions() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris");
        assertEquals("What is the capital of France?", memoryDatabase.getQuestions(0));
        memoryDatabase.addQuestion("What is the capital of Spain?", List.of("Paris", "London", "Berlin", "Madrid"), "Madrid");
        assertEquals("What is the capital of Spain?", memoryDatabase.getQuestions(1));
    }

    @Test
    public void testReset() throws StorageException {
        MemoryDatabase memoryDatabase = new MemoryDatabase();
        memoryDatabase.addQuestion("What is the capital of France?", List.of("Paris", "London", "Berlin", "Madrid"), "Paris");
        memoryDatabase.addPlayer("Alice");
        memoryDatabase.addScore("Alice", 10);
        memoryDatabase.reset();
        assertEquals(0, memoryDatabase.getPlayers().size());
    }
}