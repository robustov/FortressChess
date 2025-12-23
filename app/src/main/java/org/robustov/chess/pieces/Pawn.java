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
    int direction = getColor() == Color.WHITE ? 1 : -1;
    int startRank = getColor() == Color.WHITE ? 2 : 15;

    // Forward move
    char file = position.getFile();
    int rank = position.getRank();

    Position forward = new Position(file, rank + direction);
    if (isValidPosition(forward, board) && !board.hasPiece(forward)) {
      validMoves.add(forward);

      if (!hasMoved() && rank == startRank) {
        Position doubleForward = new Position(file, rank + 2 * direction);
        if (isValidPosition(doubleForward, board) && !board.hasPiece(doubleForward)) {
          validMoves.add(doubleForward);
        }
      }
    }

    // Capture moves
    char[] captureFiles = { (char) (file - 1), (char) (file + 1) };
    for (char captureFile : captureFiles) {
      Position capturePos = new Position(captureFile, rank + direction);
      if (isValidPosition(capturePos, board)) {
        if (board.hasPiece(capturePos) && board.getPiece(capturePos).get().getColor() != getColor()) {
          validMoves.add(capturePos);
        }
      }
    }

    return validMoves;
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
