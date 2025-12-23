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

    addCastlingMoves(position, board, validMoves);

    return validMoves;
  }

  private boolean isValidPosition(Position position) {
    return position.getFile() >= 'a' && position.getFile() <= 'h' &&
        position.getRank() >= 1 && position.getRank() <= 8;
  }

  private void addCastlingMoves(Position position, Board board, Set<Position> validMoves) {
    if (hasMoved())
      return;

    if (canCastleKingside(position, board)) {
      validMoves.add(new Position('g', position.getRank()));
    }

    if (canCastleQueenside(position, board)) {
      validMoves.add(new Position('c', position.getRank()));
    }
  }

  private boolean canCastleKingside(Position position, Board board) {
    int rank = position.getRank();

    Position fSquare = new Position('f', rank);
    Position gSquare = new Position('g', rank);

    if (board.hasPiece(fSquare) || board.hasPiece(gSquare)) {
      return false;
    }

    Position rookPosition = new Position('h', rank);
    if (!board.hasPiece(rookPosition) ||
        !(board.getPiece(rookPosition).get() instanceof Rook rook) ||
        rook.hasMoved()) {
      return false;
    }

    return true;
  }

  private boolean canCastleQueenside(Position position, Board board) {
    int rank = position.getRank();

    Position bSquare = new Position('b', rank);
    Position cSquare = new Position('c', rank);
    Position dSquare = new Position('d', rank);

    if (board.hasPiece(bSquare) || board.hasPiece(cSquare) || board.hasPiece(dSquare)) {
      return false;
    }

    Position rookPosition = new Position('a', rank);
    if (!board.hasPiece(rookPosition) ||
        !(board.getPiece(rookPosition).get() instanceof Rook rook) ||
        rook.hasMoved()) {
      return false;
    }

    return true;
  }
}
