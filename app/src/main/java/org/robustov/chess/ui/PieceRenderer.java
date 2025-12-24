package org.robustov.chess.ui;

import org.robustov.chess.model.Color;
import org.robustov.chess.model.PieceType;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.util.HashMap;
import java.util.Map;

public class PieceRenderer {
  private static final Map<PieceType, String[]> PATH_DATA = new HashMap<>();

  static {
    PATH_DATA.put(PieceType.KING, new String[] {
        "M 22.5,11.625 L 22.5,6",
        "M 22.5,25 C 22.5,25 27,17.5 25.5,14.5 C 25.5,14.5 24.5,12 22.5,12 C 20.5,12 19.5,14.5 19.5,14.5 C 18,17.5 22.5,25 22.5,25",
        "M 11.5,37 C 17,40.5 27,40.5 32.5,37 L 32.5,30 C 32.5,30 41.5,25.5 38.5,19.5 C 34.5,13 25,16 22.5,23.5 L 22.5,27 L 22.5,23.5 C 19,16 9.5,13 6.5,19.5 C 3.5,25.5 11.5,29.5 11.5,29.5 L 11.5,37 z",
        "M 20,8 L 25,8",
        "M 11.5,29.5 C 17,27 27,27 32.5,30",
        "M 11.5,37 C 17,34.5 27,34.5 32.5,37",
        "M 11.5,33.5 C 17,31.5 27,31.5 32.5,33.5"
    });

    PATH_DATA.put(PieceType.QUEEN, new String[] {
        "M 22 9 C 19.792 9 18 10.792 18 13 C 18 13.885103 18.29397 14.712226 18.78125 15.375 C 16.829274 16.496917 15.5 18.588492 15.5 21 C 15.5 23.033947 16.442042 24.839082 17.90625 26.03125 C 14.907101 27.08912 10.5 31.578049 10.5 39.5 L 33.5 39.5 C 33.5 31.578049 29.092899 27.08912 26.09375 26.03125 C 27.557958 24.839082 28.5 23.033948 28.5 21 C 28.5 18.588492 27.170726 16.496917 25.21875 15.375 C 25.70603 14.712226 26 13.885103 26 13 C 26 10.792 24.208 9 22 9 z",
        "M 9,26 C 9,28 10.5,28 11.5,30 C 12.5,31.5 12.5,31 12,33.5 C 10.5,34.5 10.5,36 10.5,36 C 9,37.5 11,38.5 11,38.5 C 17.5,39.5 27.5,39.5 34,38.5 C 34,38.5 35.5,37.5 34,36 C 34,36 34.5,34.5 33,33.5 C 32.5,31 32.5,31.5 33.5,30 C 34.5,28 36,28 36,26 C 27.5,24.5 17.5,24.5 9,26 z",
        "M 11.5,30 C 15,29 30,29 33.5,30",
        "M 12,33.5 C 18,32.5 27,32.5 33,33.5"
    });

    PATH_DATA.put(PieceType.ROOK, new String[] {
        "M 9,39 L 36,39 L 36,36 L 9,36 L 9,39 z",
        "M 12,36 L 12,32 L 33,32 L 33,36 L 12,36 z",
        "M 11,14 L 11,9 L 15,9 L 15,11 L 20,11 L 20,9 L 25,9 L 25,11 L 30,11 L 30,9 L 34,9 L 34,14",
        "M 34,14 L 31,17 L 14,17 L 11,14",
        "M 31,17 L 31,29.500018 L 14,29.500018 L 14,17",
        "M 31,29.5 L 32.5,32 L 12.5,32 L 14,29.5",
        "M 11,14 L 34,14"
    });

    PATH_DATA.put(PieceType.BISHOP, new String[] {
        "M 9,36 C 12.385255,35.027671 19.114744,36.430821 22.5,34 C 25.885256,36.430821 32.614745,35.027671 36,36 C 36,36 37.645898,36.541507 39,38 C 38.322949,38.972328 37.354102,38.986164 36,38.5 C 32.614745,37.527672 25.885256,38.958493 22.5,37.5 C 19.114744,38.958493 12.385255,37.527672 9,38.5 C 7.6458978,38.986164 6.6770511,38.972328 6,38 C 7.3541023,36.055343 9,36 9,36 z",
        "M 15,32 C 17.5,34.5 27.5,34.5 30,32 C 30.5,30.5 30,30 30,30 C 30,27.5 27.5,26 27.5,26 C 33,24.5 33.5,14.5 22.5,10.5 C 11.5,14.5 12,24.5 17.5,26 C 17.5,26 15,27.5 15,30 C 15,30 14.5,30.5 15,32 z",
        "M 17.5,26 L 27.5,26",
        "M 15,30 L 30,30",
        "M 22.5,15.5 L 22.5,20.5",
        "M 20,18 L 25,18"
    });

    PATH_DATA.put(PieceType.KNIGHT, new String[] {
        "M 22,10 C 32.5,11 38.5,18 38,39 L 15,39 C 15,30 25,32.5 23,18",
        "M 24,18 C 24.384461,20.911278 18.447064,25.368624 16,27 C 13,29 13.180802,31.342892 11,31 C 9.95828,30.055984 12.413429,27.962451 11,28 C 10,28 11.187332,29.231727 10,30 C 9,30 5.9968392,30.999999 6,26 C 6,24 12,14 12,14 C 12,14 13.885866,12.097871 14,10.5 C 13.273953,9.505631 13.5,8.5 13.5,7.5 C 14.5,6.5 16.5,10 16.5,10 L 18.5,10 C 18.5,10 19.281781,8.0080745 21,7 C 22,7 22,10 22,10",
        "M 37,39 C 38,19 31.5,11.5 25,10.5"
    });

    PATH_DATA.put(PieceType.PAWN, new String[] {
        "M 22 9 C 19.792 9 18 10.792 18 13 C 18 13.885103 18.29397 14.712226 18.78125 15.375 C 16.829274 16.496917 15.5 18.588492 15.5 21 C 15.5 23.033947 16.442042 24.839082 17.90625 26.03125 C 14.907101 27.08912 10.5 31.578049 10.5 39.5 L 33.5 39.5 C 33.5 31.578049 29.092899 27.08912 26.09375 26.03125 C 27.557958 24.839082 28.5 23.033948 28.5 21 C 28.5 18.588492 27.170726 16.496917 25.21875 15.375 C 25.70603 14.712226 26 13.885103 26 13 C 26 10.792 24.208 9 22 9 z"
    });
  }

