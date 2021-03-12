package game;

/**
 * An interface for all classes implementing a Cell for the board of Langton's Ant.
 */
public interface Cell {

  /**
   * Returns the {@code Cell}'s state.
   */
  int getState();
}
