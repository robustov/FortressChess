package org.robustov.chess.pieces;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.PieceType;
import org.robustov.chess.model.Position;

import java.util.HashSet;
import java.util.Set;

public class Rook extends Piece {
  public Rook(Color color) {
    super(color, PieceType.ROOK);
  }

  @Override
  public Set<Position> getValidMoves(Position position, Board board) {
    Set<Position> validMoves = new HashSet<>();
    addDirectionMoves(position, board, validMoves, 1, 0);
    addDirectionMoves(position, board, validMoves, -1, 0);
    addDirectionMoves(position, board, validMoves, 0, 1);
    addDirectionMoves(position, board, validMoves, 0, -1);
    return validMoves;
  }

  private void addDirectionMoves(Position position, Board board, Set<Position> moves, int fileDelta, int rankDelta) {
    char currentFile = position.getFile();
    int currentRank = position.getRank();

    while (true) {
      currentFile = (char) (currentFile + fileDelta);
      currentRank = currentRank + rankDelta;

      Position target = new Position(currentFile, currentRank);

      if (!board.isLegalPosition(target)) {
        break;
      }

      if (board.hasPiece(target)) {
        Piece piece = board.getPiece(target).get();
        if (piece.getColor() != getColor()) {
          moves.add(target);
        }
        break;
      }

      moves.add(target);
    }
  }
}
