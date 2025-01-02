package edu.moravian;

import java.util.HashMap;
import java.util.List;


public class BotResponses {

    public static String gameInfo() {
        return """
                  🏀**NBA Trivia Game** 🏀  
                Test your NBA knowledge in this exciting single/multiplayer trivia game!

                **How Single Player Works:**
                - Start a new game with `!start`
                - Players join the game with `!join`
                - Start the game with `!go`
                - Answer trivia questions to earn points
                - The game ends after 6 questions
                                
                **How Multiplayer Works:**
                - Start a new game with `!start`
                - Players join the game with `!join`
                - Start the game with `!go`
                - Answer trivia questions to earn points
                - Wrong questions are repeated until answered correctly
                - The game ends after 6 questions
                                
                Show off your skills and take the crown of LeBron! 🏆  
                """;
    }

    public static String serverError() {
        return "Uh oh! Something went wrong on our end. Please try again later.";
    }

    public static String gameStarted() {
        return "\t Welcome to NBA Trivia!" + '\n' +
                "type `!help` for commands" + '\n' +
                "type `!join` to join the game";

    }

    public static String playerJoined(String username) {
        return "Player `" + username + "` has joined the game. " + '\n' +
                "Type `!go` to start the game once all players are joined.";
    }

    public static String noGameStarted() {
        return "No game has been started.  Use `!start` to start a new game.";
    }


    public static String noPlayersInGame() {
        return "No players have joined the game.  Use `!join` to join the game.";
    }

    public static String noGameStatus() {
        return "No game is currently in progress.  Use `!start` to start a new game.";
    }


    public static String gameInProgress(HashMap<String, Integer> playerScores) {
        StringBuilder response = new StringBuilder("A Game Is In Progress:\n");
        response.append("====================\n");
        response.append("Scoreboard:\n");
        for (String player : playerScores.keySet()) {
            response.append("`" + player + "`").append(": ").append(playerScores.get(player)).append(" points\n");
        }
        return response.toString();
    }

    public static String gameStarting(List<String> players) {
        return "A Game Is Starting. " + '\n' +
                "Players can join the game by typing `!join`  " + '\n' +
                "Players in the game: `" + players + "`";
    }

    public static String gameStartedButNotGoing() {
        return "A game has been started, but it has not yet begun.  " + '\n' +
                "Type `!join` to join the game.  " + '\n' +
                "Type `!go` to start the game after all players have joined.";
    }

    public static String quitGame(HashMap<String, Integer> playersScore) {
        StringBuilder response = new StringBuilder("A Game Is Over." + '\n');

        if (playersScore != null && !playersScore.isEmpty()) {
            response.append("Final Scoreboard:\n");
            for (String player : playersScore.keySet()) {
                response.append("`").append(player).append("`").append(": ")
                        .append(playersScore.get(player)).append(" points\n");
            }
        } else {
            response.append("No players participated in the game.\n");
        }

        return response.toString();

    }


    public static String help() {
        return "Nba Trivia Commands: \n" +
                "* `!start` - start a new trivia game." + '\n' +
                "* `!join` - executed by any user who wants to join the game.  \n" +
                "* `!go` - used to move the game from `STARTING` to `IN_PROGRESS`. \n" +
                "* `!status` - get the status of the game.  \n" +
                "* `!info` - get information about the game.  \n" +
                "* `!quit` - places the game in the `NO_GAME` state if it is not already\n" +
                "* `!help` - list the commands of the game";
    }


    public static String gameInProgress() {
        return "A Game Is In Progress.";
    }


    public static String answerNotInChoices() {
        return "The answer you provided is not one of the choices. Try again.";
    }


    public static String sendQuestion(String currentQuestionAndChoices) {
        return currentQuestionAndChoices;
    }

    public static String playerNotInGame(String username) {
        return "Player: `" + username + "` is not in the game.";
    }

    public static String invalidCommand() {
        return "Unknown command.  Type `!help` for a list of commands.";
    }
}

