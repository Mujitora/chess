package dataaccess.sql;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.DatabaseManager;
import model.AuthData;

import java.sql.SQLException;

public class SQLAuthDAO implements AuthDAO {
    @Override
    public void createAuth(AuthData authData) throws DataAccessException {
        try(var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("INSERT INTO auth (authToken, username) VALUES (?,?)")) {
                preparedStatement.setString(1, authData.authToken());
                preparedStatement.setString(2, authData.username());
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("could not create auth: " +e.getMessage());
        }

    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT authToken, username FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                try (var resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        return new AuthData(
                                resultSet.getString("authToken"),
                                resultSet.getString("username"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not get auth: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("DELETE FROM auth WHERE authToken = ?")) {
                preparedStatement.setString(1, authToken);
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("could not delete auth: " + e.getMessage());
        }
    }

    @Override
    public void clear() throws DataAccessException {
        try(var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("DELETE FROM auth")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("could not clear auth: " +e.getMessage());
        }
    }
}
