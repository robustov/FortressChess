package org.robustov.chess.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.robustov.chess.pieces.King;
import org.robustov.chess.pieces.Rook;
import org.robustov.chess.pieces.King;
import org.robustov.chess.pieces.King;
import org.robustov.chess.pieces.King;

public class Board {
  private final Map<Position, Square> squares;
  private final Map<Color, King> kings;

  public Board() {
    squares = new HashMap<>();
    kings = new HashMap<>();
    initializeEmptyBoard();
  }

  private void initializeEmptyBoard() {
    for (char file = 'a'; file <= 'h'; file++) {
      for (int rank = 1; rank <= 8; rank++) {
        Position position = new Position(file, rank);
        squares.put(position, new Square(position));
      }
    }
  }

  public void placePiece(Piece piece, Position position) {
    Square square = getSquare(position);
    if (square.hasPiece()) {
      throw new IllegalArgumentException("Position " + position + " is already occupied");
    }

    square.setPiece(piece);

    if (piece instanceof King king) {
      kings.put(piece.getColor(), king);
    }
  }

  public Optional<Piece> removePiece(Position position) {
    return getSquare(position).removePiece();
  }

  public void movePiece(Position source, Position target) {
    Square sourceSquare = getSquare(source);
    if (!sourceSquare.hasPiece()) {
      throw new IllegalArgumentException("No piece at source position: " + source);
    }

    Piece piece = sourceSquare.getPiece().orElseThrow();
    Square targetSquare = getSquare(target);

    if (targetSquare.hasPiece()) {
      targetSquare.removePiece();
    }

    sourceSquare.removePiece();
    targetSquare.setPiece(piece);
    piece.markAsMoved();
  }

  public Square getSquare(Position position) {
    Square square = squares.get(position);
    if (square == null) {
      throw new IllegalArgumentException("Invalid position: " + position);
    }
    return square;
  }

  public boolean hasPiece(Position position) {
    return getSquare(position).hasPiece();
  }

  public Optional<Piece> getPiece(Position position) {
    return getSquare(position).getPiece();
  }

  public boolean isKingInCheck(Color color) {
    King king = kings.get(color);
    if (king == null) {
      return false;
    }

    Position kingPosition = findKingPosition(color);
    if (kingPosition == null) {
      return false;
    }

    Color opponentColor = color.getOpponent();
    for (Position position : squares.keySet()) {
      Optional<Piece> pieceOptional = getPiece(position);
      if (pieceOptional.isPresent() && pieceOptional.get().getColor() == opponentColor) {
        Piece piece = pieceOptional.get();
        if (piece.isValidMove(position, kingPosition, this)) {
          return true;
        }
      }
    }
    return false;
  }

  private Position findKingPosition(Color color) {
    for (Map.Entry<Position, Square> entry : squares.entrySet()) {
      Optional<Piece> piece = entry.getValue().getPiece();
      if (piece.isPresent() && piece.get() instanceof King && piece.get().getColor() == color) {
        return entry.getKey();
      }
    }
    return null;
  }

  public void setupStandardPosition() {
    squares.values().forEach(square -> square.removePiece());
    kings.clear();

    for (char file = 'a'; file <= 'h'; file++) {
      placePiece(new Pawn(Color.WHITE), new Position(file, 2));
      placePiece(new Pawn(Color.BLACK), new Position(file, 7));
    }

    setupBackRank(Color.WHITE, 1);
    setupBackRank(Color.BLACK, 8);
  }

  private void setupBackRank(Color color, int rank) {
    placePiece(new Rook(color), new Position('a', rank));
    placePiece(new Knight(color), new Position('b', rank));
    placePiece(new Bishop(color), new Position('c', rank));
    placePiece(new Queen(color), new Position('d', rank));
    placePiece(new King(color), new Position('e', rank));
    placePiece(new Bishop(color), new Position('f', rank));
    placePiece(new Knight(color), new Position('g', rank));
    placePiece(new Rook(color), new Position('h', rank));
  }
}
