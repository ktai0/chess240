package server.service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.InvalidRequestException;
import dataaccess.AlreadyTakenException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.AuthData;
import model.UserData;
import model.request.RegisterRequest;
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
        if (request.username() == null || request.password() == null || request.email() == null) {
            throw new InvalidRequestException("Error: missing information");
        }
        if(userDAO.getUser(request.username()) != null ){
            throw new AlreadyTakenException ("Error: already taken");
        }
        UserData user = new UserData(request.username(), request.password(), request.email());
        userDAO.createUser(user);

        String authToken = UUID.randomUUID().toString();
        authDAO.createAuth(new AuthData(authToken, user.username()));

        return new RegisterResult(request.username(), authToken);

    }
}
