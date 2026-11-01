package dataaccess;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
import chess.InvalidMoveException;
import com.google.gson.Gson;
import dataaccess.sql.DatabaseSetup;
import dataaccess.sql.SQLGameDAO;
import model.GameData;
import org.junit.jupiter.api.*;

import java.util.Collection;

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

    @Test
    public void listGamesPositive() throws DataAccessException{
        GameData game = new GameData(0, "", "", "theGame", new ChessGame());
        int id = gameDAO.createGameData(game);
        GameData game2 = new GameData(0, "", "", "theGame2", new ChessGame());
        int id2 = gameDAO.createGameData(game2);
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertEquals(2, games.size());
    }

    @Test
    public void listGamesNegative() throws DataAccessException{
        Collection<GameData> games = gameDAO.listGames();
        Assertions.assertTrue(games.isEmpty());
    }

    @Test
    public void updateGameAddedPlayer() throws DataAccessException{
        GameData game = new GameData(0, "", "", "theGame", new ChessGame());
        int id = gameDAO.createGameData(game);
        GameData newGame = new GameData(id, "bob", "", "theGame", new ChessGame());
        gameDAO.updateGame(new GameData(id, newGame.whiteUsername(), game.blackUsername(), game.gameName() ,game.game()));
        GameData updatedGame = gameDAO.getGame(id);
        Assertions.assertEquals(updatedGame.whiteUsername(), newGame.whiteUsername());
    }

    @Test
    public void updateGameMovesPersist() throws InvalidMoveException,DataAccessException{
        GameData game = new GameData(0, "bob", "joe", "theGame", new ChessGame());
        int id = gameDAO.createGameData(game);
        game.game().makeMove(new ChessMove(new ChessPosition(2, 5), new ChessPosition(4, 5), null));
        gameDAO.updateGame(new GameData(id, "bob", "joe", "theGame", game.game()));
        GameData updatedGame = gameDAO.getGame(id);
        Assertions.assertEquals(game.game().getBoard(), updatedGame.game().getBoard());
        Assertions.assertEquals(ChessGame.TeamColor.BLACK, updatedGame.game().getTeamTurn());
    }

    @Test
    public void updateGameNotFound() throws DataAccessException{
        GameData game = new GameData(9999, "bob", "joe", "theGame", new ChessGame());
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(new GameData(game.gameID(), "","","",new ChessGame())));
    }


}
