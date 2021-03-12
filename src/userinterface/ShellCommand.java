package userinterface;

/**
 * Contains all possible commands for the Shell for Lantons's Ant that are currently implemented.
 * Furthermore all associated parameter numbers  and a help text are stored.
 */
enum ShellCommand {
  NEW("new", 4,
      "new <cols> <rows> <config>: Creates a new Board."),
  ANT("ant", 3,
      "ant <x> <y>: Adds a new ant to the board. Only one ant at once is possible."),
  UNANT("unant", 1,
      "unant: Removes the current ant from the board."),
  STEP("step", 1,
      "step: Computes the next round i.e. the ant moves once."),
  MULTIPLE_STEPS("step", 2,
      "step <n>: Computes the next n rounds i.e. the next n moves of the ant."),
  PRINT("print", 1,
      "print: Prints the current state of the board."),
  CLEAR("clear", 1,
      "clear: Resets the entire board to its initial state."),
  RESIZE("resize", 3,
      "resize <cols> <rows>: Resizes the current grid. If the ant is out of range it is deleted"),
  HELP("help", 1,
      "help: Prints this help text."),
  QUIT("quit", 1,
      "quit: Exits this programm."),
  // for wrong commands
  UNKNOWN("unknown", 0, "");

  /**
   * String representation of this command.
   */
  private final String command;

  /**
   * The expected length of a String[] containing the correct number of parameters for a command.
   */
  private final int numberOfExpectedParameters;

  /**
   * Help text for the current command.
   */
  private final String helpText;

  private ShellCommand(String command, int numberOfExpectedParameters, String helpText) {
    this.command = command;
    this.numberOfExpectedParameters = numberOfExpectedParameters;
    this.helpText = helpText;
  }

  /** Returns the number of expected parameters for the command. */
  int getParameterNumber() {
    return numberOfExpectedParameters;
  }

  /** Returns the string representation for the command. */
  String getCommandAsString() {
    return command;
  }

  /** Returns the help text for the command. */
  String getHelpText() {
    return helpText;
  }

}
