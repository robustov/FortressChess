package org.robustov.chess.model;

public enum Color {
  YELLOW,
  BLUE,
  RED,
  GREEN;

  public Color getNextPlayer() {
    return switch (this) {
      case YELLOW -> RED;
      case BLUE -> GREEN;
      case RED -> BLUE;
      case GREEN -> YELLOW;
    };
  }
}
