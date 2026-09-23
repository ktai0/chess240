package chess.moves;

import chess.ChessBoard;
import chess.ChessPiece;
import chess.ChessPosition;
import chess.ChessMove;
import chess.ChessGame;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves implements PieceMovesRule {

    @Override
    public Collection<ChessMove> calculateMoves(ChessBoard board, ChessPosition position) {
        ArrayList<ChessMove> moves = new ArrayList<>();
        ChessPiece currentPiece = board.getPiece(position);

        if (currentPiece == null) {
            return moves;
        }

        int direction;
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            direction = 1;
        }else{
            direction = -1;
        }
        int startRow;
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            startRow = 2;
        } else{
            startRow = 7;
        }
        int promoRow;
        if (currentPiece.getTeamColor() == ChessGame.TeamColor.WHITE){
            promoRow = 8;
        }else {
            promoRow = 1;
        }

        int row = position.getRow();
        int col = position.getColumn();

        int oneStep = row + direction;
        if (oneStep >= 1 && oneStep<= 8) {
            ChessPosition oneStepVal = new ChessPosition(oneStep, col);

            // Pawns can ONLY move forward into an EMPTY square
            if (board.getPiece(oneStepVal) == null) {
                addMoveOrPromotions(position, oneStepVal, oneStep == promoRow, moves);

                // DOUBLE STEP: Only allowed if on starting row AND 1-step square was empty
                int twoStep = row + (2 * direction);
                if (row == startRow) {
                    ChessPosition twoStepVal = new ChessPosition(twoStep, col);
                    if (board.getPiece(twoStepVal) == null) {
                        addMoveOrPromotions(position, twoStepVal, false, moves);
                    }
                }
            }
        }

        int[] possibleCap = {col - 1, col + 1};
        for (int capCol : possibleCap) {
            if (capCol >= 1 && capCol <= 8 && oneStep >= 1 && oneStep <= 8) {
                ChessPosition capVal = new ChessPosition(oneStep, capCol);
                ChessPiece targetPiece = board.getPiece(capVal);

                // Pawns can ONLY capture if there is an ENEMY piece diagonally
                if (targetPiece != null && targetPiece.getTeamColor() != currentPiece.getTeamColor()) {
                    addMoveOrPromotions(position, capVal, oneStep == promoRow, moves);
                }
            }
        }

        return moves;
    }

    // Helper method to handle standard moves vs. promotion moves
    private void addMoveOrPromotions(ChessPosition start, ChessPosition end, boolean isPromotion, Collection<ChessMove> moves) {
        if (isPromotion) {
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.QUEEN));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.ROOK));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.BISHOP));
            moves.add(new ChessMove(start, end, ChessPiece.PieceType.KNIGHT));
        } else {
            moves.add(new ChessMove(start, end, null));
        }
    }
}