package server.service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import model.request.LoginRequest;
import model.request.RegisterRequest;
import model.result.LoginResult;
import model.result.RegisterResult;

import java.util.UUID;

public class UserService {
    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public UserService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.authDAO = authDAO;
        this.gameDAO = gameDAO;
    }

    public RegisterResult register(RegisterRequest request) throws DataAccessException, InvalidRequestException, AlreadyTakenException {
        if (request.username == null || request.password == null || request.email == null) {
            throw new InvalidRequestException("Error: missing information");
        }
        if(userDAO.getUser(request.username) != null ){
            throw new AlreadyTakenException ("Error: already taken");
        }
        UserData user = new UserData(request.username, request.password, request.email);
        userDAO.createUser(user);

        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, user.username()));

        return new RegisterResult(request.username, authToken);
    }

    public LoginResult login(LoginRequest request) throws DataAccessException,InvalidRequestException, UnauthException {
        //edge cases -> no password
        if (request.username == null|| request.password == null){
            throw new InvalidRequestException("Error: missing information");
        }

        UserData user = userDAO.getUser(request.username);

        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, user.username()));

        if (!userDAO.getUser(request.username).equals(request.username)){
            throw new UnauthException("Error: Incorrect user data");
        }
        return new LoginResult(request.username, authToken);
    }
}
