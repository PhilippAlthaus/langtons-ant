package userinterface;

import game.Ant;
import game.Board;
import game.Cell;
import game.Coordinate;
import game.Grid;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

/**
 * Shell to run Langton's Ant.
 */
public class Shell {

  // ANSI escape sequence to reset the color
  private static final String ANSI_RESET = "\u001B[0m";

  // state configuration representation
  private static final char TURN_RIGHT = 'R';
  private static final char TURN_LEFT = 'L';

  // currently possible number of states (2-12 are possible)
  private static final int MINIMUM_NUMBER_OF_STATES = 2;
  private static final int MAXIMUM_NUMBER_OF_STATES = 12;

  // currently possible number of ants
  private static final int MAXIMUM_NUMBER_OF_ANTS = 1;

  // common error messages
  private static final String INVALID_INPUT = "Error! Invalid input.";
  private static final String INVALID_STATES = "Error! Invalid states.";
  private static final String COMMAND_DOESNT_EXIST = "Error! This command does not exist.";
  private static final String NO_BOARD_EXISTING = "Error! No board existing.";
  private static final String NO_ANT_EXISTING = "Error! No ant existing.";
  private static final String TOO_MANY_ANTS = "Error! Only one ant is allowed at once.";
  private static final String INDEX_OUT_OF_RANGE = "Error! Index out of range.";

  private Shell() {
    // generating objects of this class is not intended
    throw new AssertionError();
  }

