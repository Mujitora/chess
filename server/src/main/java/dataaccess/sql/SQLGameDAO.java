package dataaccess.sql;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import dataaccess.GameDAO;
import model.GameData;
import model.UserData;

import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SQLGameDAO implements GameDAO {
    @Override
    public int createGameData(GameData gameData) throws DataAccessException {
        String json = new Gson().toJson(gameData.game());
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("INSERT INTO game (whiteUsername, blackUsername,gameName,game) VALUES(?, ?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
                preparedStatement.setString(1, gameData.whiteUsername());
                preparedStatement.setString(2, gameData.blackUsername());
                preparedStatement.setString(3, gameData.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.executeUpdate();
                try (var keys = preparedStatement.getGeneratedKeys()){
                    if(keys.next()){
                        return keys.getInt(1);
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not create game: " + e.getMessage());
        }
        throw new DataAccessException("game id was unable to be generated");
    }


    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game WHERE gameID = ?")) {
                preparedStatement.setInt(1, gameID);
                try (var resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        return new GameData(
                                resultSet.getInt("gameID"),
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"),
                                new Gson().fromJson(resultSet.getString("game"), ChessGame.class));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not get game: " + e.getMessage());
        }
        return null;
    }

    @Override
    public Collection<GameData> listGames() throws DataAccessException {
        Collection<GameData> gameList = new ArrayList<>();
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT gameID, whiteUsername, blackUsername, gameName, game FROM game")) {
                try (var resultSet = preparedStatement.executeQuery()) {
                    while (resultSet.next()) {
                        gameList.add(new GameData(
                                resultSet.getInt("gameID"),
                                resultSet.getString("whiteUsername"),
                                resultSet.getString("blackUsername"),
                                resultSet.getString("gameName"),
                                new Gson().fromJson(resultSet.getString("game"), ChessGame.class)));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not get game list: " + e.getMessage());
        }
        return gameList;
    }

    @Override
    public void updateGame(GameData updatedGameData) throws DataAccessException {
        String json = new Gson().toJson(updatedGameData.game());
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("UPDATE game SET whiteUsername = ?, blackUsername = ?, gameName = ?, game = ? WHERE gameID = ?")) {
                preparedStatement.setString(1, updatedGameData.whiteUsername());
                preparedStatement.setString(2, updatedGameData.blackUsername());
                preparedStatement.setString(3, updatedGameData.gameName());
                preparedStatement.setString(4, json);
                preparedStatement.setInt(5, updatedGameData.gameID());
                int rows = preparedStatement.executeUpdate();
                if (rows == 0 ){
                    throw new DataAccessException("no game with that ID");
                }
            }

        }
        catch (SQLException e) {
            throw new DataAccessException("could not update game: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try(var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("DELETE FROM game")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("could not clear games: " +e.getMessage());
        }

    }
}
