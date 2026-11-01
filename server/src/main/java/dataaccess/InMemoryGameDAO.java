package dataaccess;
import chess.ChessGame;

import java.util.HashMap;
import java.util.Map;

public class InMemoryGameDAO implements GameDAO{
    private final Map<Integer, ChessGame> games = new HashMap<>();

    @Override
    public void clear() {
        games.clear();
    }
}
