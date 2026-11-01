package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import org.junit.jupiter.api.*;

public class SQLGameDAOTests {

    @Test
    public void jsonStuffWorks() {
        ChessGame game = new ChessGame();
        String json = new Gson().toJson(game);
        ChessGame restored = new Gson().fromJson(json, ChessGame.class);
        Assertions.assertEquals(game.getBoard(), restored.getBoard());
        Assertions.assertEquals(game.getTeamTurn(), restored.getTeamTurn());
    }
}
