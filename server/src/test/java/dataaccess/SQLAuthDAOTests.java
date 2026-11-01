package dataaccess;
import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.*;

public class SQLAuthDAOTests {
    private AuthDAO authDAO;

    @BeforeEach
    public void setup() throws DataAccessException {
        DatabaseSetup.createTable();
        authDAO = new SQLAuthDAO();
        authDAO.clear();
    }

    @Test
    public void createAuthPositive() throws DataAccessException {
        AuthData auth = new AuthData("123","bob");
        authDAO.createAuth(auth);
        AuthData receivedAuth = authDAO.getAuth("123");
        Assertions.assertEquals(auth, receivedAuth);
    }

    @Test
    public void createAuthDuplicate() throws DataAccessException{
        AuthData auth = new AuthData("123","bob");
        authDAO.createAuth(auth);
        Assertions.assertThrows(DataAccessException.class,
                () -> authDAO.createAuth(new AuthData("123", "notbob")));
    }

    @Test
    public void getAuthNotFound() throws DataAccessException{
        Assertions.assertNull(authDAO.getAuth("890"));
    }

    @Test
    public void deleteAuthPositive() throws DataAccessException{
        AuthData auth = new AuthData("123","bob");
        authDAO.createAuth(auth);
        authDAO.deleteAuth("123");
        Assertions.assertNull(authDAO.getAuth("123"));
    }

    @Test
    public void deleteAuthWithoutDeletingOthers() throws DataAccessException{
        AuthData auth = new AuthData("123","bob");
        authDAO.createAuth(auth);
        AuthData auth2 = new AuthData("456","james");
        authDAO.createAuth(auth2);
        authDAO.deleteAuth("123");
        AuthData recieveAuth = authDAO.getAuth("456");
        Assertions.assertEquals(auth2, recieveAuth);
    }

    @Test
    public void clearRemovesAuth() throws DataAccessException{
        AuthData auth = new AuthData("123","bob");
        authDAO.createAuth(auth);
        authDAO.clear();
        Assertions.assertNull(authDAO.getAuth("123"));
    }
}
