package org.robustov.chess.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.robustov.chess.pieces.King;
import org.robustov.chess.pieces.Queen;
import org.robustov.chess.pieces.Rook;
import org.robustov.chess.pieces.Pawn;
import org.robustov.chess.pieces.Bishop;
import org.robustov.chess.pieces.Knight;

public class Board {
  private final Map<Position, Square> squares;
  private final Map<Color, King> kings;
  private Color currentPlayer;

  public Board() {
    squares = new HashMap<>();
    kings = new HashMap<>();
    currentPlayer = Color.YELLOW;
    initializeFortressBoard();
  }

  private void initializeFortressBoard() {
    for (char file = 'a'; file <= 'p'; file++) {
      for (int rank = 1; rank <= 16; rank++) {
        Position position = new Position(file, rank);
        boolean isLegal = isLegalSquare(rank, file);
        squares.put(position, new Square(position, isLegal));
      }
    }
  }

  private boolean isLegalSquare(int rank, char file) {
    int fileIndex = file - 'a' + 1;

    if (rank <= 2 || rank >= 15) {
      return fileIndex <= 4 || fileIndex >= 13;
    } else if (rank <= 4 || rank >= 13) {
      return true;
    } else {
      return fileIndex > 2 && fileIndex < 15;
    }
  }

  public void placePiece(Piece piece, Position position) {
    Square square = getSquare(position);
    if (!square.isLegal()) {
      throw new IllegalArgumentException("Cannot place piece on illegal square: " + position);
    }
    if (square.hasPiece()) {
      throw new IllegalArgumentException("Position " + position + " is already occupied");
    }

    square.setPiece(piece);

    if (piece instanceof King king) {
      kings.put(piece.getColor(), king);
    }
  }

  public Optional<Piece> removePiece(Position position) {
    Square square = getSquare(position);
    if (!square.isLegal()) {
      throw new IllegalArgumentException("Cannot remove piece from illegal square: " + position);
    }
    return square.removePiece();
  }

  public void movePiece(Position source, Position target) {
    Square sourceSquare = getSquare(source);
    Square targetSquare = getSquare(target);

    if (!sourceSquare.isLegal() || !targetSquare.isLegal()) {
      throw new IllegalArgumentException("Cannot move to/from illegal squares");
    }

    if (!sourceSquare.hasPiece()) {
      throw new IllegalArgumentException("No piece at source position: " + source);
    }

    Piece piece = sourceSquare.getPiece().orElseThrow();

    if (piece.getColor() != currentPlayer) {
      throw new IllegalStateException("Not " + currentPlayer + "'s turn. Current turn: " + currentPlayer);
    }

    if (targetSquare.hasPiece()) {
      targetSquare.removePiece();
    }

    sourceSquare.removePiece();
    targetSquare.setPiece(piece);
    piece.markAsMoved();

    advanceTurn();
  }

  private void advanceTurn() {
    currentPlayer = currentPlayer.getNextPlayer();
  }

  public Color getCurrentPlayer() {
    return currentPlayer;
  }

  public Square getSquare(Position position) {
    Square square = squares.get(position);
    if (square == null) {
      throw new IllegalArgumentException("Invalid position: " + position);
    }
    return square;
  }

  public boolean isLegalPosition(Position position) {
    return getSquare(position).isLegal();
  }

  public boolean hasPiece(Position position) {
    Square square = getSquare(position);
    return square.isLegal() && square.hasPiece();
  }

  public Optional<Piece> getPiece(Position position) {
    Square square = getSquare(position);
    return square.isLegal() ? square.getPiece() : Optional.empty();
  }

  public boolean isKingInCheck(Color color) {
    King king = kings.get(color);
    if (king == null) {
      return false;
    }

    Position kingPosition = findKingPosition(color);
    if (kingPosition == null || !isLegalPosition(kingPosition)) {
      return false;
    }

    for (Position position : squares.keySet()) {
      if (!isLegalPosition(position))
        continue;

      Optional<Piece> pieceOptional = getPiece(position);
      if (pieceOptional.isPresent() && pieceOptional.get().getColor() != color) {
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
      if (!entry.getValue().isLegal())
        continue;

      Optional<Piece> piece = entry.getValue().getPiece();
      if (piece.isPresent() && piece.get() instanceof King && piece.get().getColor() == color) {
        return entry.getKey();
      }
    }
    return null;
  }

  public void setupFortressPosition() {
    squares.values().forEach(square -> {
      if (square.isLegal()) {
        square.removePiece();
      }
    });
    kings.clear();
    currentPlayer = Color.YELLOW;

    // Player 1 (Yellow) - bottom-left corner area
    setupPlayerCorner(Color.YELLOW, 'a', 1, 1);

    // Player 2 (Blue) - bottom-right corner area
    setupPlayerCorner(Color.BLUE, 'm', 1, 1);

    // Player 3 (Red) - top-left corner area
    setupPlayerCorner(Color.RED, 'a', 13, -1);

    // Player 4 (Green) - top-right corner area
    setupPlayerCorner(Color.GREEN, 'm', 13, -1);
  }

  private void setupPlayerCorner(Color color, char startFile, int startRank, int direction) {
    for (int i = 0; i < 4; i++) {
      char file = (char) (startFile + i);
      int rank = startRank;

      try {
        Position pawnPos = new Position(file, rank + direction);
        if (isLegalPosition(pawnPos)) {
          placePiece(new Pawn(color), pawnPos);
        }

        Position piecePos = new Position(file, startRank);
        if (isLegalPosition(piecePos)) {
          switch (i) {
            case 0:
              placePiece(new Rook(color), piecePos);
              break;
            case 1:
              placePiece(new Knight(color), piecePos);
              break;
            case 2:
              placePiece(new Bishop(color), piecePos);
              break;
            case 3:
              placePiece(new King(color), piecePos);
              break;
          }
        }
      } catch (IllegalArgumentException e) {
      }
    }
  }
}
