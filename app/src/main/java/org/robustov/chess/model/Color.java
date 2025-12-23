package org.robustov.chess.model;

public enum Color {
  WHITE,
  BLACK;

  public Color getOpponent() {
    return this == WHITE ? BLACK : WHITE;
  }
}
