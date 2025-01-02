package edu.moravian;

import org.example.exceptions.InternalServerException;
import org.example.exceptions.StorageException;

import java.util.HashMap;


public class BotResponder {

    GameStatus gameStatus;
    TriviaGame game;


    public BotResponder(TriviaGame game) {
        this.game = game;
        gameStatus = GameStatus.NO_GAME;

    }

    public String respond(String username, String message) {
        try {
            if (message.equals("!start"))
                return handleStartGame();
            else if (message.equals("!join"))
                return handleJoin(username);
            else if (message.equals("!go"))
                return handleGo(username);
            else if (message.equals("!status"))
                return handleStatus(username);
            else if (message.equals("!quit"))
                return handleQuit();
            else if (message.equals("!help"))
                return handleHelp(username);
            else if (message.equals("!info"))
                return handleInfo(username);
            else if (message.startsWith("!"))
                return handleUnknownCommand(username);
            else
                return handleAnswer(username, message);
        } catch (InternalServerException | StorageException e) {
            return BotResponses.serverError();
        }
    }

    private String handleUnknownCommand(String username) throws InternalServerException {
        if(gameStatus == GameStatus.NO_GAME){
            return BotResponses.invalidCommand();
        }
        if (gameStatus == GameStatus.STARTING) {
            return BotResponses.invalidCommand();
        }

        if(!game.getPlayers().contains(username)){
            return BotResponses.playerNotInGame(username);
        }
        return BotResponses.invalidCommand();
    }

    private String handleInfo(String username) throws InternalServerException {
        if (gameStatus == GameStatus.NO_GAME) {
            return BotResponses.gameInfo();
        }
        if (gameStatus == GameStatus.STARTING) {
            return BotResponses.gameInfo();
        }
        if(!game.getPlayers().contains(username)){
            return BotResponses.playerNotInGame(username);
        }
        return BotResponses.gameInfo();
    }

    public String handleStartGame() throws InternalServerException, StorageException {
        if (gameStatus != GameStatus.NO_GAME) {
            return BotResponses.gameInProgress();
        }
        game.startGame();
        gameStatus = GameStatus.STARTING;
        return BotResponses.gameStarted();
    }

    public String handleJoin(String username) throws InternalServerException {
        if (gameStatus == GameStatus.NO_GAME) {
            return BotResponses.noGameStarted();
        }
        if (gameStatus == GameStatus.IN_PROGRESS) {
            return BotResponses.gameInProgress();
        }
        game.addPlayer(username);
        return BotResponses.playerJoined(username);
    }

    public String handleGo(String player) throws InternalServerException {
        if (!game.getPlayers().contains(player)) {
            return BotResponses.playerNotInGame(player);
        }
        if (gameStatus == GameStatus.NO_GAME) {
            return BotResponses.noGameStarted();
        }
        if (gameStatus == GameStatus.IN_PROGRESS) {
            return BotResponses.gameInProgress();
        }
        if (game.getPlayers().isEmpty()) {
            return BotResponses.noPlayersInGame();
        }
        gameStatus = GameStatus.IN_PROGRESS;
        return sendQuestionAndAnswer();
    }

    public String sendQuestionAndAnswer() throws InternalServerException {

        if (game.getCurrentIndexOfQuestion() >= game.getQuestionsCount()) {
            gameStatus = GameStatus.NO_GAME;
            return handleQuit();
        }
        String questionAndChoices = game.getCurrentQuestionAndChoices(game.getCurrentIndexOfQuestion());
        return BotResponses.sendQuestion(questionAndChoices);
    }

    public String handleStatus(String username) throws InternalServerException{
        if (gameStatus == GameStatus.NO_GAME)
        {
            return BotResponses.noGameStatus();
        } else if (gameStatus == GameStatus.STARTING) {
            return BotResponses.gameStarting(game.getPlayers());
        }
        if (!game.getPlayers().contains(username)) {
            return BotResponses.playerNotInGame(username);
        }
        HashMap<String, Integer> playersScores = new HashMap<>();
        for (String player : game.getPlayers())
            playersScores.put(player, game.getPlayersScores(player));
        return BotResponses.gameInProgress(playersScores);
    }

    public String handleQuit() throws InternalServerException {

        if (gameStatus == GameStatus.STARTING ) {
            game.reset();
            gameStatus = GameStatus.NO_GAME;
            return BotResponses.noGameStarted();
        }
        HashMap<String, Integer> playerScores = new HashMap<>();
        for (String player : game.getPlayers()) {
            playerScores.put(player, game.getPlayersScores(player));
        }
        game.reset();
        gameStatus = GameStatus.NO_GAME;
        return BotResponses.quitGame(playerScores);

    }

    public String handleHelp(String username) throws InternalServerException {
        if(gameStatus == GameStatus.NO_GAME){
            return BotResponses.help();
        }
        if(gameStatus == GameStatus.STARTING){
            return BotResponses.help();
        }
        if(!game.getPlayers().contains(username)){
            return BotResponses.playerNotInGame(username);
        }
        return BotResponses.help();
    }


    public String handleAnswer(String username, String answer) throws InternalServerException, StorageException {
        if (gameStatus == GameStatus.NO_GAME)
            return BotResponses.noGameStarted();
        if (gameStatus == GameStatus.STARTING)
            return BotResponses.gameStartedButNotGoing();
        if (!game.getPlayers().contains(username)) {
            return BotResponses.playerNotInGame(username);
        }
        if (!game.isAnswerInChoices(answer)) {
            return BotResponses.answerNotInChoices();
        }
        if (game.getCurrentQuestion() == null) {
            return handleQuit();
        }
        game.handleAnswer(username, answer);
        return sendQuestionAndAnswer();
    }
}
