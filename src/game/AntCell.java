package game;

/**
 * This class represents a Cell for the board for Langton's Ant.
 */
public class AntCell implements Cell {

  /**
   * The {@code AntCell}'s state.
   */
  private int state;

  /**
   * Creates a new {@code AntCell}.
   */
  private AntCell() {
    this.state = 0;
  }

  /**
   * Creates a new {@code AntCell}.
   * 
   * @return the newly created {@code AntCell}
   */
  public static AntCell create() {
    return new AntCell();
  }

  @Override
  public int getState() {
    return state;
  }

  /**
   * Switches the {@code Cell}'s state to the next state. In order to follow the game's rules the
   * first state will be taken if an overflow would occur.
   */
  void switchToNextState(int states) {
    state = (state + 1) % states;
  }

  /**
   * Switches the {@code Cell}'s state to the previous state. In order to follow the game's rules
   * the last state will be taken if an overflow would occur.
   */
  void switchToPreviousState(int states) {
    state = (state + (states - 1)) % states;
  }

}
