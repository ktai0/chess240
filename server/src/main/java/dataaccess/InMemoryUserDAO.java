package dataaccess;

import model.UserData;

import java.util.HashMap;
import java.util.Map;

public class InMemoryUserDAO implements UserDAO {
    private final Map<String, UserData> users = new HashMap<>();

    @Override
    public void clear() {
        users.clear();
    }

    @Override
    public UserData getUser(String username){
        return users.get(username);
    }

    @Override
    public void createUser(UserData userData){
        users.put(userData.username(), userData);
    }
}
