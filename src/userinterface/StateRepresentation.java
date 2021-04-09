package userinterface;

enum StateRepresentation {
  STATE_0("\u001B[47m", "0", 0),
  STATE_1("\u001B[37;40m", "1", 1),
  STATE_2("\u001B[42m", "2", 2),
  STATE_3("\u001B[41m", "3", 3),
  STATE_4("\u001B[37;44m", "4", 4),
  STATE_5("\u001B[43m", "5", 5),
  STATE_6("\u001B[46m", "6", 6),
  STATE_7("\u001B[45m", "7", 7),
  STATE_8("\u001B[36;41m", "8", 8),
  STATE_9("\u001B[31;44m", "9", 9),
  STATE_10("\u001B[34;43m", "A", 10),
  STATE_11("\u001B[32;45m", "B", 11);

  /**
   * Color of the state as ANSI escape sequence.
   */
  private String color;

  /**
   * Textual representation of the state.
   */
  private String representation;

  /**
   * Number of the state.
   */
  private int number;

  private StateRepresentation(String color, String represantion, int number) {
    this.color = color;
    this.representation = represantion;
    this.number = number;
  }

  /** Returns the state's color. */
  String getColor() {
    return color;
  }

  /** Returns the state's textual representation. */
  String getRepresentation() {
    return representation;
  }

  /** Returns the state's number. */
  int getNumber() {
    return number;
  }

}

