package dataaccess;
import model.GameData;

import java.util.HashMap;
import java.util.Map;

public class InMemoryGameDAO implements GameDAO{
    private final Map<Integer, GameData> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }
}
