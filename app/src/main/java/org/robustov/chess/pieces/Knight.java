package org.robustov.chess.pieces;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.PieceType;
import org.robustov.chess.model.Position;

import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {
  public Knight(Color color) {
    super(color, PieceType.KNIGHT);
  }

  @Override
  public Set<Position> getValidMoves(Position position, Board board) {
    Set<Position> validMoves = new HashSet<>();
    char file = position.getFile();
    int rank = position.getRank();

    int[][] moves = {
        { 2, 1 }, { 2, -1 }, { -2, 1 }, { -2, -1 },
        { 1, 2 }, { 1, -2 }, { -1, 2 }, { -1, -2 }
    };

    for (int[] move : moves) {
      char targetFile = (char) (file + move[0]);
      int targetRank = rank + move[1];

      Position target = new Position(targetFile, targetRank);

      if (!board.isLegalPosition(target)) {
        continue;
      }

      if (!board.hasPiece(target) || board.getPiece(target).get().getColor() != getColor()) {
        validMoves.add(target);
      }
    }

    return validMoves;
  }
}
