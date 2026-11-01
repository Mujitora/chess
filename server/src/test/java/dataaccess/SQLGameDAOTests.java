package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.*;

public class SQLGameDAOTests {
    private GameDAO gameDAO;

    @BeforeEach
    public void setup() throws DataAccessException{
        DatabaseSetup.createTable();
        gameDAO = new SQLGameDAO();
        gameDAO.clear();
    }

    @Test
    public void jsonStuffWorks() {
        ChessGame game = new ChessGame();
        String json = new Gson().toJson(game);
        ChessGame restored = new Gson().fromJson(json, ChessGame.class);
        Assertions.assertEquals(game.getBoard(), restored.getBoard());
        Assertions.assertEquals(game.getTeamTurn(), restored.getTeamTurn());
    }

    @Test
    public void createGamePositive() throws DataAccessException{
        GameData game = new GameData(0, "", "", "theGame", new ChessGame());
        int id = gameDAO.createGameData(game);
        GameData receivedGame = gameDAO.getGame(id);
        Assertions.assertEquals("theGame", receivedGame.gameName());
        Assertions.assertEquals(new ChessGame().getBoard(), receivedGame.game().getBoard());
    }


    @Test
    public void createGameIdsAreDifferent() throws DataAccessException{
        GameData game = new GameData(0, "", "", "theGame", new ChessGame());
        int id = gameDAO.createGameData(game);
        GameData game2 = new GameData(0, "", "", "theGame2", new ChessGame());
        int id2 = gameDAO.createGameData(game2);
        Assertions.assertNotEquals(id, id2);
    }

    @Test
    public void createGameNoName()throws DataAccessException{
        GameData game = new GameData(0, null, null, null, new ChessGame());
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.createGameData(game));
    }

    @Test
    public void getGameGameNotFound() throws DataAccessException{
        Assertions.assertNull(gameDAO.getGame(8989));
    }


}
