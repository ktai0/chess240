package chess.moves;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;

public class KingMoves extends JumpMoves {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        int[][] kingMoves = {
                {1,0},{1,1},{0,1},{-1,1},{-1,0},{-1,-1},{0,-1},{1,-1},
        };
        return calculateJumpMoves(board,position,kingMoves);
    }
}
