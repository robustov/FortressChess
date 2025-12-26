package org.robustov.chess.pieces;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.PieceType;
import org.robustov.chess.model.Position;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {
  public Pawn(Color color) {
    super(color, PieceType.PAWN);
  }

  @Override
  public Set<Position> getValidMoves(Position position, Board board) {
    Set<Position> validMoves = new HashSet<>();
    int[] direction = getMovementDirection();

    char file = position.getFile();
    int rank = position.getRank();

    Position forward = new Position((char) (file + direction[0]), rank + direction[1]);
    if (isValidPosition(forward, board) && !board.hasPiece(forward)) {
      validMoves.add(forward);

      if (!hasMoved()) {
        Position doubleForward = new Position((char) (file + 2 * direction[0]), rank + 2 * direction[1]);
        if (isValidPosition(doubleForward, board) && !board.hasPiece(doubleForward)) {
          validMoves.add(doubleForward);
        }
      }
    }

    if (movesVertically()) {
      Position captureRight = new Position((char) (file + 1), rank + direction[1]);
      Position captureLeft = new Position((char) (file - 1), rank + direction[1]);

      if (isValidPosition(captureRight, board) && hasEnemyPiece(captureRight, board)) {
        validMoves.add(captureRight);
      }

      if (isValidPosition(captureLeft, board) && hasEnemyPiece(captureLeft, board)) {
        validMoves.add(captureLeft);
      }
    } else {
      Position captureUp = new Position(file, rank + 1);
      Position captureDown = new Position(file, rank - 1);

      if (isValidPosition(captureUp, board) && hasEnemyPiece(captureUp, board)) {
        validMoves.add(captureUp);
      }

      if (isValidPosition(captureDown, board) && hasEnemyPiece(captureDown, board)) {
        validMoves.add(captureDown);
      }
    }

    return validMoves;
  }

  private boolean movesVertically() {
    return getColor() == Color.RED || getColor() == Color.GREEN;
  }

  private int[] getMovementDirection() {
    return switch (getColor()) {
      case YELLOW -> new int[] { 1, 0 };
      case BLUE -> new int[] { -1, 0 };
      case RED -> new int[] { 0, -1 };
      case GREEN -> new int[] { 0, 1 };
    };
  }

  private boolean hasEnemyPiece(Position position, Board board) {
    if (!board.hasPiece(position)) {
      return false;
    }
    Piece piece = board.getPiece(position).get();
    return piece.getColor() != getColor();
  }

  private boolean isValidPosition(Position position, Board board) {
    return isValidFile(position.getFile()) && isValidRank(position.getRank()) &&
        board.isLegalPosition(position);
  }

  private boolean isValidFile(char file) {
    return file >= 'a' && file <= 'p';
  }

  private boolean isValidRank(int rank) {
    return rank >= 1 && rank <= 16;
  }
}
