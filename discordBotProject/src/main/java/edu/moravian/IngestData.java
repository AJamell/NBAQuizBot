package edu.moravian;


import org.example.exceptions.StorageException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class IngestData
{
    public static List<String> readFile(String fileName) throws IOException
    {
        Path filePath = Paths.get(fileName);
        return Files.readAllLines(filePath);
    }

    public static void main(String[] args)
    {
        try
        {
            RedisDatabase redisActions = new RedisDatabase("localhost", 6379);
            redisActions.reset();
            List<String> questions = readFile("/Users/jamell/IdeaProjects/FinalProjectTrivia/src/textData/questions");
            List<String> answers = readFile("/Users/jamell/IdeaProjects/FinalProjectTrivia/src/textData/answers");
            List<String> options = readFile("/Users/jamell/IdeaProjects/FinalProjectTrivia/src/textData/choices");

            if (questions.size() != answers.size() || questions.size() != options.size()) {
                System.out.println("Mismatch in the number of questions, answers, and options.");
                return;
            }

            for (int i = 0; i < questions.size(); i++) {
                String questionText = questions.get(i).trim();
                String correctAnswer = answers.get(i).trim();
                String optionText = options.get(i).trim();

                String[] optionParts = optionText.split(", ");
                redisActions.addQuestion(questionText, List.of(optionParts), correctAnswer);
                System.out.println("Added question: " + questionText);
                System.out.println("Options: " + optionText);
                System.out.println("Correct answer: " + correctAnswer);
            }
        }
        catch (IOException | StorageException e)
        {
            System.out.println("Error while processing data: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
