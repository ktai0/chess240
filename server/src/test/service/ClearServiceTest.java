package service;

import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.service.ClearService;

import java.util.HashSet;
import java.util.Set;

public class ClearServiceTest {
    @Test
    public void clearAllDAO() throws DataAccessException{
        Set<String> cleared = new HashSet<>();
        UserDAO users = () -> cleared.add("users");
        GameDAO games = () -> cleared.add("games");
        AuthDAO auths = () -> cleared.add("auths");

        new ClearService(users, games, auths).clear();

        Assertions.assertEquals(Set.of("users", "games", "auths"), cleared);
    }

    @Test
    public void testDataAccessExceptionClear(){
        UserDAO users = () -> {throw new DataAccessException("database down");};
        GameDAO games = () -> { };
        AuthDAO auths = () -> { };

        ClearService service = new ClearService(users, games, auths);

        Assertions.assertThrows(DataAccessException.class, service::clear);
        }
    }

