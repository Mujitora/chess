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
}
