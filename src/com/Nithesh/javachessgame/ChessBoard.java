package com.Nithesh.javachessgame;

public class ChessBoard {
    private Piece[][] board;

    public ChessBoard() {
        this.board = new Piece[8][8]; // 8x8 chessboard

        setupPieces();
    }

    public Piece[][] getBoard() {
        return board;
    }

    public Piece getPiece(int row, int column) {
        return board[row][column];
    }

    public void setPiece(int row, int column, Piece piece) {
        board[row][column] = piece;
        if (piece != null) {
            piece.setPosition(new Position(row, column));
        }
    }

    private void setupPieces() {
        // a b c d e f g h
        // 8 R K B Q K B K R
        // 7 P P P P P P P P
        // 6 . . . . . . . .
        // 5 . . . . . . . .
        // 4 . . . . . . . .
        // 3 . . . . . . . .
        // 2 P P P P P P P P
        // 1 R K B Q K B K R

        // Place Rooks
        board[0][0] = new Rook(PieceColor.BLACK, new Position(0, 0)); // a8
        board[0][7] = new Rook(PieceColor.BLACK, new Position(0, 7)); // h8
        board[7][0] = new Rook(PieceColor.WHITE, new Position(7, 0)); // a1
        board[7][7] = new Rook(PieceColor.WHITE, new Position(7, 7)); // h1

        // Place Knights
        board[0][1] = new Knight(PieceColor.BLACK, new Position(0, 1));
        board[0][6] = new Knight(PieceColor.BLACK, new Position(0, 6));
        board[7][1] = new Knight(PieceColor.WHITE, new Position(7, 1));
        board[7][6] = new Knight(PieceColor.WHITE, new Position(7, 6));

        // Place Bishops
        board[0][2] = new Bishop(PieceColor.BLACK, new Position(0, 2));
        board[0][5] = new Bishop(PieceColor.BLACK, new Position(0, 5));
        board[7][2] = new Bishop(PieceColor.WHITE, new Position(7, 2));
        board[7][5] = new Bishop(PieceColor.WHITE, new Position(7, 5));

        // Place Queens
        board[0][3] = new Queen(PieceColor.BLACK, new Position(0, 3));
        board[7][3] = new Queen(PieceColor.WHITE, new Position(7, 3));

        // Place Kings
        board[0][4] = new King(PieceColor.BLACK, new Position(0, 4));
        board[7][4] = new King(PieceColor.WHITE, new Position(7, 4));

        // Place Pawns
        for (int i = 0; i < 8; i++) {
            board[1][i] = new Pawn(PieceColor.BLACK, new Position(1, i));
            board[6][i] = new Pawn(PieceColor.WHITE, new Position(6, i));
        }
    }

    public void movePiece(Position start, Position end) {
        // Check if there is a piece at the start position and if the move is valid
        if (board[start.getRow()][start.getColumn()] != null &&
                board[start.getRow()][start.getColumn()].isValidMove(end, board)) {

            // Perform the move: place the piece at the end position
            board[end.getRow()][end.getColumn()] = board[start.getRow()][start.getColumn()];

            // Update the piece's position
            board[end.getRow()][end.getColumn()].setPosition(end);

            // Clear the start position
            board[start.getRow()][start.getColumn()] = null;
        }
    }

}
