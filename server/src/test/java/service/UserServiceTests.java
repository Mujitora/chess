package service;

import dataaccess.*;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;

public class UserServiceTests {
    private UserDAO userDAO;
    private UserService service;
    private AuthDAO authDAO;


    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        authDAO = new MemoryAuthDAO();
        service = new UserService(userDAO, authDAO);
    }

    @Test
    public void registerPositive() throws Exception{
        AuthResult result = service.register(new UserData("marshall", "password", "mhawk@byu.edu"));
        Assertions.assertEquals("marshall", result.username());
        Assertions.assertNotNull(authDAO.getAuth(result.authToken()));
    }

    @Test
    public void registerDuplicate() throws Exception{
        service.register(new UserData("marshall", "password", "mhawk@byu.edu"));
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.register(new UserData("marshall", "password", "mhawk@byu.edu")));
    }


}
