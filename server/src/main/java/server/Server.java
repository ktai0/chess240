package server;

import io.javalin.*;
import com.google.gson.Gson;
import server.handler.ClearHandler;
import server.service.ClearService;
import dataaccess.AuthDAO;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import dataaccess.DataAccessException;
import dataaccess.InMemoryAuthDAO;
import dataaccess.InMemoryGameDAO;
import dataaccess.InMemoryUserDAO;
import java.util.Map;


public class Server {

    private final Javalin javalin;

    public Server() {
        javalin = Javalin.create(config -> config.staticFiles.add("web"));

        UserDAO userDAO = new InMemoryUserDAO();
        GameDAO gameDAO = new InMemoryGameDAO();
        AuthDAO authDAO = new InMemoryAuthDAO();

        ClearService clearService = new ClearService(userDAO,gameDAO, authDAO);
        ClearHandler clearHandler = new ClearHandler(clearService);

        javalin.delete("/db", clearHandler::clear);

        javalin.exception(DataAccessException.class, (e, ctx) -> {
            ctx.status(500);
            ctx.contentType("application/json");
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
