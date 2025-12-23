package org.robustov.chess.ui;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.Position;
import org.robustov.chess.model.Square;
import org.robustov.chess.model.Color;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class BoardPanel extends JPanel {
  private final Board board;
  private final int squareSize = 40;
  private Position selectedPosition = null;

  public BoardPanel(Board board) {
    this.board = board;
    setPreferredSize(new Dimension(16 * squareSize, 16 * squareSize));
    setBackground(java.awt.Color.DARK_GRAY);

    addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        int col = e.getX() / squareSize;
        int row = 15 - (e.getY() / squareSize);

        if (col >= 0 && col < 16 && row >= 0 && row < 16) {
          char file = (char) ('a' + col);
          int rank = row + 1;
          Position position = new Position(file, rank);

          if (board.isLegalPosition(position)) {
            if (selectedPosition == null) {
              selectedPosition = position;
            } else {
              try {
                board.movePiece(selectedPosition, position);
                selectedPosition = null;
              } catch (Exception ex) {
                selectedPosition = position;
              }
            }
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

          if (board.hasPiece(position)) {
            Piece piece = board.getPiece(position).get();
            g2d.setFont(new Font("SansSerif", Font.BOLD, 24));
            java.awt.Color pieceColor = piece.getColor() == Color.WHITE ? java.awt.Color.BLACK : java.awt.Color.WHITE;
            g2d.setColor(pieceColor);
            String symbol = String.valueOf(piece.getSymbol());
            FontMetrics fm = g2d.getFontMetrics();
            int textX = x + (squareSize - fm.stringWidth(symbol)) / 2;
            int textY = y + (squareSize + fm.getAscent()) / 2;
            g2d.drawString(symbol, textX, textY);
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
  }
}
