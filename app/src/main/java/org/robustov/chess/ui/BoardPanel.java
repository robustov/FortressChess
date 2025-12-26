package org.robustov.chess.ui;

import org.robustov.chess.model.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Set;

public class BoardPanel extends JPanel {
  private final Board board;
  private final int squareSize = 45;
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
            PieceRenderer.renderPiece(g2d, piece.getType(), piece.getColor(),
                x + 2, y + 2, squareSize - 4);
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

  private void drawStatusBar(Graphics2D g2d) {
    int statusBarY = 16 * squareSize;
    g2d.setColor(java.awt.Color.DARK_GRAY);
    g2d.fillRect(0, statusBarY, getWidth(), 30);

    g2d.setColor(java.awt.Color.WHITE);
    g2d.setFont(new Font("SansSerif", Font.BOLD, 16));

    String currentPlayerName = board.getCurrentPlayer().name();
    java.awt.Color playerColor = PieceRenderer.getPlayerColor(board.getCurrentPlayer());

    g2d.setColor(playerColor);
    g2d.drawString("Current Turn: " + currentPlayerName, 10, statusBarY + 20);
  }
}
