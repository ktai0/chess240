package chess.moves;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;

public class KnightMoves extends JumpMoves {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        int[][] knightMoves = {
                {-1,-2},{-2,-1},{-1,2},{-2,1},{1,-2},{1,2},{2,-1},{2,1}
        };
        return calculateJumpMoves(board,position,knightMoves);
    }
}
