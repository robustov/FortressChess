package org.robustov.chess.model;

public enum Color {
    YELLOW,
    BLUE, 
    RED,
    GREEN;

    public Color getNextPlayer() {
        return switch (this) {
            case YELLOW -> BLUE;
            case BLUE -> RED;
            case RED -> GREEN;
            case GREEN -> YELLOW;
        };
    }
}
