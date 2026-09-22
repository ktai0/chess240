package chess.moves;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;

public class RookMoves extends StraightMoves {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        int[][] rookMoves = {
                {1,0},{0,1},{-1,0},{0,-1}
        };
        return calculateStraightMoves(board,position,rookMoves);
    }
}
