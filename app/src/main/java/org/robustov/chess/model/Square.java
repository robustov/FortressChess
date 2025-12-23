package org.robustov.chess.model;

import java.util.Optional;

public class Square {
  private final Position position;
  private Piece piece;
  private final boolean isLegal;

  public Square(Position position, boolean isLegal) {
    this.position = position;
    this.isLegal = isLegal;
  }

  public Position getPosition() {
    return position;
  }

  public boolean isLegal() {
    return isLegal;
  }

  public boolean hasPiece() {
    return piece != null;
  }

  public Optional<Piece> getPiece() {
    return Optional.ofNullable(piece);
  }

  public void setPiece(Piece piece) {
    if (this.piece != null) {
      throw new IllegalStateException("Square " + position + " is already occupied");
    }
    this.piece = piece;
  }

  public Optional<Piece> removePiece() {
    Piece removed = piece;
    piece = null;
    return Optional.ofNullable(removed);
  }

  @Override
  public String toString() {
    return position.toNotation() + (isLegal ? " (LEGAL)" : " (ILLEGAL)") +
        (hasPiece() ? " : " + piece : "");
  }
}
