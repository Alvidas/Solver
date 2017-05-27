package MW;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

import MW.game.Board;
import MW.game.Cell;


public class Solver {

  private static final int RUNS = 10000;
  private static final int SIZE = 20;
  private static final int MINES = 20;

  public static void main(String[] args) {
    int numberOfWins = 0;
    System.out.println(RUNS + " runs");
    System.out.println("Running...");
    final long startTime = System.currentTimeMillis();
    for (int i=0; i<RUNS; i++) {
      Board board = new Board(SIZE, MINES);
      board.initBoard();

      if (Solver.solve(board)) {
        numberOfWins++;
      }
    }
    final long finishTime = System.currentTimeMillis();
    final double winRate = (numberOfWins*100.0)/RUNS;
    final double timeTaken = (finishTime - startTime)/1000.0;

    System.out.println("Number of wins: " + numberOfWins);
    System.out.println("Win " + winRate + "%");
    System.out.println("Time " + timeTaken + " s");
  }


  //Solve возвращает true или false в зависимости от исхода

  public static boolean solve(Board board) {
    boolean win = false;

    Queue<Cell> choosenCells = new LinkedList<Cell>();

    // Начинаем с случайной ячейки
    Random randomGenerator = new Random();
    int randomInt = randomGenerator.nextInt(board.getUnopenedCells().size());

    // Мы поддерживаем очередь выбранных ячеек, которую хотим открыть.
    // Эта очередь будет заполняться с помощью метода selectCells ().
    // Когда эта очередь становится пустой, это означает, что у нас нет ничего, что можно открыть,
    // это значит что все открыто и игра решена.
    choosenCells.add(board.getUnopenedCells().get(randomInt));
    while(!choosenCells.isEmpty()) {
      Cell cell = choosenCells.poll();
      if (!Board.openCell(board, cell)) {
        // openCell() возвращает false если открыли клетку с миной.
        break;
      } else {
        choosenCells.addAll(chooseCells(board));
        // Если не найдено новых ячеек, то это значит выйгрыш.
        if (choosenCells.isEmpty()) {
          win = true;
        }
      }
    }

    return win;
  }

  /**
   * Логика для выбора новой ячейки:
   * Каждый раз просматривайте только закрытые ячейки.
   * Вычислить количество мин, уже отмеченных рядом с этой ячейкой = X
   * Рассчитать количество смежных ячеек, оставшихся неоткрытыми = Y
   *
   * Если этот номер ячейки OfAdjMines == X, тогда верните все Y в качестве безопасных ячеек, которые нужно открыть.
   * (Потому что это означает, что мы уже правильно отметили возможные мины для этой ячейки.
   * Если номер этой ячейки AdjMines == X + Y, то все Y - мины (потому что это единственное
   * Путь, который может быть правдой).
   * Поэтому мы обозначаем все Y как мины.
   */
  private static List<Cell> chooseCells(Board board) {
    for (int i=0; i<board.getMatrixSize(); i++) {
      for (int j=0; j<board.getMatrixSize(); j++) {
        Cell cell = board.getCells()[i][j];

        // Если ячейка уже открыта, она не будет выбрана.
        if (cell.isOpened()) {
          continue;
        }

        List<Cell> unopenedAdjacentCells = new ArrayList<Cell>();
        int numberAdjacentFlaggedMines = 0;
        for (Cell adjCell : cell.getAdjacentCells()) {
          if (!adjCell.isOpened()) {
            if (board.getUnopenedCells().contains(adjCell)) {
              unopenedAdjacentCells.add(adjCell);
            } else {
              numberAdjacentFlaggedMines++;
            }
          }
        }

        if (cell.getNumberOfAdjacentMines() != 0 &&
            cell.getNumberOfAdjacentMines() == numberAdjacentFlaggedMines) {
          return unopenedAdjacentCells;
        }

        if (cell.getNumberOfAdjacentMines() ==
            (unopenedAdjacentCells.size() + numberAdjacentFlaggedMines)) {
          board.getUnopenedCells().removeAll(unopenedAdjacentCells);
        }
      }
    }

    // Если нет безопасных ячеек, выбираем случайным образом.
    // Но не обращаем внимания на помеченые ячейки,
    // потому что мы удаляем их из списка неоткрытых ячеек, когда мы уверены, что они мины.
    if (board.getUnopenedCells().size() > 0) {
      Random randomGenerator = new Random();
      int randomInt = randomGenerator.nextInt(board.getUnopenedCells().size());
      Cell cell = board.getUnopenedCells().get(randomInt);
      return Arrays.asList(cell);
    } else {
      return new ArrayList<Cell>();
    }
  }
}
