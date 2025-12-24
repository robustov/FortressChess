package org.robustov.chess.ui;

import org.robustov.chess.model.Board;

import javax.swing.*;

public class ChessFrame extends JFrame {
  public ChessFrame(Board board) {
    setTitle("Fortress Chess (Русские четвертные шахматы с крепостями)");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    BoardPanel boardPanel = new BoardPanel(board);
    add(boardPanel);

    pack();
    setLocationRelativeTo(null);
  }
}
