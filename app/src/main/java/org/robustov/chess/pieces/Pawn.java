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
    int startRank = getColor() == Color.WHITE ? 2 : 7;

    Position forward = new Position(position.getFile(), position.getRank() + direction);
    if (isValidPosition(forward) && !board.hasPiece(forward)) {
      validMoves.add(forward);

      if (!hasMoved() && position.getRank() == startRank) {
        Position doubleForward = new Position(position.getFile(), position.getRank() + 2 * direction);
        if (!board.hasPiece(doubleForward)) {
          validMoves.add(doubleForward);
        }
      }
    }

    char[] captureFiles = { (char) (position.getFile() - 1), (char) (position.getFile() + 1) };
    for (char file : captureFiles) {
      Position capturePos = new Position(file, position.getRank() + direction);
      if (isValidPosition(capturePos)) {
        if (board.hasPiece(capturePos) && board.getPiece(capturePos).get().getColor() != getColor()) {
          validMoves.add(capturePos);
        }
      }
    }

    return validMoves;
  }

  private boolean isValidPosition(Position position) {
    return position.getFile() >= 'a' && position.getFile() <= 'h' &&
        position.getRank() >= 1 && position.getRank() <= 8;
  }
}
