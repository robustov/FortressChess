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

      if (!board.isLegalPosition(target)) {
        continue;
      }

      if (!board.hasPiece(target) ||
          board.getPiece(target).get().getColor() != getColor()) {
        validMoves.add(target);
      }
    }

    addCastlingMoves(position, board, validMoves);

    return validMoves;
  }

  private void addCastlingMoves(Position position, Board board, Set<Position> validMoves) {
    if (hasMoved())
      return;

    if (canCastleKingside(position, board)) {
      char kingFile = position.getFile();
      int rank = position.getRank();
      validMoves.add(new Position((char) (kingFile + 2), rank));
    }

    if (canCastleQueenside(position, board)) {
      char kingFile = position.getFile();
      int rank = position.getRank();
      validMoves.add(new Position((char) (kingFile - 2), rank));
    }
  }

  private boolean canCastleKingside(Position position, Board board) {
    int rank = position.getRank();
    char kingFile = position.getFile();
    char rookFile = (char) ('p'); // Rightmost file

    // Check if squares between king and rook are legal and empty
    Position firstSquare = new Position((char) (kingFile + 1), rank);
    Position secondSquare = new Position((char) (kingFile + 2), rank);

    if (!board.isLegalPosition(firstSquare) || !board.isLegalPosition(secondSquare)) {
      return false;
    }

    if (board.hasPiece(firstSquare) || board.hasPiece(secondSquare)) {
      return false;
    }

    // Check if rook exists and hasn't moved
    Position rookPosition = new Position(rookFile, rank);
    if (!board.isLegalPosition(rookPosition) || !board.hasPiece(rookPosition) ||
        !(board.getPiece(rookPosition).get() instanceof Rook rook) ||
        rook.hasMoved()) {
      return false;
    }

    return true;
  }

  private boolean canCastleQueenside(Position position, Board board) {
    int rank = position.getRank();
    char kingFile = position.getFile();
    char rookFile = 'a'; // Leftmost file

    // Check if squares between king and rook are legal and empty
    Position firstSquare = new Position((char) (kingFile - 1), rank);
    Position secondSquare = new Position((char) (kingFile - 2), rank);
    Position thirdSquare = new Position((char) (kingFile - 3), rank);

    if (!board.isLegalPosition(firstSquare) || !board.isLegalPosition(secondSquare) ||
        !board.isLegalPosition(thirdSquare)) {
      return false;
    }

    if (board.hasPiece(firstSquare) || board.hasPiece(secondSquare) || board.hasPiece(thirdSquare)) {
      return false;
    }

    // Check if rook exists and hasn't moved
    Position rookPosition = new Position(rookFile, rank);
    if (!board.isLegalPosition(rookPosition) || !board.hasPiece(rookPosition) ||
        !(board.getPiece(rookPosition).get() instanceof Rook rook) ||
        rook.hasMoved()) {
      return false;
    }

    return true;
  }
}