  /**
   * Starts a new shell that waits for user input.
   * 
   * @param args command line arguments
   * @throws IOException if a problem with the InputStream occurs
   */
  public static void main(String[] args) throws IOException {
    final BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));
    runAnt(stdin);
  }

  /**
   * Helper method to run the default mode.
   * 
   * @throws IOException if the InputStream fails
   */
  private static void runAnt(final BufferedReader stdin) throws IOException {
    Grid game = null;
    boolean run = true;

    while (run) {
      System.out.print("ant> ");

      final String input = stdin.readLine();
      if (input == null) {
        break;
      }

      final String[] tokens = input.trim().split("\\s+");
      final String firstInput = tokens[0];
      final ShellCommand command = identifyCommand(firstInput, tokens);

      switch (command) {
        case NEW:
          game = newHelper(game, tokens);
          break;
        case ANT:
          antHelper(game, tokens);
          break;
        case UNANT:
          unantHelper(game);
          break;
        case STEP:
          stepHelper(game);
          break;
        case MULTIPLE_STEPS:
          stepHelper(game, tokens);
          break;
        case PRINT:
          printHelper(game);
          break;
        case CLEAR:
          clearHelper(game);
          break;
        case RESIZE:
          resizeHelper(game, tokens);
          break;
        case HELP:
          helpPrinter();
          break;
        case QUIT:
          run = false;
          break;
        default:
          printError(COMMAND_DOESNT_EXIST);
          break;
      }
    }
  }

  /** Helper method for the command "new". Also checks all parameters for errors. */
  private static Grid newHelper(final Grid game, final String[] parameters) {
    boolean parametersAreNonNegative = !containsNegativeNumbers(parameters);
    boolean statesAreInvalid = !checkForCorrectStates(parameters[parameters.length - 1]);

    if (parametersAreNonNegative) {
      printError(INVALID_INPUT);
      return game;
    } else if (statesAreInvalid) {
      printError(INVALID_STATES);
      return game;
    }

    final int columns = Integer.parseInt(parameters[1]);
    final int rows = Integer.parseInt(parameters[2]);
    final String states = parameters[3];

    return Board.create(columns, rows, states);
  }

  /** Helper method for the command "ant". Also checks all parameters for errors. */
  private static void antHelper(final Grid game, final String[] parameters) {
    if (game == null) {
      printError(NO_BOARD_EXISTING);
      return;
    } else if (game.getAnts().size() == MAXIMUM_NUMBER_OF_ANTS) {
      printError(TOO_MANY_ANTS);
      return;
    } else if (!containsNegativeNumbers(parameters)) {
      printError(INVALID_INPUT);
      return;
    }

    final int xCoordinate = Integer.parseInt(parameters[1]);
    final int yCoordinate = Integer.parseInt(parameters[2]);

    if (xCoordinate >= game.getWidth() || yCoordinate >= game.getHeight()) {
      printError(INDEX_OUT_OF_RANGE);
      return;
    }

    game.setAnt(Ant.create(xCoordinate, yCoordinate), xCoordinate, yCoordinate);
  }

  /** Helper method for the command "unant". */
  private static void unantHelper(final Grid game) {
    if (game == null) {
      printError(NO_BOARD_EXISTING);
      return;
    } else if (game.getAnts().isEmpty()) {
      printError(NO_ANT_EXISTING);
    } else {
      game.clearAnts();
    }
  }

  /** Helper method for the command "step". */
  private static void stepHelper(final Grid game) {
    if (game != null && !game.getAnts().isEmpty()) {
      game.performStep();
      System.out.println(game.getStepCount());
    } else if (game != null && game.getAnts().isEmpty()) {
      printError(NO_ANT_EXISTING);
    } else {
      printError(NO_BOARD_EXISTING);
    }
  }

  /**
   * Helper method for the command "step 'n'" i.e. multiple steps. Also checks all parameters for
   * errors.
   */
  private static void stepHelper(final Grid game, final String[] parameters) {
    if (!checkForInvalidInput(parameters)) {
      printError(INVALID_INPUT);
      return;
    } else if (game == null) {
      printError(NO_BOARD_EXISTING);
      return;
    }
    final int numberOfSteps = Integer.parseInt(parameters[1]);
    if (numberOfSteps > 0) {
      if (game.getAnts().isEmpty()) {
        printError(NO_ANT_EXISTING);
        return;
      }
      game.performStep(numberOfSteps);
    } else {
      if (numberOfSteps == 0) {
        printError(INVALID_INPUT);
        return;
      }
      game.reset(-numberOfSteps);
    }
    System.out.println(game.getStepCount());
  }

  /** Helper method for the command "print". */
  private static void printHelper(final Grid game) {
    if (game != null) {
      printGrid(game);
    } else {
      printError(NO_BOARD_EXISTING);
    }
  }

  /** Helper method for the command "clear". */
  private static void clearHelper(final Grid game) {
    if (game != null) {
      game.clear();
    } else {
      printError(NO_BOARD_EXISTING);
    }
  }

  /** Helper method for the command "resize". */
  private static void resizeHelper(final Grid game, final String[] parameters) {
    if (game == null) {
      printError(NO_BOARD_EXISTING);
      return;
    } else if (containsNegativeNumbers(parameters)) {
      printError(INVALID_INPUT);
      return;
    }

    final int columns = Integer.parseInt(parameters[1]);
    final int rows = Integer.parseInt(parameters[2]);

    game.resize(columns, rows);
  }

  /** Helper method to print the help texts for all commands. */
  private static void helpPrinter() {
    System.out.println("\n=== All possible commands: ===\n");
    for (final ShellCommand cmd : ShellCommand.values()) {
      System.out.println(cmd.getHelpText() + "\n");
    }
  }

  /** Prints a textual representation of the current grid. */
  private static void printGrid(Grid game) {
    for (int i = 0; i < game.getHeight(); i++) {
      int j = 0;
      for (Cell c : game.getRow(i)) {
        for (final StateRepresentation state : StateRepresentation.values()) {
          if (state.getNumber() == c.getState()) {
            System.out.print(
                state.getColor() + printCell(game, i, j, state.getRepresentation()) + ANSI_RESET);
          }
        }
        j++;
      }
      System.out.println();
    }
  }

  /** Prints the current cell (with or without ant). */
  private static String printCell(Grid game, int y, int x, String state) {
    if (game.getAnts().isEmpty()) {
      return state;
    }

    Ant currentAnt = null;
    Coordinate antCoordinate = null;

    for (Map.Entry<Coordinate, Ant> entry : game.getAnts().entrySet()) {
      antCoordinate = entry.getKey();
      currentAnt = entry.getValue();
    }

    final int antX = antCoordinate.getX();
    final int antY = antCoordinate.getY();

    if (antX == x && antY == y) {
      switch (currentAnt.getOrientation()) {
        case EAST:
          return ">";
        case NORTH:
          return "^";
        case SOUTH:
          return "v";
        case WEST:
          return "<";
        default:
          break;
      }
    }
    return state;
  }

  /** Helper method identify a given command. */
  private static ShellCommand identifyCommand(String possibleCommand, final String[] parameters) {
    possibleCommand = possibleCommand.toLowerCase();

    if (possibleCommand.trim().isEmpty()) {
      // catch empty input
      return ShellCommand.UNKNOWN;
    }

    for (final ShellCommand cmd : ShellCommand.values()) {
      boolean commandHasCorrectString = cmd.getCommandAsString().equals(possibleCommand);
      // check if the command is the first letter of a possible command
      boolean commandIsFirstLetter = cmd.getCommandAsString().charAt(0) == possibleCommand.charAt(0)
          && possibleCommand.length() == 1;
      boolean commandHasCorrectParameterNumber = cmd.getParameterNumber() == parameters.length;
      boolean commandIsCorrect =
          (commandHasCorrectString || commandIsFirstLetter) && commandHasCorrectParameterNumber;

      if (commandIsCorrect) {
        return cmd;
      }
    }
    // command does not exist
    return ShellCommand.UNKNOWN;
  }

  /** Checks if a String[] (beginning at index 1) contains only numbers. */
  private static boolean checkForInvalidInput(final String[] input) {
    for (int i = 1; i < input.length; i++) {
      try {
        Integer.parseInt(input[i]);
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return true;
  }

  /** Checks if a String[] (beginning at index 1) contains non-negative numbers. */
  private static boolean containsNegativeNumbers(final String[] input) {
    for (int i = 1; i < input.length; i++) {
      try {
        if (Integer.parseInt(input[i]) < 0) {
          return true;
        }
      } catch (NumberFormatException e) {
        return false;
      }
    }
    return false;
  }

  /**
   * Checks if the correct state configuration was used i.e. if the given String consists only of
   * the capital letters 'R' and 'L' and its length is between 2 and 12.
   */
  private static boolean checkForCorrectStates(final String input) {
    if (input.length() < MINIMUM_NUMBER_OF_STATES || input.length() > MAXIMUM_NUMBER_OF_STATES) {
      return false;
    }
    for (final char currentChar : input.toCharArray()) {
      if (currentChar != TURN_RIGHT && currentChar != TURN_LEFT) {
        return false;
      }
    }
    return true;
  }

  /** Prints the specified string to standard out. */
  private static void printError(String errorMessage) {
    System.err.println(errorMessage);
  }

}
