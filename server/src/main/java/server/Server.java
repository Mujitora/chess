package server;

import dataaccess.*;
import io.javalin.*;
import service.ClearService;
import com.google.gson.Gson;
import java.util.Map;

public class Server {

    private final Javalin javalin;
    private UserDAO userDAO;
    private GameDAO gameDAO;
    private AuthDAO authDAO;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        userDAO = new MemoryUserDAO();
        gameDAO = new MemoryGameDAO();
        authDAO = new MemoryAuthDAO();
        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);
        // Register your endpoints and exception handlers here.
        javalin.delete("/db", ctx->{
            clearService.clear();
            ctx.result("{}");
        });
        javalin.exception(DataAccessException.class, (e, ctx) ->{
            ctx.status(500);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
        });

    }

    public int run(int desiredPort) {
        javalin.start(desiredPort);
        return javalin.port();
    }

    public void stop() {
        javalin.stop();
    }
}
