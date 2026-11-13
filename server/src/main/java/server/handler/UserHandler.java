package server.handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import server.service.UserService;

public record UserHandler(UserService userService) {
    public void register(Context ctx) throws DataAccessException {
        userService.register();
        ctx.status(200);
        ctx.contentType(string);
        ctx.result("{username}"",""authToken");
    }
}
