package dataaccess;

import java.util.HashMap;
import java.util.Map;

public class InMemoryAuthDAO implements AuthDAO {
    private final Map<String, String> auth = new HashMap<>();

    @Override
    public void clear() {
        auth.clear();
    }
}
