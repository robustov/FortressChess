package org.robustov.chess.model;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.robustov.chess.pieces.Bishop;
import org.robustov.chess.pieces.King;
import org.robustov.chess.pieces.Queen;
import org.robustov.chess.pieces.Rook;
import org.robustov.chess.pieces.Pawn;
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
    Optional<Piece> removed = square.removePiece();
    if (removed.isPresent() && removed.get() instanceof King) {
      kings.remove(removed.get().getColor());
    }
    return removed;
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

    Set<Position> validMoves = piece.getValidMoves(source, this);
    if (!validMoves.contains(target)) {
      throw new IllegalArgumentException("Piece cannot move to target: " + target);
    }

    if (wouldLeaveKingInCheck(piece.getColor(), source, target)) {
      throw new IllegalArgumentException("Illegal move: would leave king in check");
    }

    Optional<Piece> captured = Optional.empty();
    if (targetSquare.hasPiece()) {
      captured = removePiece(target);
    }

    sourceSquare.removePiece();
    targetSquare.setPiece(piece);
    piece.markAsMoved();

    try {
      checkAndEliminateMatedPlayers();
    } catch (RuntimeException ex) {
      System.err.println("Error during check/mate scanning: " + ex.getMessage());
      ex.printStackTrace();
    } finally {
      advanceTurn();
    }
  }

  private void advanceTurn() {
    for (int i = 0; i < Color.values().length; i++) {
      currentPlayer = currentPlayer.getNextPlayer();
      if (isPlayerActive(currentPlayer)) {
        return;
      }
    }
  }

  private boolean isPlayerActive(Color color) {
    if (kings.containsKey(color)) {
      return true;
    }
    for (Square s : squares.values()) {
      if (!s.isLegal())
        continue;
      Optional<Piece> p = s.getPiece();
      if (p.isPresent() && p.get().getColor() == color) {
        return true;
      }
    }
    return false;
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
        try {
          if (piece.isValidMove(position, kingPosition, this)) {
            return true;
          }
        } catch (RuntimeException ex) {
          System.err.println("Error while testing attack from " + position + " : " + ex.getMessage());
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

    setupPlayerCorner(Color.YELLOW, 'a', 1, 1);

    setupPlayerCorner(Color.BLUE, 'm', 1, 1);

    setupPlayerCorner(Color.RED, 'a', 13, -1);

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

  public boolean isCheckmate(Color color) {
    if (!kings.containsKey(color)) {
      return false;
    }

    if (!isKingInCheck(color)) {
      return false;
    }

    for (Map.Entry<Position, Square> entry : squares.entrySet()) {
      Position from = entry.getKey();
      Square square = entry.getValue();
      if (!square.isLegal())
        continue;
      Optional<Piece> pOpt = square.getPiece();
      if (pOpt.isEmpty())
        continue;
      Piece piece = pOpt.get();
      if (piece.getColor() != color)
        continue;

      Set<Position> moves = piece.getValidMoves(from, this);
      for (Position to : moves) {
        if (!wouldLeaveKingInCheck(color, from, to)) {
          return false;
        }
      }
    }

    return true;
  }

  private boolean wouldLeaveKingInCheck(Color color, Position from, Position to) {
    Square src = getSquare(from);
    Square dst = getSquare(to);

    Piece moving = src.getPiece().orElseThrow();
    Optional<Piece> captured = dst.getPiece();

    src.removePiece();
    if (dst.hasPiece()) {
      dst.removePiece();
    }
    dst.setPiece(moving);

    boolean kingStillInCheck;
    try {
      kingStillInCheck = isKingInCheck(color);
    } finally {
      dst.removePiece();
      src.setPiece(moving);
      if (captured.isPresent()) {
        dst.setPiece(captured.get());
      }
    }

    return kingStillInCheck;
  }

  private void checkAndEliminateMatedPlayers() {
    Set<Color> toEliminate = new HashSet<>();

    for (Color color : Color.values()) {
      if (kings.containsKey(color) && isCheckmate(color)) {
        toEliminate.add(color);
      }
    }

    if (!toEliminate.isEmpty()) {
      for (Color color : toEliminate) {
        eliminatePlayerPieces(color);
        System.out.println("Player " + color + " has been checkmated and eliminated from the board.");
      }
    }
  }

  private void eliminatePlayerPieces(Color color) {
    for (Map.Entry<Position, Square> entry : squares.entrySet()) {
      Square square = entry.getValue();
      if (!square.isLegal())
        continue;
      Optional<Piece> pOpt = square.getPiece();
      if (pOpt.isPresent() && pOpt.get().getColor() == color) {
        square.removePiece();
      }
    }
    kings.remove(color);
  }

  public String savePosition() {
    BoardState state = new BoardState();
    state.currentPlayer = currentPlayer;
    Map<String, SquareState> squaresMap = new HashMap<>();
    for (Map.Entry<Position, Square> entry : squares.entrySet()) {
      Position pos = entry.getKey();
      Square square = entry.getValue();
      SquareState squareState = new SquareState();
      if (square.hasPiece()) {
        Piece piece = square.getPiece().get();
        PieceState pieceState = new PieceState();
        pieceState.color = piece.getColor();
        pieceState.type = piece.getType();
        pieceState.moved = piece.hasMoved();
        squareState.piece = pieceState;
      }
      squaresMap.put(pos.toString(), squareState);
    }
    state.squares = squaresMap;
    Gson gson = new GsonBuilder().setPrettyPrinting().create();
    return gson.toJson(state);
  }

  public void loadPosition(String json) {
    Gson gson = new Gson();
    BoardState state = gson.fromJson(json, BoardState.class);
    squares.values().forEach(square -> {
      if (square.isLegal()) {
        square.removePiece();
      }
    });
    kings.clear();
    currentPlayer = state.currentPlayer;
    for (Map.Entry<String, SquareState> entry : state.squares.entrySet()) {
      Position pos = new Position(entry.getKey());
      Square square = squares.get(pos);
      if (square == null) {
        throw new IllegalArgumentException("Invalid position in save: " + entry.getKey());
      }
      SquareState squareState = entry.getValue();
      if (squareState.piece != null) {
        PieceState pieceState = squareState.piece;
        Piece piece = createPiece(pieceState.type, pieceState.color);
        piece.setHasMoved(pieceState.moved);
        square.setPiece(piece);
        if (piece instanceof King) {
          kings.put(piece.getColor(), (King) piece);
        }
      }
    }
  }

  private Piece createPiece(PieceType type, Color color) {
    return switch (type) {
      case PAWN -> new Pawn(color);
      case ROOK -> new Rook(color);
      case KNIGHT -> new Knight(color);
      case BISHOP -> new Bishop(color);
      case QUEEN -> new Queen(color);
      case KING -> new King(color);
    };
  }

  public static class BoardState {
    @SerializedName("current_player")
    Color currentPlayer;
    @SerializedName("squares")
    Map<String, SquareState> squares;
  }

  public static class SquareState {
    @SerializedName("piece")
    PieceState piece;
  }

  public static class PieceState {
    @SerializedName("color")
    Color color;
    @SerializedName("type")
    PieceType type;
    @SerializedName("moved")
    boolean moved;
  }
}