  public static void renderPiece(Graphics2D g2d, PieceType pieceType, Color teamColor, int x, int y, int size) {
    GeneralPath[] paths = createPiecePaths(pieceType, size);
    java.awt.Color fillColor = getPlayerColor(teamColor);

    g2d.translate(x, y);
    g2d.scale(size / 45.0, size / 45.0);

    g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);

    g2d.setColor(fillColor);
    for (GeneralPath path : paths) {
      g2d.fill(path);
    }

    g2d.setColor(java.awt.Color.BLACK);
    g2d.setStroke(new BasicStroke(1.5f));
    for (GeneralPath path : paths) {
      g2d.draw(path);
    }

    g2d.scale(45.0 / size, 45.0 / size);
    g2d.translate(-x, -y);
  }

  private static GeneralPath[] createPiecePaths(PieceType pieceType, int size) {
    String[] pathData = PATH_DATA.get(pieceType);
    GeneralPath[] paths = new GeneralPath[pathData.length];

    for (int i = 0; i < pathData.length; i++) {
      paths[i] = parseSvgPath(pathData[i]);
    }

    return paths;
  }

  private static GeneralPath parseSvgPath(String d) {
    GeneralPath path = new GeneralPath();
    String[] commands = d.split("(?=[A-Za-z])");

    float currentX = 0, currentY = 0;

    for (String command : commands) {
      if (command.isEmpty())
        continue;

      char cmd = command.charAt(0);
      String[] coords = command.substring(1).trim().split("[ ,]+");

      switch (cmd) {
        case 'M':
        case 'm':
          if (coords.length >= 2) {
            float x = Float.parseFloat(coords[0]);
            float y = Float.parseFloat(coords[1]);
            path.moveTo(x, y);
            currentX = x;
            currentY = y;
          }
          break;

        case 'L':
        case 'l':
          if (coords.length >= 2) {
            float x = Float.parseFloat(coords[0]);
            float y = Float.parseFloat(coords[1]);
            path.lineTo(x, y);
            currentX = x;
            currentY = y;
          }
          break;

        case 'C':
        case 'c':
          if (coords.length >= 6) {
            float x1 = Float.parseFloat(coords[0]);
            float y1 = Float.parseFloat(coords[1]);
            float x2 = Float.parseFloat(coords[2]);
            float y2 = Float.parseFloat(coords[3]);
            float x = Float.parseFloat(coords[4]);
            float y = Float.parseFloat(coords[5]);
            path.curveTo(x1, y1, x2, y2, x, y);
            currentX = x;
            currentY = y;
          }
          break;

        case 'z':
        case 'Z':
          path.closePath();
          break;
      }
    }

    return path;
  }

  public static java.awt.Color getPlayerColor(Color color) {
    return switch (color) {
      case YELLOW -> new java.awt.Color(255, 255, 0);
      case BLUE -> new java.awt.Color(0, 100, 255);
      case RED -> new java.awt.Color(255, 50, 50);
      case GREEN -> new java.awt.Color(0, 200, 0);
    };
  }
}
