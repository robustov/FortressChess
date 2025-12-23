package org.robustov.chess.pieces;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.Piece;
import org.robustov.chess.model.PieceType;
import org.robustov.chess.model.Position;

import java.util.HashSet;
import java.util.Set;

public class Bishop extends Piece {
  public Bishop(Color color) {
    super(color, PieceType.BISHOP);
  }

  @Override
  public Set<Position> getValidMoves(Position position, Board board) {
    Set<Position> validMoves = new HashSet<>();
    addDiagonalMoves(position, board, validMoves);
    return validMoves;
  }

  private void addDiagonalMoves(Position position, Board board, Set<Position> moves) {
    addDirectionMoves(position, board, moves, 1, 1);
    addDirectionMoves(position, board, moves, 1, -1);
    addDirectionMoves(position, board, moves, -1, 1);
    addDirectionMoves(position, board, moves, -1, -1);
  }

  private void addDirectionMoves(Position position, Board board, Set<Position> moves, int fileDelta, int rankDelta) {
    char currentFile = position.getFile();
    int currentRank = position.getRank();

    while (true) {
      currentFile = (char) (currentFile + fileDelta);
      currentRank = currentRank + rankDelta;

      Position target = new Position(currentFile, currentRank);
      if (!isValidPosition(target)) {
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

  private boolean isValidPosition(Position position) {
    return position.getFile() >= 'a' && position.getFile() <= 'h' &&
        position.getRank() >= 1 && position.getRank() <= 8;
  }
}
