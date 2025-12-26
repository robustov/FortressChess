package org.robustov;

import org.robustov.chess.model.Board;
import org.robustov.chess.ui.ChessFrame;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      Board board = new Board();
      Options options = createOptions();

      try {
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("load")) {
          String filename = cmd.getOptionValue("load");
          Path filePath = resolveFilePath(filename);

          if (Files.exists(filePath)) {
            try {
              String json = Files.readString(filePath);
              board.loadPosition(json);
              System.out.println("Loaded game from " + filePath.toAbsolutePath());
            } catch (Exception e) {
              System.err.println("Error loading game from " + filePath.toAbsolutePath() + ": " + e.getMessage());
              System.err.println("Falling back to standard position");
              board.setupFortressPosition();
            }
          } else {
            System.err.println("File not found: " + filePath.toAbsolutePath());
            System.err.println("Falling back to standard position");
            board.setupFortressPosition();
          }
        } else {
          board.setupFortressPosition();
        }
      } catch (ParseException e) {
        System.err.println("Error parsing command line arguments: " + e.getMessage());
        System.err.println("Usage: --load <filename>");
        board.setupFortressPosition();
      }

      ChessFrame frame = new ChessFrame(board);
      frame.setVisible(true);
    });
  }

  private static Options createOptions() {
    Options options = new Options();
    options.addOption(Option.builder("l")
        .longOpt("load")
        .hasArg(true)
        .argName("file")
        .desc("Load game from JSON file")
        .build());
    return options;
  }

  private static Path resolveFilePath(String filename) {
    Path path = Paths.get(filename);

    if (Files.exists(path)) {
      return path;
    }

    Path relativeToProject = Paths.get("app", filename);
    if (Files.exists(relativeToProject)) {
      return relativeToProject;
    }

    Path relativeToResources = Paths.get("app", "src", "main", "resources", filename);
    if (Files.exists(relativeToResources)) {
      return relativeToResources;
    }

    return path;
  }
}
