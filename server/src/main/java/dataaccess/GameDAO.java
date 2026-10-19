package dataaccess;

import model.GameData;

import java.util.Collection;

public interface GameDAO {
    //createGame
    int createGameData(GameData gameData) throws DataAccessException;
    //getGame
    GameData getGame(int gameID) throws DataAccessException;
    //listGames
    Collection<GameData> listGames() throws DataAccessException;
    //updateGame
    void updateGame(GameData updatedGameData) throws DataAccessException;
    void clear() throws DataAccessException;
}
