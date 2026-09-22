package chess.moves;

import chess.ChessMove;
import chess.ChessPosition;
import chess.ChessBoard;
import chess.ChessPiece;

import java.util.Collection;
import java.util.ArrayList;

public abstract class StraightMoves implements PieceMovesRule {
    public Collection<ChessMove> calculateStraightMoves(ChessBoard board, ChessPosition position, int[][] directions) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);

        if (currentPiece == null) {
            return moves;
        }

        for (int[] dir : directions) {
            int rowChange = dir[0];
            int columnChange = dir[1];

            int newRow = position.getRow() + rowChange;
            int newColumn = position.getColumn() + columnChange;

            while (newRow >= 1 && newColumn <= 8 && newRow <= 8 && newColumn >= 1) {
                ChessPosition targetPosition = new ChessPosition(newRow, newColumn);
                ChessPiece pieceAtTarget = board.getPiece(targetPosition);

                if (pieceAtTarget == null) {
                    moves.add(new ChessMove(position, targetPosition, null));
                } else if (pieceAtTarget.getTeamColor() != currentPiece.getTeamColor()) {
                    moves.add(new ChessMove(position, targetPosition, null));
                    break;
                } else {
                    break;
                }

                newRow += rowChange;
                newColumn += columnChange;
            }
        }
        return moves;
    }
}