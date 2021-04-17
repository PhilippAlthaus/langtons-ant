package game;

/**
 * This class represents the Ant for Langton's Ant.
 */
public class Ant {

  /**
   * The {@code Ant}'s x-Coordinate.
   */
  private int xCoordinate;

  /**
   * The {@code Ant}'s y-Coordinate.
   */
  private int yCoordinate;

  /**
   * The {@code Ant}'s orientation.
   */
  private Direction orientation;

  /**
   * Creates a new {@code Ant}.
   * 
   * @param x non-negative x-Coordinate of the cell the ant is set to
   * @param y non-negative y-Coordinate of the cell the ant is set to
   */
  private Ant(int x, int y) {
    xCoordinate = x;
    yCoordinate = y;
    orientation = Direction.WEST;
  }

  /**
   * Creates a copy of a given {@code Ant}.
   * 
   * @param another the {@code Ant} to be copied
   */
  private Ant(Ant another) {
    this.xCoordinate = another.xCoordinate;
    this.yCoordinate = another.yCoordinate;
    this.orientation = another.orientation;
  }

  /**
   * Creates a new {@code Ant}.
   * 
   * @param x non-negative x-Coordinate of the cell the ant is set to
   * @param y non-negative y-Coordinate of the cell the ant is set to
   */
  public static Ant create(int x, int y) {
    return new Ant(x, y);
  }

  /**
   * Creates a (deep) copy of a given {@code Ant}.
   * 
   * @param original the {@code Ant} to be copied
   * @return the copied {@code Ant}
   */
  public static Ant copyOf(Ant original) {
    return new Ant(original);
  }

  /** Returns the x-Coordinate. */
  int getX() {
    return xCoordinate;
  }

  /** Sets the x-Coordinate. */
  void setX(int x) {
    this.xCoordinate = x;
  }

  /** Returns the y-Coordinate. */
  int getY() {
    return yCoordinate;
  }

  /** Sets the y-Coordinate. */
  void setY(int y) {
    this.yCoordinate = y;
  }

  /**
   * Returns the orientation of the {@code Ant}.
   */
  public Direction getOrientation() {
    return orientation;
  }

  /** Moves the ant one step forward (depending on its current orientation). */
  void move() {
    switch (orientation) {
      case NORTH:
        yCoordinate--;
        break;
      case EAST:
        xCoordinate++;
        break;
      case SOUTH:
        yCoordinate++;
        break;
      case WEST:
        xCoordinate--;
        break;
      default:
        throw new AssertionError();
    }
  }

  /** Turns the ant to the right i.e. 90 degrees. */
  void turnRight() {
    switch (orientation) {
      case NORTH:
        orientation = Direction.EAST;
        break;
      case EAST:
        orientation = Direction.SOUTH;
        break;
      case SOUTH:
        orientation = Direction.WEST;
        break;
      case WEST:
        orientation = Direction.NORTH;
        break;
      default:
        throw new AssertionError();
    }
  }

  /** Turns the ant to the left i.e. 270 degrees. */
  void turnLeft() {
    switch (orientation) {
      case NORTH:
        orientation = Direction.WEST;
        break;
      case EAST:
        orientation = Direction.NORTH;
        break;
      case SOUTH:
        orientation = Direction.EAST;
        break;
      case WEST:
        orientation = Direction.SOUTH;
        break;
      default:
        throw new AssertionError();
    }
  }

}
