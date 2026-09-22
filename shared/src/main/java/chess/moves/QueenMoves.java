package chess.moves;

import chess.ChessBoard;
import chess.ChessPosition;
import chess.ChessMove;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMoves extends StraightMoves {
    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position){
        ArrayList<ChessMove> moves = new ArrayList<>();

        RookMoves rook = new RookMoves();
        moves.addAll(rook.calculateMoves(board,position));

        BishopMoves bishop = new BishopMoves();
        moves.addAll(bishop.calculateMoves(board,position));

        return moves;
    }
}
