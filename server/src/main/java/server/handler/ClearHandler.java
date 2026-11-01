package server.handler;

import dataaccess.DataAccessException;
import io.javalin.http.Context;
import server.service.ClearService;

public record ClearHandler(ClearService clearService) {
    public void clear(Context ctx) throws DataAccessException{
        clearService.clear();
        ctx.status(200);
        ctx.contentType("application/json");
        ctx.result("{}");
    }
}
