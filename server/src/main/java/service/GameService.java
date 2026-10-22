package service;
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



}
