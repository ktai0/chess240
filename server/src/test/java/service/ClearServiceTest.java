package service;

import dataaccess.*;
import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.service.ClearService;


public class ClearServiceTest {
    @Test
    public void clearAllDAO() throws DataAccessException {
        UserDAO users = new InMemoryUserDAO();
        GameDAO games = new InMemoryGameDAO();
        AuthDAO auths = new InMemoryAuthDAO();

        users.createUser(new UserData("jane", "12345", "j@byu.edu"));
        auths.createAuth(new AuthData("29e8di", "jane"));

        ClearService service = new ClearService(users, games, auths);
        service.clear();
        Assertions.assertNull(users.getUser("jane"));
    }

    @Test
    public void testDataAccessExceptionClear() {
        ClearService service = new ClearService(new InMemoryUserDAO(), new InMemoryGameDAO(), new InMemoryAuthDAO());
        Assertions.assertDoesNotThrow(service::clear);
    }
}

