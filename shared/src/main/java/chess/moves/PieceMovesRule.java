package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;

public interface PieceMovesRule {
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position);
}
