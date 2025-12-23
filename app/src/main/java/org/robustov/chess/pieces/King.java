package org.robustov.chess.pieces;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.PieceType;
import org.robustov.chess.model.Position;

import java.util.HashSet;
import java.util.Set;

public class King extends Piece {
  public King(Color color) {
    super(color, PieceType.KING);
  }

  @Override
  public Set<Position> getValidMoves(Position position, Board board) {
    Set<Position> validMoves = new HashSet<>();
    char file = position.getFile();
    int rank = position.getRank();

    int[] fileOffsets = { -1, -1, -1, 0, 0, 1, 1, 1 };
    int[] rankOffsets = { -1, 0, 1, -1, 1, -1, 0, 1 };

    for (int i = 0; i < 8; i++) {
      char targetFile = (char) (file + fileOffsets[i]);
      int targetRank = rank + rankOffsets[i];

      Position target = new Position(targetFile, targetRank);
      if (isValidPosition(target)) {
        if (!board.hasPiece(target) ||
            board.getPiece(target).get().getColor() != getColor()) {
          validMoves.add(target);
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
