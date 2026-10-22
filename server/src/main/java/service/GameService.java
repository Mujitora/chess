package service;
import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.AuthData;
import model.GameData;

import java.util.ArrayList;
import java.util.Collection;


public class GameService {
    private AuthDAO authDAO;
    private GameDAO gameDAO;

    public GameService(AuthDAO authDAO, GameDAO gameDAO){
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public ListGamesResult listGames(String authToken) throws UnauthorizedException, DataAccessException{
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("unauthorized");
        }
        Collection<GameData> gameList =  gameDAO.listGames();
        Collection<GameRecord> newGames = new ArrayList<>();
        for(GameData game: gameList){
            GameRecord thisGame = new GameRecord(game.gameID(), game.whiteUsername(), game.blackUsername(), game.gameName());
            newGames.add(thisGame);
        }
        return new ListGamesResult(newGames);
    }

    public CreateGameResult createGame(String authToken, CreateGameRequest request) throws UnauthorizedException, DataAccessException, BadRequestException{
        if (authDAO.getAuth(authToken) == null){
            throw new UnauthorizedException("unauthorized");
        }
        if(request == null || request.gameName() == null || request.gameName().isEmpty()){
            throw new BadRequestException("bad request");
        }
        GameData gameData = new GameData(0, null, null, request.gameName(), new ChessGame());
        int gameID = gameDAO.createGameData(gameData);
        return new CreateGameResult(gameID);
    }

    public void joinGame(String authToken, JoinGameRequest request) throws  BadRequestException, DataAccessException, UnauthorizedException, AlreadyTakenException{
        AuthData authData = authDAO.getAuth(authToken);
        if (authData == null){
            throw new UnauthorizedException("unauthorized");
        }
        if(request == null || request.playerColor() == null ){
            throw new BadRequestException("bad request");
        }
        boolean validColor = "WHITE".equals(request.playerColor()) || "BLACK".equals(request.playerColor());
        if(!validColor){
            throw new BadRequestException("bad request");
        }
        GameData joiningGame = gameDAO.getGame(request.gameID());
        if(joiningGame == null){
            throw new BadRequestException("bad request");
        }
        if(joiningGame.whiteUsername() != null && request.playerColor().equals("WHITE")){
            throw new AlreadyTakenException("color already taken");
        }
        if(joiningGame.blackUsername() != null && request.playerColor().equals("BLACK")){
            throw new AlreadyTakenException("color already taken");
        }

        GameData updatedGame;
        if (request.playerColor().equals("WHITE")){
            updatedGame = new GameData(request.gameID(), authData.username() , joiningGame.blackUsername(), joiningGame.gameName(), joiningGame.game());
        }
        else {
            updatedGame = new GameData(request.gameID(), joiningGame.whiteUsername(), authData.username(), joiningGame.gameName(), joiningGame.game());
        }
        gameDAO.updateGame(updatedGame);




    }





}
