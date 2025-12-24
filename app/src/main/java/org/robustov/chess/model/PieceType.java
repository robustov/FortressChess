package org.robustov.chess.model;

public enum PieceType {
  PAWN(1),
  KNIGHT(3),
  BISHOP(3),
  ROOK(5),
  QUEEN(9),
  KING(Integer.MAX_VALUE);

  private final int value;

  PieceType(int value) {
    this.value = value;
  }

  public int getValue() {
    return value;
  }

  // This method is no longer needed for rendering but kept for other purposes
  public char getSymbol(Color color) {
    return switch (this) {
      case PAWN -> 'P';
      case KNIGHT -> 'N';
      case BISHOP -> 'B';
      case ROOK -> 'R';
      case QUEEN -> 'Q';
      case KING -> 'K';
      default -> '?';
    };
  }
}
