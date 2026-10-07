package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board;
    private TeamColor currentTurn;
    private boolean whiteKingMoved = false;
    private boolean blackKingMoved = false;
    private boolean whiteRook1Moved = false;
    private boolean whiteRook2Moved = false;
    private boolean blackRook1Moved = false;
    private boolean blackRook2Moved = false;
    private ChessMove lastMove = null;


    public ChessGame() {
        this.board = new ChessBoard();
        this.board.resetBoard();
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

        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            castlingMoves(startPosition, piece.getTeamColor(), legalMoves);
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

        updateTracking(start, currentPiece);
        int dif = end.getColumn() - start.getColumn();
        if (currentPiece.getPieceType() == ChessPiece.PieceType.KING){
            if (dif ==2 || dif == -2) {
                doCastling(move);
            }
        }

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
        // has no valid moves (no other pieces can block) and the king is in check
        return isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        // the king is not in check, but there are no valid moves
        return !isInCheck(teamColor) && noValidMoves(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        whiteKingMoved = false;
        blackKingMoved = false;
        whiteRook1Moved = false;
        whiteRook2Moved = false;
        blackRook1Moved = false;
        blackRook2Moved = false;
        lastMove = null;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    private boolean noValidMoves (TeamColor teamColor) {
        for (int row =1; row <= 8; row++){
            for (int col =1; col <= 8; col++){
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    if (!validMoves(pos).isEmpty()){
                        return false;
                    }
                }
            }
        }
        return true;
    }

    //Extra Credit
    //Castling
    private void castlingMoves(ChessPosition kingPos, ChessGame.TeamColor teamColor, Collection<ChessMove> moves){
        // check if king has moved
        if (teamColor == TeamColor.WHITE && whiteKingMoved) {
            return;
        }
        if (teamColor == TeamColor.BLACK && blackKingMoved){
            return;
        }

        //check if king is in check
        if (isInCheck(teamColor)){
            return;
        }

        // check if kingside rook (column 8) has moved
        int kingRow = kingPos.getRow();
        boolean rightSideRookMoved = false;
        if (teamColor == TeamColor.WHITE){
            rightSideRookMoved = whiteRook2Moved;
        }else {
            rightSideRookMoved = blackRook2Moved;
        }

        if (!rightSideRookMoved){
            ChessPosition rookPos = new ChessPosition(kingRow,8);
            ChessPiece rook = board.getPiece(rookPos);

            if (rook != null && rook.getPieceType() == ChessPiece.PieceType.ROOK
                    && rook.getTeamColor() == teamColor){
                //check between king and rook
                ChessPosition square6 = new ChessPosition(kingRow, 6);
                ChessPosition square7 = new ChessPosition(kingRow, 7);

                if (board.getPiece(square6) == null && board.getPiece(square7) == null){
                    boolean square6Clear = !isSquareAttacked(square6, teamColor);
                    boolean square7Clear = !isSquareAttacked(square7, teamColor);

                    if (square6Clear && square7Clear){
                        ChessMove castleMove = new ChessMove(kingPos, square7, null);
                        moves.add(castleMove);
                    }
                }
            }
        }

        // otherside
        boolean leftSideRookMoved = false;
        if (teamColor == TeamColor.WHITE){
            leftSideRookMoved = whiteRook1Moved;
        }else {
            leftSideRookMoved = blackRook1Moved;
        }

        if (!leftSideRookMoved){
            ChessPosition rookPos = new ChessPosition(kingRow,1);
            ChessPiece rook = board.getPiece(rookPos);

            if (rook != null && rook.getPieceType() == ChessPiece.PieceType.ROOK
                    && rook.getTeamColor() == teamColor){
                //check between king and rook
                ChessPosition square2 = new ChessPosition(kingRow, 2);
                ChessPosition square3 = new ChessPosition(kingRow, 3);
                ChessPosition square4 = new ChessPosition(kingRow, 4);

                if (board.getPiece(square2) == null && board.getPiece(square3) == null
                        && board.getPiece(square4) == null){
                    boolean square2Clear = !isSquareAttacked(square2, teamColor);
                    boolean square3Clear = !isSquareAttacked(square3, teamColor);
                    boolean square4Clear = !isSquareAttacked(square4, teamColor);

                    if (square2Clear && square3Clear && square4Clear){
                        ChessMove castleMove = new ChessMove(kingPos, square3, null);
                        moves.add(castleMove);
                    }
                }
            }
        }
    }

    private boolean isSquareAttacked(ChessPosition square, ChessGame.TeamColor teamColor) {
        // loop through squares on board
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);

                // skip empty squares
                if (piece == null) {
                    continue;
                }
                if (piece.getTeamColor() == teamColor) {
                    continue;
                }

                // check enemy pieces
                Collection<ChessMove> enemyMoves = piece.pieceMoves(board, pos);
                for (ChessMove move : enemyMoves) {
                    if (move.getEndPosition().equals(square)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private void doCastling(ChessMove move) {
        int startRow = move.getStartPosition().getRow();
        int kingEndCol = move.getEndPosition().getColumn();

        if (kingEndCol == 7) {
            // move rook from column 8 to column 6
            ChessPosition oldRookPos = new ChessPosition(startRow, 8);
            ChessPosition newRookPos = new ChessPosition(startRow, 6);
            ChessPiece rook = board.getPiece(oldRookPos);
            board.addPiece(newRookPos, rook);
            board.addPiece(oldRookPos, null);
        }

        //other side
        if (kingEndCol == 3) {
            // move rook from column 1 to column 4
            ChessPosition oldRookPos = new ChessPosition(startRow, 1);
            ChessPosition newRookPos = new ChessPosition(startRow, 4);
            ChessPiece rook = board.getPiece(oldRookPos);
            board.addPiece(newRookPos, rook);
            board.addPiece(oldRookPos, null);
        }
    }
    private void updateTracking(ChessPosition start, ChessPiece piece) {
        // if king moved
        if (piece.getPieceType() == ChessPiece.PieceType.KING) {
            if (piece.getTeamColor() == TeamColor.WHITE) {
                whiteKingMoved = true;
            } else {
                blackKingMoved = true;
            }
        }

        // if rook moved
        if (piece.getPieceType() == ChessPiece.PieceType.ROOK) {
            //whiteRook
            if (start.getRow() == 1 && start.getColumn() == 1) {
                whiteRook1Moved = true;
            }
            if (start.getRow() == 1 && start.getColumn() == 8) {
                whiteRook2Moved = true;
            }
            if (start.getRow() == 8 && start.getColumn() == 1) {
                blackRook1Moved = true;
            }
            if (start.getRow() == 8 && start.getColumn() == 8) {
                blackRook2Moved = true;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currentTurn == chessGame.currentTurn;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currentTurn);
    }
}



