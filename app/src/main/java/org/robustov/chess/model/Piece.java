package org.robustov.chess.model;

import java.util.Set;

public abstract class Piece {
  private final Color color;
  private final PieceType type;
  private boolean hasMoved;

  protected Piece(Color color, PieceType type) {
    this.color = color;
    this.type = type;
  }

  public Color getColor() {
    return color;
  }

  public PieceType getType() {
    return type;
  }

  public boolean hasMoved() {
    return hasMoved;
  }

  public void markAsMoved() {
    hasMoved = true;
  }

  public abstract Set<Position> getValidMoves(Position position, Board board);

  public boolean isValidMove(Position position, Position target, Board board) {
    return getValidMoves(position, board).contains(target);
  }

  public char getSymbol() {
    return type.getSymbol(color);
  }

  @Override
  public String toString() {
    return color + " " + type;
  }
}
