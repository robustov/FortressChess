package org.robustov.chess.model;

public enum PieceType {
  PAWN,
  KNIGHT,
  BISHOP,
  ROOK,
  QUEEN,
  KING;

  public char getSymbol(Color color) {
    return switch (this) {
      case PAWN -> color == Color.WHITE ? '♙' : '♟';
      case KNIGHT -> color == Color.WHITE ? '♘' : '♞';
      case BISHOP -> color == Color.WHITE ? '♗' : '♝';
      case ROOK -> color == Color.WHITE ? '♖' : '♜';
      case QUEEN -> color == Color.WHITE ? '♕' : '♛';
      case KING -> color == Color.WHITE ? '♔' : '♚';
    };
  }
}
