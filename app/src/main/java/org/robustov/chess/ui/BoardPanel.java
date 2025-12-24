package org.robustov.chess.ui;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.Position;
import org.robustov.chess.model.Square;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.PieceType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class BoardPanel extends JPanel {
  private final Board board;
  private final int squareSize = 40;
  private Position selectedPosition = null;
  private Set<Position> validMoves = null;

  public BoardPanel(Board board) {
    this.board = board;
    setPreferredSize(new Dimension(16 * squareSize, 16 * squareSize + 30));
    setBackground(java.awt.Color.DARK_GRAY);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        if (e.getY() > 16 * squareSize) {
          return;
        }

        int col = e.getX() / squareSize;
        int row = 15 - (e.getY() / squareSize);

        if (col >= 0 && col < 16 && row >= 0 && row < 16) {
          char file = (char) ('a' + col);
          int rank = row + 1;
          Position position = new Position(file, rank);

          if (!board.isLegalPosition(position)) {
            return;
          }

          if (selectedPosition == null) {
            if (board.hasPiece(position)) {
              Piece piece = board.getPiece(position).get();
              if (piece.getColor() == board.getCurrentPlayer()) {
                selectedPosition = position;
                validMoves = piece.getValidMoves(position, board);
                repaint();
              }
            }
          } else {
            if (validMoves != null && validMoves.contains(position)) {
              try {
                board.movePiece(selectedPosition, position);
              } catch (Exception ex) {
                System.err.println("Move failed: " + ex.getMessage());
              }
            }
            selectedPosition = null;
            validMoves = null;
            repaint();
          }
        }
      }
    });
  }

  @Override
  protected void paintComponent(Graphics g) {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    for (int row = 0; row < 16; row++) {
      for (int col = 0; col < 16; col++) {
        char file = (char) ('a' + col);
        int rank = 16 - row;
        Position position = new Position(file, rank);
        Square square = board.getSquare(position);

        int x = col * squareSize;
        int y = row * squareSize;

        if (square.isLegal()) {
          java.awt.Color squareColor = (row + col) % 2 == 0 ? new java.awt.Color(240, 217, 181)
              : new java.awt.Color(181, 136, 99);
          g2d.setColor(squareColor);
          g2d.fillRect(x, y, squareSize, squareSize);

          g2d.setColor(java.awt.Color.BLACK);
          g2d.drawRect(x, y, squareSize, squareSize);

          if (validMoves != null && validMoves.contains(position)) {
            g2d.setColor(new java.awt.Color(0, 200, 0, 150));
            int dotSize = squareSize / 3;
            int dotX = x + (squareSize - dotSize) / 2;
            int dotY = y + (squareSize - dotSize) / 2;
            g2d.fillOval(dotX, dotY, dotSize, dotSize);
          }

          if (board.hasPiece(position)) {
            Piece piece = board.getPiece(position).get();
            renderPiece(g2d, piece, x, y, squareSize);
          }

          if (selectedPosition != null && selectedPosition.equals(position)) {
            g2d.setColor(new java.awt.Color(255, 255, 0, 100));
            g2d.fillRect(x, y, squareSize, squareSize);
          }
        } else {
          g2d.setColor(java.awt.Color.LIGHT_GRAY);
          g2d.fillRect(x, y, squareSize, squareSize);
        }
      }
    }

    drawStatusBar(g2d);
  }

  private void renderPiece(Graphics2D g2d, Piece piece, int x, int y, int size) {
    java.awt.Color pieceColor = getPlayerColor(piece.getColor());
    PieceType pieceType = piece.getType();
    int centerX = x + size / 2;
    int centerY = y + size / 2;
    int pieceSize = size * 3 / 4;

    g2d.setColor(pieceColor);

    switch (pieceType) {
      case PAWN:
        g2d.fillOval(centerX - pieceSize / 4, centerY - pieceSize / 4, pieceSize / 2, pieceSize / 2);
        break;
      case KNIGHT:
        int[] triangleX = { centerX, centerX - pieceSize / 3, centerX + pieceSize / 3 };
        int[] triangleY = { centerY - pieceSize / 4, centerY + pieceSize / 4, centerY + pieceSize / 4 };
        g2d.fillPolygon(triangleX, triangleY, 3);
        break;
      case BISHOP:
        g2d.fillOval(centerX - pieceSize / 4, centerY - pieceSize / 4, pieceSize / 2, pieceSize / 2);
        g2d.drawLine(centerX, centerY - pieceSize / 3, centerX, centerY + pieceSize / 3);
        break;
      case ROOK:
        g2d.fillRect(centerX - pieceSize / 4, centerY - pieceSize / 4, pieceSize / 2, pieceSize / 2);
        break;
      case QUEEN:
        g2d.fillOval(centerX - pieceSize / 3, centerY - pieceSize / 3, pieceSize * 2 / 3, pieceSize * 2 / 3);
        break;
      case KING:
        g2d.fillRect(centerX - pieceSize / 6, centerY - pieceSize / 3, pieceSize / 3, pieceSize / 2);
        g2d.fillOval(centerX - pieceSize / 6, centerY - pieceSize / 2, pieceSize / 3, pieceSize / 3);
        break;
    }

    g2d.setColor(java.awt.Color.BLACK);
    g2d.setStroke(new BasicStroke(1));
  }

  private void drawStatusBar(Graphics2D g2d) {
    int statusBarY = 16 * squareSize;
    g2d.setColor(java.awt.Color.DARK_GRAY);
    g2d.fillRect(0, statusBarY, getWidth(), 30);

    g2d.setColor(java.awt.Color.WHITE);
    g2d.setFont(new Font("SansSerif", Font.BOLD, 16));

    String currentPlayerName = board.getCurrentPlayer().name();
    java.awt.Color playerColor = getPlayerColor(board.getCurrentPlayer());

    g2d.setColor(playerColor);
    g2d.drawString("Current Turn: " + currentPlayerName, 10, statusBarY + 20);
  }

  private java.awt.Color getPlayerColor(Color color) {
    return switch (color) {
      case YELLOW -> new java.awt.Color(255, 255, 0); // Yellow
      case BLUE -> new java.awt.Color(0, 0, 255); // Blue
      case RED -> new java.awt.Color(255, 0, 0); // Red
      case GREEN -> new java.awt.Color(0, 128, 0); // Green
    };
  }
}
