package chess.moves;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.Collection;

public class BishopMoves extends StraightMoves {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        int[][] bishopMoves = {
                {1,1}, {1,-1},{-1,-1},{-1,1}
        };
        return calculateStraightMoves(board,position,bishopMoves);
    }
}
