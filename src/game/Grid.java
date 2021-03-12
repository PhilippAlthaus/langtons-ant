package game;

import java.util.List;
import java.util.Map;

/**
 * An interface for all classes implementing the game Langton's Ant. Specifies all operations that
 * should be supported. Langton's Ant is mathematical game consisting of an ant that moves on the
 * grid in a specified way and switches the color of the positions it visited. The game can also be
 * descriped as cellular automaton and is Turing complete.
 */
public interface Grid {

  /**
   * Adds a new {@code Ant} to the {@code Grid}.
   * 
   * @param object the {@code Ant} to be added
   * @param col x-Coordinate of the {@code Ant} to be added
   * @param row y-Coordinate of the {@code Ant} to be added
   */
  void setAnt(Ant object, int col, int row);

  /**
   * Returns all ants that are currently on the grid.
   */
  Map<Coordinate, Ant> getAnts();

  /**
   * Deletes all ants on the grid.
   */
  void clearAnts();

  /**
   * Computes the next round.
   */
  void performStep();

  /**
   * Computes multiple rounds.
   * 
   * @param number the number of rounds to be computed
   */
  void performStep(int number);

  /**
   * Resets the current {@code Grid} by a given number of rounds.
   * 
   * @param number the number of rounds to be resetted
   */
  void reset(int number);

  /**
   * Returns the width (x-Dimension) of the {@code Grid}.
   */
  int getWidth();

  /**
   * Returns the height (y-Dimension) of the grid.
   */
  int getHeight();

  /**
   * Returns the column at the given x-Coordinate.
   * 
   * @param i the x-Coordinate
   */
  List<Cell> getColumn(int i);

  /**
   * Returns the row at the given y-Coordinate.
   * 
   * @param j the y-Coordinate
   */
  List<Cell> getRow(int j);

  /**
   * Changes the size of the {@code Grid}.
   * 
   * @param cols the new number of cols
   * @param rows the new number of rows
   */
  void resize(int cols, int rows);

  /**
   * Resets the entire {@code Grid} i.e. resets all cells, deletes the current {@code Ant}.
   */
  void clear();

  /**
   * Returns the current number of steps.
   */
  int getStepCount();
}
