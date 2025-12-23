package org.robustov.chess.model;

public final class Position {
  private final char file;
  private final int rank;

  public Position(char file, int rank) {
    validateFile(file);
    validateRank(rank);
    this.file = Character.toLowerCase(file);
    this.rank = rank;
  }

  public Position(String notation) {
    if (notation == null || notation.length() != 2) {
      throw new IllegalArgumentException("Invalid position notation: " + notation);
    }
    this.file = Character.toLowerCase(notation.charAt(0));
    this.rank = Character.getNumericValue(notation.charAt(1));
    validateFile(this.file);
    validateRank(this.rank);
  }

  private void validateFile(char file) {
    if (file < 'a' || file > 'h') {
      throw new IllegalArgumentException("Invalid file: " + file);
    }
  }

  private void validateRank(int rank) {
    if (rank < 1 || rank > 8) {
      throw new IllegalArgumentException("Invalid rank: " + rank);
    }
  }

  public char getFile() {
    return file;
  }

  public int getRank() {
    return rank;
  }

  public String toNotation() {
    return "" + file + rank;
  }

  public int distanceTo(Position other) {
    return Math.abs(file - other.file) + Math.abs(rank - other.rank);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Position position = (Position) o;
    return file == position.file && rank == position.rank;
  }

  @Override
  public int hashCode() {
    return 31 * file + rank;
  }

  @Override
  public String toString() {
    return toNotation();
  }
}
