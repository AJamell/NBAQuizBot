package edu.moravian;


import org.example.exceptions.StorageException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.exceptions.JedisException;
import java.util.List;

public class RedisDatabase implements DatabaseManager {

    private final Jedis jedis;
    public RedisDatabase(String host, int port) throws StorageException {
        try {
            jedis = new Jedis(host, port);
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    public void testConnection() throws StorageException {
        try {
            jedis.ping();
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }


    @Override
    public void addQuestion(String question, List<String> options, String answer) throws StorageException {

        try {
            jedis.rpush("questions", question);
            jedis.hset("options", question, options.toString());
            jedis.rpush("answers", answer);
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public String getAnswer(int index) throws StorageException {
        try{
            return jedis.lindex("answers", index);
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }
    @Override
    public String getChoices(int index) throws StorageException {
        try {
            String question = jedis.lindex("questions", index);
            return jedis.hget("options", question);
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public boolean checkAnswer(String question, String answer) throws StorageException {
        try{
            List<String> questions = jedis.lrange("questions", 0, -1);
            List<String> answers = jedis.lrange("answers", 0, -1);
            for (int i = 0; i < questions.size(); i++) {
                if (questions.get(i).equals(question)) {
                    return answers.get(i).equals(answer);
                }
            }
            return false;
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public String getQuestions(int questionIndex) throws StorageException {
        try{
            return jedis.lindex("questions", questionIndex);
        }
        catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public int getQuestionsCount() throws StorageException {
        try{
            return (int) jedis.llen("questions");
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public void addPlayer(String player) throws StorageException {
        try{
            jedis.hset("scores", player, "0");
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public List<String> getPlayers() throws StorageException {
        try{
            return List.copyOf(jedis.hkeys("scores"));
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public void addScore(String player, int score) throws StorageException {
        try{
            int currentScore = Integer.parseInt(jedis.hget("scores", player));
            int newScore = currentScore + score;
            jedis.hset("scores", player, String.valueOf(newScore));
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public int getScore(String player) throws StorageException {
        try {
            return Integer.parseInt(jedis.hget("scores", player));
        } catch (JedisException e) {
            throw new StorageException("Could not connect to Redis server");
        }
    }

    @Override
    public void reset() throws StorageException {
        jedis.del("players");
        jedis.del("scores");
    }
}

