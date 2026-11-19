package server;

import dataaccess.*;
import io.javalin.*;
import com.google.gson.Gson;
import io.javalin.http.Context;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;
import server.handler.ClearHandler;
import server.handler.UserHandler;
import server.service.ClearService;
import server.service.UserService;

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

        UserService userService = new UserService(userDAO, gameDAO, authDAO);
        UserHandler userHandler = new UserHandler(userService);

        javalin.delete("/db", clearHandler::clear);
        javalin.post("/user", ctx -> {
            //ctx body (as a class)-> register request class ->
            try {
                RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);
                RegisterResult result = userService.register(request);
                ctx.status(200).json(result);
            } catch(AlreadyTakenException e) {
                ctx.status(403).json(Map.of("message", "Error: username already taken"));
            } catch (InvalidRequestException e){
                ctx.status(401).json(Map.of("message", "Error: invalid request"));
            } catch (DataAccessException e){
                ctx.status(500).json(Map.of("message","Error: Data Access Exception"));
            }
        });

        javalin.post("/session", ctx -> {
            //ctx body (as a class)-> register request class -
            try {
                LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
                LoginResult result = userService.login(request);
                ctx.status(200).json(result);
            } catch (InvalidRequestException e){
                ctx.status(401).json(Map.of("message", "Error: invalid request"));
            } catch (DataAccessException e){
                ctx.status(400).json(Map.of("message","Error: Data Access Exception"));
            } catch (UnauthException e){
                ctx.status(404).json(Map.of("message", "Error: Incorrect user data"));
            }
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
