package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDAO implements UserDAO {
    private final Map<String, String> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }
}
