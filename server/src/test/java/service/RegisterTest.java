package service;

import dataaccess.*;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import server.service.UserService;

public class RegisterTest {
    @Test
    public void registerSuccess() throws Exception{
        //register with userData
        UserService service = new UserService(new InMemoryUserDAO(), new InMemoryGameDAO(), new InMemoryAuthDAO());
        RegisterRequest request = new RegisterRequest("jane", "12345", "j@byu.edu");
        //call using username
        RegisterResult result = service.register(request);
        //assert equals
        Assertions.assertEquals("jane", result.username());
        Assertions.assertNotNull(result.authToken());
    }

    @Test
    public void alreadyTaken() throws Exception{
        //register with userData
        //register with same username
        //assert equals

        UserService service = new UserService(new InMemoryUserDAO(), new InMemoryGameDAO(), new InMemoryAuthDAO());
        RegisterRequest request = new RegisterRequest("jane", "12345", "j@byu.edu");
        service.register(request);

        Assertions.assertThrows(AlreadyTakenException.class, () ->service.register(request));
    }

    @Test
    public void nullInfo() throws Exception{
        //register with missing userdata
        //assert equals
        UserService service = new UserService(new InMemoryUserDAO(), new InMemoryGameDAO(), new InMemoryAuthDAO());
        RegisterRequest request = new RegisterRequest("jane", "12345", null);

        Assertions.assertThrows(InvalidRequestException.class, () ->service.register(request));
    }
}
