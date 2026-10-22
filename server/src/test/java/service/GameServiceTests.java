package service;

import dataaccess.*;
import model.AuthData;
import org.junit.jupiter.api.*;

public class GameServiceTests {
    private AuthDAO authDAO;
    private GameDAO gameDAO;
    private GameService service;


    @BeforeEach
    public void setup() throws DataAccessException {
        authDAO = new MemoryAuthDAO();
        gameDAO = new MemoryGameDAO();
        service = new GameService(authDAO, gameDAO);
        authDAO.createAuth(new AuthData("123", "marshall"));
    }
    @Test
    public void createGamePositive() throws Exception{
        CreateGameResult result = service.createGame("123", new CreateGameRequest("test game"));
        Assertions.assertNotNull(gameDAO.getGame(result.gameID()));
    }


    @Test
    public void createGameWrongToken() throws Exception{
        Assertions.assertThrows(UnauthorizedException.class, () -> service.createGame("wrongToken", new CreateGameRequest("test game")));
    }

    @Test
    public void listGamesPositive() throws Exception{
        service.createGame("123", new CreateGameRequest("test game"));
        ListGamesResult result = service.listGames("123");
        Assertions.assertEquals(1, result.games().size());
    }

    @Test
    public void listGamesWrongToken() throws Exception{
        service.createGame("123", new CreateGameRequest("test game"));
        Assertions.assertThrows(UnauthorizedException.class, () -> service.listGames("805"));
    }

    @Test
    public void joinGamePositive() throws Exception{
        CreateGameResult gameResult = service.createGame("123", new CreateGameRequest("test game"));
        service.joinGame("123", new JoinGameRequest("WHITE", gameResult.gameID()));
        Assertions.assertEquals("marshall", gameDAO.getGame(gameResult.gameID()).whiteUsername());

    }

    @Test
    public void joinGameColorTaken() throws Exception{
        CreateGameResult gameResult = service.createGame("123", new CreateGameRequest("test game"));
        service.joinGame("123", new JoinGameRequest("WHITE", gameResult.gameID()));
        Assertions.assertThrows(AlreadyTakenException.class, () -> service.joinGame("123", new JoinGameRequest("WHITE", gameResult.gameID())));

    }

}
