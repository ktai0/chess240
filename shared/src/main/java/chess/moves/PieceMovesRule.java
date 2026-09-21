package chess.moves;

import chess.ChessBoard;
import chess.ChessMove;
import chess.ChessPosition;
import java.util.Collection;

public class PieceMovesRule {
    Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position);
}
