package com.Nithesh.javachessgame;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;
import java.util.List;

public class ChessGameGUI extends JFrame {
    private final ChessSquareComponent[][] squares = new ChessSquareComponent[8][8];
    private final ChessGame game = new ChessGame();

    private final Map<Class<? extends Piece>, String> pieceUnicodeMap = new HashMap<>() {
        {
            put(Pawn.class, "P");
            put(Rook.class, "R");
            put(Knight.class, "K");
            put(Bishop.class, "B");
            put(Queen.class, "Q");
            put(King.class, "K");
        }
    };

    public ChessGameGUI() {
        setTitle("Chess Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new GridLayout(8, 8));
        initializeBoard();
        addGameResetOption();
        pack(); // Adjust window size to fit the chessboard
        setVisible(true);
    }

    private void initializeBoard() {
        for (int row = 0; row < squares.length; row++) {
            for (int col = 0; col < squares[row].length; col++) {
                final int finalRow = row;
                final int finalCol = col;
                ChessSquareComponent square = new ChessSquareComponent(row, col);
                square.setFont(new Font("SansSerif", Font.PLAIN, 36)); // Ensure the font supports Unicode
                square.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mouseClicked(MouseEvent e) {
                        handleSquareClick(finalRow, finalCol);
                    }
                });
                add(square);
                squares[row][col] = square;
            }
        }
        refreshBoard();
    }

    private void refreshBoard() {
        ChessBoard board = game.getBoard();
        PieceColor currentPlayer = game.getCurrentPlayerColor(); // This method should return the current player's color
        boolean inCheck = game.isInCheck(currentPlayer);
        Position kingPosition = game.findKingPosition(currentPlayer); // Assume this method returns the king's position

        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                Piece piece = board.getPiece(row, col); // Assuming ChessBoard has a getPiece method
                if (piece != null) {
                    String symbol = pieceUnicodeMap.get(piece.getClass());
                    Color color = (piece.getColor() == PieceColor.WHITE) ? Color.WHITE : Color.BLACK;
                    squares[row][col].setPieceSymbol(symbol, color);

                    if (inCheck && piece instanceof King && piece.getColor() == currentPlayer) {
                        squares[row][col].setBackground(Color.RED);
                    } else {
                        squares[row][col].setBackground(
                                (row + col) % 2 == 0 ? new Color(139, 98, 63) : new Color(196, 162, 106));
                    }
                } else {
                    squares[row][col].clearPieceSymbol(); // Ensure this method clears the square
                    squares[row][col]
                            .setBackground((row + col) % 2 == 0 ? new Color(139, 98, 63) : new Color(196, 162, 106));
                }
            }
        }
    }

    private void handleSquareClick(int row, int col) {
        boolean moveResult = game.handleSquareSelection(row, col);
        clearHighlights();
        if (moveResult) {
            // If a move was made, refresh and check game state without highlighting new
            // moves
            refreshBoard();
            checkGameState();
            checkGameOver();
        } else {
            // If no move was made but a piece is selected, highlight its legal moves
            highlightLegalMoves(new Position(row, col));
        }
    }

    private void checkGameState() {
        PieceColor currentPlayer = game.getCurrentPlayerColor(); // This method should return the current player's color
        boolean inCheck = game.isInCheck(currentPlayer);

        if (inCheck) {
            JOptionPane.showMessageDialog(this, currentPlayer + " is in check!");
        }
    }

    private void highlightLegalMoves(Position position) {
        ChessBoard board = game.getBoard();
        List<Position> legalMoves = game.getLegalMovesForPieceAt(position);
        Piece movingPiece = board.getPiece(position.getRow(), position.getColumn());

        for (Position move : legalMoves) {
            Piece destinationPiece = board.getPiece(move.getRow(), move.getColumn());
            if (destinationPiece != null && destinationPiece.getColor() != movingPiece.getColor()) {
                squares[move.getRow()][move.getColumn()].setBackground(Color.RED);
            } else {
                squares[move.getRow()][move.getColumn()].setBackground(Color.GREEN);
            }
        }
    }

    private void clearHighlights() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                squares[row][col]
                        .setBackground((row + col) % 2 == 0 ? new Color(139, 98, 63) : new Color(196, 162, 106));
            }
        }
    }

    private void addGameResetOption() {
        JMenuBar menuBar = new JMenuBar();
        JMenu gameMenu = new JMenu("Game");
        JMenuItem resetItem = new JMenuItem("Reset");
        resetItem.addActionListener(e -> resetGame());
        gameMenu.add(resetItem);
        menuBar.add(gameMenu);
        setJMenuBar(menuBar);
    }

    private void resetGame() {
        game.resetGame();
        refreshBoard();
    }

    private void checkGameOver() {
        if (game.isCheckmate(game.getCurrentPlayerColor())) {
            int response = JOptionPane.showConfirmDialog(this, "Checkmate! Would you like to play again?", "Game Over",
                    JOptionPane.YES_NO_OPTION);
            if (response == JOptionPane.YES_OPTION) {
                resetGame();
            } else {
                System.exit(0);
            }
        }
    }

    public static void main(String[] args) {
        System.out.println("\u265f");
        SwingUtilities.invokeLater(ChessGameGUI::new);
    }
}
