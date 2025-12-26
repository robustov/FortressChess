package org.robustov.chess.ui;

import org.robustov.chess.model.Board;
import org.robustov.chess.model.Color;
import org.robustov.chess.model.Position;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

public class ChessFrame extends JFrame {
  private Board board;
  private final BoardPanel boardPanel;
  private boolean gameOver = false;

  public ChessFrame(Board board) {
    this.board = board;
    setTitle("Шахматы-Крепость (Русские четвертные шахматы с крепостями)");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setResizable(false);

    boardPanel = new BoardPanel(board);
    boardPanel.addBoardListener(this::handleMove);

    add(boardPanel);

    createMenuBar();

    pack();
    setLocationRelativeTo(null);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu gameMenu = new JMenu("Игра");
    JMenuItem restartItem = new JMenuItem("Начать заново");
    restartItem.addActionListener(e -> restartGame());
    gameMenu.add(restartItem);

    JMenuItem exitItem = new JMenuItem("Выход");
    exitItem.addActionListener(e -> System.exit(0));
    gameMenu.add(exitItem);

    menuBar.add(gameMenu);

    JMenu helpMenu = new JMenu("Помощь");
    JMenuItem aboutItem = new JMenuItem("О программе");
    aboutItem.addActionListener(e -> showAboutDialog());
    helpMenu.add(aboutItem);

    menuBar.add(helpMenu);

    setJMenuBar(menuBar);
  }

  private void handleMove(Position source, Position target) {
    if (gameOver) {
      showGameOverAlreadyDialog();
      return;
    }

    try {
      board.movePiece(source, target);
      boardPanel.repaint();
      checkGameEnd();
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Неверный ход: " + e.getMessage(),
          "Ошибка хода",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void checkGameEnd() {
    Set<Color> activePlayers = new HashSet<>();
    for (Color color : Color.values()) {
      if (board.isPlayerActive(color)) {
        activePlayers.add(color);
      }
    }

    if (activePlayers.size() <= 1) {
      gameOver = true;
      Color winner = activePlayers.isEmpty() ? null : activePlayers.iterator().next();
      showGameOverDialog(winner);
    }
  }

  private void showGameOverDialog(Color winner) {
    StringBuilder message = new StringBuilder();
    if (winner != null) {
      message.append("🏆 Игра окончена! 🏆\n\n");
      message.append("Победитель: ").append(winner).append("\n\n");
      message.append("Все остальные игроки устранены!");
    } else {
      message.append("🏆 Игра окончена! 🏆\n\n");
      message.append("Нет оставшихся игроков!");
    }

    int option = JOptionPane.showOptionDialog(
        this,
        message.toString(),
        "Игра окончена",
        JOptionPane.YES_NO_OPTION,
        JOptionPane.INFORMATION_MESSAGE,
        null,
        new String[] { "Играть снова", "Выход" },
        "Играть снова");

    if (option == JOptionPane.YES_OPTION) {
      restartGame();
    } else {
      System.exit(0);
    }
  }

  private void showGameOverAlreadyDialog() {
    JOptionPane.showMessageDialog(this,
        "Игра уже завершена!\nПожалуйста, начните заново, чтобы играть снова.",
        "Игра окончена",
        JOptionPane.INFORMATION_MESSAGE);
  }

  private void showAboutDialog() {
    String message = "Шахматы-Крепость\n" +
        "Русские четвертные шахматы с крепостями\n\n" +
        "Вариант шахмат для четырех игроков на доске 16x16\n" +
        "с крепостями в каждом углу.\n\n" +
        "Игроки: Желтый, Синий, Красный, Зеленый\n" +
        "Текущий ход: " + board.getCurrentPlayer();

    JOptionPane.showMessageDialog(this,
        message,
        "О программе",
        JOptionPane.INFORMATION_MESSAGE);
  }

  private void restartGame() {
    try {
      Board newBoard = new Board();
      Path defaultPath = Paths.get("../game.json");

      if (!Files.exists(defaultPath)) {
        defaultPath = Paths.get("app/../game.json");
      }

      if (!Files.exists(defaultPath)) {
        defaultPath = Paths.get("game.json");
      }

      if (Files.exists(defaultPath)) {
        String json = Files.readString(defaultPath);
        newBoard.loadPosition(json);
        JOptionPane.showMessageDialog(this,
            "Игра успешно перезапущена!",
            "Перезапуск",
            JOptionPane.INFORMATION_MESSAGE);
      } else {
        JOptionPane.showMessageDialog(this,
            "Файл игры по умолчанию не найден. Начинаем с новой доски.",
            "Файл не найден",
            JOptionPane.WARNING_MESSAGE);
      }

      this.board = newBoard;
      boardPanel.setBoard(newBoard);
      gameOver = false;
      setTitle("Шахматы-Крепость (Русские четвертные шахматы с крепостями)");
      boardPanel.repaint();
    } catch (IOException e) {
      JOptionPane.showMessageDialog(this,
          "Ошибка чтения файла игры: " + e.getMessage(),
          "Ошибка файла",
          JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Ошибка перезапуска игры: " + e.getMessage(),
          "Ошибка перезапуска",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
