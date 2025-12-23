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

  public char getSymbol(Color color) {
    return switch (this) {
      case PAWN -> color == Color.WHITE ? '♙' : '♟';
      case KNIGHT -> color == Color.WHITE ? '♘' : '♞';
      case BISHOP -> color == Color.WHITE ? '♗' : '♝';
      case ROOK -> color == Color.WHITE ? '♖' : '♜';
      case QUEEN -> color == Color.WHITE ? '♕' : '♛';
      case KING -> color == Color.WHITE ? '♔' : '♚';
      default -> '?';
    };
  }
}
