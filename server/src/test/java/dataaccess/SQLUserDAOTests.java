package dataaccess;

import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SQLUserDAO;
import model.UserData;
import org.junit.jupiter.api.*;

public class SQLUserDAOTests {
    private UserDAO userDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        DatabaseSetup.createTable();
        userDAO = new SQLUserDAO();
        userDAO.clear();
    }

    @Test
    public void createUserPositive() throws DataAccessException{
        UserData user = new UserData("bob", "password", "bob@bob.com");
        userDAO.createUser(user);
        UserData recievedData = userDAO.getUser("bob");
        Assertions.assertEquals(user, recievedData);
    }

    @Test
    public void createUserDuplicateUsername() throws DataAccessException {
        userDAO.createUser(new UserData("bob", "password", "bob@bob.com"));
        Assertions.assertThrows(DataAccessException.class,
                () -> userDAO.createUser(new UserData("bob", "password2", "notbob@bob.com")));
    }

    @Test
    public void getUserNotFound() throws DataAccessException {
        Assertions.assertNull(userDAO.getUser("nothing"));
    }

    @Test
    public void clearRemovesUsers() throws DataAccessException {
        userDAO.createUser(new UserData("bob", "password", "mhawk@byu.edu"));
        userDAO.clear();
        Assertions.assertNull(userDAO.getUser("bob"));
    }

}
