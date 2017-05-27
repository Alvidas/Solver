package MW.game;

import java.util.List;


public class Cell {

  private Index index;

  public enum Status {
    OPEN, CLOSED;
  }

  private Status status;

  private boolean isMine;

  private int numberOfAdjacentMines;

  private List<Cell> adjacentCells;

  public void print() {
    if (this.isMine()) {
      System.out.print("M ");
    } else {
      if (this.getNumberOfAdjacentMines() > 0) {
        System.out.print(this.getNumberOfAdjacentMines() + " ");
      } else {
        System.out.print(". ");
      }
    }
  }


  public void setIndex(int i, int j) {
    this.index = new Index(i, j);
  }

  public Index getIndex() {
    return index;
  }


  public void setStatus(Status status) { this.status = status; }

  public boolean isOpened() {
    return this.status == Status.OPEN;
  }

  boolean isMine() {
    return isMine;
  }

  public void setIsMine(boolean isMine) {
    this.isMine = isMine;
  }

  public int getNumberOfAdjacentMines() {
    return numberOfAdjacentMines;
  }

  public void setNumberOfAdjacentMines(int numberOfAdjacentMines) {
    this.numberOfAdjacentMines = numberOfAdjacentMines;
  }

  public List<Cell> getAdjacentCells() {
    return adjacentCells;
  }

  public void setAdjacentCell(List<Cell> adjacentCells) {
    this.adjacentCells = adjacentCells;
  }
}
