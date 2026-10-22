package service;

import chess.ChessGame;
import dataaccess.*;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.*;

public class ClearServiceTests {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private UserDAO userDAO;
    private ClearService service;





    @BeforeEach
    public void setup() {
        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        service = new ClearService(userDAO, gameDAO, authDAO);
    }

    @Test
    public void ClearTest() throws Exception{
        userDAO.createUser(new UserData("marshall", "password", "mhawk@byu.edu"));
        gameDAO.createGameData(new GameData(101, "bob", "joe", "test game", new ChessGame()));
        authDAO.createAuth(new AuthData("321", "bob"));
        service.clear();
        Assertions.assertNull(userDAO.getUser("marshall"));
        Assertions.assertNull(gameDAO.getGame(101));
        Assertions.assertNull(authDAO.getAuth("321"));
    }





}
