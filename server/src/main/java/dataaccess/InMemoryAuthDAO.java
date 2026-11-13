package dataaccess;

import model.AuthData;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAuthDAO implements AuthDAO {
    private final Map<String, AuthData> auth = new HashMap<>();

    @Override
    public void clear() {
        auth.clear();
    }
}
