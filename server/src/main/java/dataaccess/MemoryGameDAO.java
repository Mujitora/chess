package dataaccess;

import model.GameData;

import java.util.*;

public class MemoryGameDAO implements GameDAO{
    private Map<Integer, GameData> games = new HashMap<>();
    private int id = 100;
    @Override
    public int createGameData(GameData gameData) throws DataAccessException {
        id += 1;
        GameData newGame = new GameData(id, gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), gameData.game());
        games.put(id, newGame);
        return id;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return games.get(gameID);
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        return new ArrayList<>(games.values());
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        if (games.containsKey(updatedGameData.gameID())){
            games.put(updatedGameData.gameID(), updatedGameData);
        }
        else{
            throw new DataAccessException("Game does not exist");
        }

    }

    @Override
    public void clear() throws DataAccessException {
        games.clear();
    }
}
