package server;

import dataaccess.*;
import io.javalin.*;
import model.UserData;
import service.*;
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

        UserService userService = new UserService(userDAO, authDAO);
        javalin.post("/user", ctx->{
           UserData userData = new Gson().fromJson(ctx.body(), UserData.class);
           RegisterResult result = userService.register(userData);
           ctx.result(new Gson().toJson(result));
        });
        javalin.exception(BadRequestException.class, (e, ctx)->{
            ctx.status(400);
            ctx.result(new Gson().toJson(Map.of("message", "Error: " + e.getMessage())));
        });
        javalin.exception(AlreadyTakenException.class, (e, ctx)->{
            ctx.status(403);
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
