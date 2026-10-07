package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor currentTurn;

    public ChessGame() {
        this.board = new ChessBoard();
        this.currentTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTurn;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null){
            return null;
        }
        Collection<ChessMove> moves = piece.pieceMoves(board, startPosition);
        Collection<ChessMove> legalMoves = new ArrayList<>();

        //simulate the move
            // move the piece
            //as long as not own color keep

        for (ChessMove move: moves){
            ChessPiece target = board.getPiece(move.getEndPosition());
            board.addPiece(move.getEndPosition(), piece);
            board.addPiece(move.getStartPosition(), null);

            if (!isInCheck(piece.getTeamColor())){
                legalMoves.add(move);
            }

            //undo
            board.addPiece(move.getStartPosition(), piece);
            board.addPiece(move.getEndPosition(), target);
        }
        return legalMoves;
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //Invalid Move Exception if
        // it's not that teams turn
        // it is trying to move outside of their allowed moves
        // there is no piece at the starting position
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promoPiece = move.getPromotionPiece();
        ChessPiece currentPiece = board.getPiece(start);

        if (currentPiece == null){
            throw new InvalidMoveException("No piece at square");
        } else if (currentPiece.getTeamColor() != currentTurn){
            throw new InvalidMoveException("Other team's turn");
        }

        Collection<ChessMove> legalMoves = validMoves(start);
        if (!legalMoves.contains(move)){
            throw new InvalidMoveException("Not a legal move");
        }
        // move piece of board
        // get starting position and then move the piece to the end position
        // edge cases : upgrade
        //switch to the other team

        if (move.getPromotionPiece() != null){
            board.addPiece(end, new ChessPiece(currentTurn, move.getPromotionPiece()));
        }else{
            board.addPiece(end, currentPiece);
        }
        board.addPiece(start, null);

        if (currentTurn == TeamColor.WHITE){
            currentTurn = TeamColor.BLACK;
        } else {
            currentTurn = TeamColor.WHITE;
        }

    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = null;

        //find the king
        for (int row = 1; row <= 8; row ++){
            for (int col = 1; col <=8; col++){
                ChessPosition pos = new ChessPosition (row, col);
                ChessPiece pieceAtPos  = board.getPiece(pos);
                if (pieceAtPos  != null && pieceAtPos.getTeamColor() == teamColor && pieceAtPos.getPieceType() == ChessPiece.PieceType.KING){
                    kingPos = pos;
                }
            }
        }

        //check if any enemy piece can reach the king
            //iterate through the board to find all enemy pieces
        for (int row =1; row <= 8; row ++){
            for (int col = 1; col <=8; col++){
                ChessPosition pos = new ChessPosition (row, col);
                ChessPiece pieceAtPos = board.getPiece(pos);
                if (pieceAtPos != null && pieceAtPos.getTeamColor() != teamColor){
                    //get all the moves for that enemy piece
                    Collection<ChessMove> moves = pieceAtPos.pieceMoves (board, pos);
                    for (ChessMove move: moves){
                        //check if the end point hits the king
                        if (move.getEndPosition().equals(kingPos)){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {

    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }



