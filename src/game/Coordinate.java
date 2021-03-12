package game;

/**
 * This class represents a coordinate in two dimensional coordinate system.
 */
public class Coordinate {

  /**
   * The x-Coordinate.
   */
  private final int xCoordinate;

  /**
   * The y-Coordinate.
   */
  private final int yCoordinate;

  /**
   * Creates a new {@code Coordinate}.
   * 
   * @param x the x-Coordinate
   * @param y the y-Coordinate
   */
  public Coordinate(int x, int y) {
    this.xCoordinate = x;
    this.yCoordinate = y;
  }

  /**
   * Returns the x-Coordinate.
   */
  public int getX() {
    return xCoordinate;
  }

  /**
   * Returns the y-Coordinate.
   */
  public int getY() {
    return yCoordinate;
  }

}
