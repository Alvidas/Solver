package MW.game;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;


public class Board {

  private Cell[][] cells;

  private int matrixSize;

  private int numberOfMines;

  private HashSet<Integer> mineLocations;

  List<Cell> unopenedCells = new ArrayList<Cell>();

  List<Cell> openedCells = new ArrayList<Cell>();

  public Board(int matrixSize, int numberOfMines) {
    this.matrixSize = matrixSize;
    this.numberOfMines = numberOfMines;
    this.unopenedCells = new ArrayList<Cell>();
    this.openedCells = new ArrayList<Cell>();
    this.cells = new Cell[matrixSize][matrixSize];
  }

  public void initBoard() {
    this.mineLocations = generateMineLocations();

    initializeCells();
  }

  public static boolean openCell(Board board, Cell cell) {
    if (cell.isMine()) {
      return false;
    }

    Queue<Cell> allCellsToOpen = new LinkedList<Cell>();
    allCellsToOpen.add(cell);
    while (!allCellsToOpen.isEmpty()) {
      Cell cellToOpen= allCellsToOpen.poll();

      cellToOpen.setStatus(Cell.Status.OPEN);
      board.openedCells.add(cellToOpen);
      board.unopenedCells.remove(cellToOpen);

      for (Cell adjCell : cellToOpen.getAdjacentCells()) {
        if (!adjCell.isOpened() && !allCellsToOpen.contains(adjCell) && !adjCell.isMine()) {
          if (adjCell.getNumberOfAdjacentMines() == 0) {
            allCellsToOpen.add(adjCell);
          } else {
            if (cellToOpen.getNumberOfAdjacentMines() == 0) {
              adjCell.setStatus(Cell.Status.OPEN);
              board.openedCells.add(adjCell);
              board.unopenedCells.remove(adjCell);
            }
          }
        }
      }
    }
    return true;
  }

  public int getMatrixSize() {
    return matrixSize;
  }

  public Cell[][] getCells() {
    return cells;
  }

  public List<Cell> getUnopenedCells() {
    return unopenedCells;
  }

  private HashSet<Integer> generateMineLocations() {
    HashSet<Integer> mineLocations = new HashSet<Integer>();
    Random randomGenerator = new Random();

    for (int idx = 1; idx <= numberOfMines; ++idx){
      int randomInt = randomGenerator.nextInt(matrixSize*matrixSize);
      while (mineLocations.contains(randomInt)) {
        randomInt = randomGenerator.nextInt(matrixSize*matrixSize);
      }
      mineLocations.add(randomInt);
    }
    return mineLocations;
  }

  private void initializeCells() {
    for (int i=0; i<matrixSize; i++) {
      for (int j=0; j<matrixSize; j++) {
        Cell cell = new Cell();
        cell.setIndex(i, j);
        cell.setStatus(Cell.Status.CLOSED);
        if (mineLocations.contains(i*matrixSize + j)) {
          cell.setIsMine(true);
        } else {
          cell.setIsMine(false);
        }
        cells[i][j] = cell;
        unopenedCells.add(cell);
      }
    }

    for (int i=0; i<matrixSize; i++) {
      for (int j=0; j<matrixSize; j++) {
        setAdjacentCells(cells[i][j]);
        setAdjacentMines(cells[i][j]);
      }
    }
  }

  private void setAdjacentCells(Cell cell) {
    List<Cell> adjacentCells = new ArrayList<Cell>();
    for(int i=-1; i<=1; i++) {
      for(int j=-1; j<=1; j++) {
        if(i == 0 && j == 0) {
          continue;
        }
        int x = cell.getIndex().i();
        int y = cell.getIndex().j();
        if(i + x >= 0 && i + x < getMatrixSize() && j + y >= 0 && j + y < getMatrixSize()) {
          adjacentCells.add(this.getCells()[i+x][j+y]);
        }
      }
    }
    cell.setAdjacentCell(adjacentCells);
  }

  private void setAdjacentMines(Cell cell) {
    int adjacentMines = 0;

    for (Cell adjacentCell : cell.getAdjacentCells()) {
      if (adjacentCell.isMine()) {
        adjacentMines++;
      }
    }
    cell.setNumberOfAdjacentMines(adjacentMines);
  }
}
