package server.handler;

import com.google.gson.Gson;
import dataaccess.AlreadyTakenException;
import dataaccess.DataAccessException;
import dataaccess.InvalidRequestException;
import io.javalin.http.Context;
import model.request.RegisterRequest;
import model.result.RegisterResult;
import server.service.UserService;

public record UserHandler(UserService userService) {
    private static final Gson GSON = new Gson();
    public void register(Context ctx) throws DataAccessException, InvalidRequestException, AlreadyTakenException {
        // ctx -> Gson-> so that it becomes a registerRequest
        RegisterRequest request = GSON.fromJson(ctx.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result(GSON.toJson(result));
    }
}
