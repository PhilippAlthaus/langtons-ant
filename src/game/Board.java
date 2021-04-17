package game;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The board for Langton's Ant. This class is responsible for managing the game i.e. calculating the
 * rounds, resizing the grid, etc.
 */
public class Board implements Grid {

  // integer representation of where the ant has to turn
  // this should actually be a boolean or an enum
  private static final int TURN_RIGHT = 0;
  private static final int TURN_LEFT = 1;

  /**
   * The grid i.e. the actual board.
   */
  private Cell[][] grid;

  /**
   * The ant.
   */
  private Ant ant; // if more ants are implemented replace this e.g. with a {@code List<Ant>}

  /**
   * Saves the number of steps.
   */
  private int stepCount;

  /**
   * Saves the configuration i.e. where the ant turns at which state.
   */
  private Map<Integer, Integer> whereToTurn;

  /**
   * Saves the moves the ant made.
   */
  private List<Ant> antMoves;

  /**
   * Creates a new {@code Board} and initializes all cells.
   * 
   * @param width non-negative width of the {@code Board}.
   * @param height non-negative height of the {@code Board}
   * @param states the state configuration i.e. where the ant turns at which state (min 2 max 12
   *        states), currently only R and L are allowed
   */
  private Board(int width, int height, String states) {
    // initialize an empty grid
    this.grid = new Cell[width][height];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        grid[i][j] = AntCell.create();
      }
    }

    ant = null;
    stepCount = 0;
    whereToTurn = new HashMap<Integer, Integer>();

    for (int i = 0; i < states.length(); i++) {
      // to add more moves (e.g. step forward, turn around, etc.) edit this if-else-statement
      whereToTurn.put(i, states.charAt(i) == 'R' ? TURN_RIGHT : TURN_LEFT);
    }
    antMoves = new LinkedList<>();
  }

  public static Board create(int width, int height, String states) {
    return new Board(width, height, states);
  }

  @Override
  public void setAnt(Ant object, int col, int row) {
    ant = object;
  }

  @Override
  public Map<Coordinate, Ant> getAnts() {
    Map<Coordinate, Ant> ants = new HashMap<>();
    if (ant != null) {
      ants.put(new Coordinate(ant.getX(), ant.getY()), ant);
    }
    return ants;
  }

  @Override
  public void clearAnts() {
    ant = null;
  }

  @Override
  public void performStep() {
    performStep(1);
  }

  @Override
  public void performStep(int number) {
    for (int i = 0; i < number; i++) {
      // save the current ant
      antMoves.add(Ant.copyOf(ant));
      // switch the state of the cell where the ant currently is
      Cell antPosition = grid[ant.getX()][ant.getY()];
      ((AntCell) antPosition).switchToNextState(getNumberOfStates());
      // move the ant by one step
      ant.move();

      // calculate the torus
      if (ant.getX() < 0) {
        ant.setX(ant.getX() + getWidth());
      } else if (ant.getY() < 0) {
        ant.setY(ant.getY() + getHeight());
      } else {
        ant.setX(ant.getX() % getWidth());
        ant.setY(ant.getY() % getHeight());
      }

      // turn the ant to the correct direction
      antPosition = grid[ant.getX()][ant.getY()];
      if (whereToTurn.get(antPosition.getState()) == TURN_RIGHT) {
        ant.turnRight();
      } else {
        ant.turnLeft();
      }
    }
    stepCount += number;
  }

  @Override
  public void reset(int number) {
    int temp = stepCount - number;
    clear();

    for (int i = 0; i < temp; i++) {
      ant = Ant.copyOf(antMoves.get(i));
      final int antX = ant.getX();
      final int antY = ant.getY();

      if (antX < getWidth() && antY < getHeight()) {
        performStep(1);
      } else {
        // ant is out of range thus the move must not be computed
        stepCount++;
      }
    }
    antMoves.subList(temp < 1 ? 1 : temp, antMoves.size()).clear();
  }

  @Override
  public int getWidth() {
    return grid.length;
  }

  @Override
  public int getHeight() {
    return grid[0].length;
  }

  @Override
  public List<Cell> getColumn(int i) {
    List<Cell> column = new LinkedList<Cell>();
    for (int j = 0; j < getWidth(); j++) {
      column.add(grid[i][j]);
    }
    return column;
  }

  @Override
  public List<Cell> getRow(int j) {
    List<Cell> row = new LinkedList<Cell>();
    for (int i = 0; i < getWidth(); i++) {
      row.add(grid[i][j]);
    }
    return row;
  }

  @Override
  public void resize(int columns, int rows) {
    if (columns < getWidth() && rows < getHeight()) {
      grid = downsizeGrid(columns, rows);
    } else if (columns >= getWidth() && rows >= getHeight()) {
      grid = expandGrid(columns, rows);
    } else {
      // expand one axis and downsize the other at once
      grid = expandGrid(columns > getWidth() ? columns : getWidth(),
          rows > getHeight() ? rows : getHeight());
      grid = downsizeGrid(columns, rows);
    }
  }

  /** Returns a downsized grid and adjusts the position of the ant. */
  private Cell[][] downsizeGrid(int cols, int rows) {
    Cell[][] downsizedGrid = new Cell[cols][rows];
    int xAxisShift = (getWidth() - cols) / 2;
    int yAxisShift = (getHeight() - rows) / 2;

    // adjust the grid
    for (int x = 0; x < cols; x++) {
      for (int y = 0; y < rows; y++) {
        if (y < getWidth() && x < getHeight()) {
          downsizedGrid[x][y] = grid[x + xAxisShift][y + yAxisShift];
        } else {
          downsizedGrid[x][y] = AntCell.create();
        }
      }
    }

    // adjust the position of the ant
    if (ant != null) {
      int antX = ant.getX();
      int antY = ant.getY();
      if (antX >= cols + xAxisShift || antY >= rows + yAxisShift || antX < xAxisShift
          || antY < yAxisShift) {
        // ant is out of range
        ant = null;
      } else {
        ant.setX(antX - xAxisShift);
        ant.setY(antY - yAxisShift);
      }
    }

    // adjust all previous moves from the ant
    for (Ant a : antMoves) {
      a.setX(a.getX() - xAxisShift);
      a.setY(a.getY() - yAxisShift);
    }
    return downsizedGrid;
  }

  /** Returns an expanded grid and adjusts the position of the ant. */
  private Cell[][] expandGrid(int cols, int rows) {
    Cell[][] expandedGrid = new Cell[cols][rows];
    // recalculated indices to insert the old smaller board into the expanded
    int xAxisShift = (cols - getWidth()) / 2;
    int shiftedLargestxAxisIndex = (cols + getWidth()) / 2;
    int yAxisShift = (rows - getHeight()) / 2;
    int shiftedLargestyAxisIndex = (rows + getHeight()) / 2;

    // adjust the grid
    for (int x = 0; x < cols; x++) {
      for (int y = 0; y < rows; y++) {
        boolean xCoordinateIsInRange = x >= xAxisShift && x < shiftedLargestxAxisIndex;
        boolean yCoordinateIsInRange = y >= yAxisShift && y < shiftedLargestyAxisIndex;
        boolean currentPositionHasToBeInsertedFromOldGrid =
            xCoordinateIsInRange && yCoordinateIsInRange;

        if (currentPositionHasToBeInsertedFromOldGrid) {
          expandedGrid[x][y] = grid[x - xAxisShift][y - yAxisShift];
        } else {
          expandedGrid[x][y] = AntCell.create();
        }
      }
    }

    // adjust the position of the ant
    if (ant != null) {
      ant.setX(ant.getX() + xAxisShift);
      ant.setY(ant.getY() + yAxisShift);
    }

    // adjust all previous moves from the ant
    for (Ant a : antMoves) {
      a.setX(a.getX() + xAxisShift);
      a.setY(a.getY() + yAxisShift);
    }
    return expandedGrid;
  }

  @Override
  public void clear() {
    for (int i = 0; i < getWidth(); i++) {
      for (int j = 0; j < getHeight(); j++) {
        grid[i][j] = AntCell.create();
      }
    }
    ant = null;
    stepCount = 0;
  }

  @Override
  public int getStepCount() {
    return stepCount;
  }

  /** Returns the number of states. */
  int getNumberOfStates() {
    return whereToTurn.size();
  }

}
