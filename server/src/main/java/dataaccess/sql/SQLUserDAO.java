package dataaccess.sql;

import dataaccess.DataAccessException;
import dataaccess.UserDAO;
import model.UserData;
import dataaccess.DatabaseManager;
import java.sql.SQLException;

public class SQLUserDAO implements UserDAO {

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("SELECT username, password, email FROM user WHERE username = ?")) {
                preparedStatement.setString(1, username);
                try (var resultSet = preparedStatement.executeQuery()){
                    if(resultSet.next()){
                        return new UserData(
                                resultSet.getString("username"),
                                resultSet.getString("password"),
                                resultSet.getString("email"));
                    }
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("could not get user: " + e.getMessage());
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
            try(var connection = DatabaseManager.getConnection()) {
                try (var preparedStatement = connection.prepareStatement("INSERT INTO user (username, password, email) VALUES (?,?,?)")) {
                    preparedStatement.setString(1, userData.username());
                    preparedStatement.setString(2, userData.password());
                    preparedStatement.setString(3, userData.email());
                    preparedStatement.executeUpdate();
                }
            }
            catch (SQLException e) {
                throw new DataAccessException("could not create user: " +e.getMessage());
            }
        }

    @Override
    public void clear() throws DataAccessException {
        try(var connection = DatabaseManager.getConnection()) {
            try (var preparedStatement = connection.prepareStatement("DELETE FROM user")) {
                preparedStatement.executeUpdate();
            }
        }
        catch (SQLException e) {
            throw new DataAccessException("could not clear users: " +e.getMessage());
        }
    }
}
