package org.robustov;

import org.robustov.chess.model.Board;
import org.robustov.chess.ui.ChessFrame;

import javax.swing.*;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Board board = new Board();
      board.setupFortressPosition();

      ChessFrame frame = new ChessFrame(board);
      frame.setVisible(true);
    });
  }
}
