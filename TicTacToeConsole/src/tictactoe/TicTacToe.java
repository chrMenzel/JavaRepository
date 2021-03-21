package tictactoe;

import java.util.Scanner;

public class TicTacToe {

  public static void main(String[] args) {
    
    int rowsCount = 3;
    int columnsCount = 3;
    
    String[][] board = new String[rowsCount][columnsCount];
    
    for (int row = 0; row <= rowsCount -1; row++) {
      for (int column = 0; column <= columnsCount - 1; column++) {
        board[row][column] = " ";
      }
    }
    
    String line = buildLineForSeperatingRows(columnsCount);
    
    printDescription();
    
    int currentPlayer = 1;
    Scanner scanner = new Scanner(System.in);
    
    int zug = 1;
    String winner;
    
    do {
      drawBoard(rowsCount, columnsCount, board, line);
      
      int row;
      int column;
      
      boolean firstTry = true;
      do {
        if (!firstTry) {
          System.out.println("Das Feld ist schon besetzt, bitte wählen Sie ein anderes Feld!");
        }
        System.out.print("\nSpieler " + currentPlayer + ": Bitte Feld für Stein wählen (A1-3, B1-3, C1-3): ");
        
        String userEntry = scanner.nextLine();
        
        column = getColumnFromEntry(userEntry);
        row = Integer.valueOf(userEntry.substring(1, 2)) - 1;
        firstTry = false;
            
      } while (!board[row][column].equals(" "));
      
      if (currentPlayer == 1) {
        board[row][column] = "x";
        currentPlayer = 2;
      } else {
        board[row][column] = "o";
        currentPlayer = 1;
      }
      
      winner = checkBoard(board, rowsCount, columnsCount);
      zug++;
    } while ((zug <= rowsCount * columnsCount) && (winner == null));
    
    drawBoard(rowsCount, columnsCount, board, line);
    
    if (winner != null) {
      System.out.println(winner);
    } else {
      System.out.println("\nUnentschieden!");
    }
    
    scanner.close();
  }

  private static String checkBoard(String[][] board, int rowsCount,
      int columnsCount) {
    
    String winner = checkForWinner(board, rowsCount, columnsCount);
    
    if (winner != null) {
      if (winner.equals("x")) {
        return "\nGlückwunsch, Spieler 1 hat gewonnen!";
      } else if (winner.equals("o")) {
        return "\\nGlückwunsch, Spieler 2 hat gewonnen!";
      }
    }
    return null;
  }

  /**
   * @param board
   * @param rowsCount
   * @param columnsCount
   * @return
   */
  private static String checkForWinner(String[][] board, int rowsCount,
      int columnsCount) {
    
    // check rows first
    String winner = checkRows(board, rowsCount, columnsCount);
    
    // if no winner so far, check columns
    if (winner == null) {
      winner = checkColumns(board, rowsCount, columnsCount);
    }
    
    // if no winner so far, check diagonals
    if (winner == null) {
      winner = checkDiagonals(board, rowsCount, columnsCount);
    }
    
    return winner;
  }

  private static String checkRows(String[][] board, int rowsCount,
      int columnsCount) {
    // Diese Methode ist darauf ausgelegt, auch eine Zeile mit mehr als 3 Feldern zu prüfen.
    // Sie geht also über ein klassisches Tic Tac Toe hinaus.
    // Wenn man immer von einem 3 x 3 - Feld ausgeht, könnte man das mit weniger Code-Zeilen programmieren.
    
    // Loop over all rows
    for (int row = 0; row < rowsCount; row++) {
      
      // Start with the first column and save the value
      int column = 0;
      String stone = board[row][0];
      
      // if there is no value, leave
      if (!stone.equals(" ")) {
        // look, if the following fields have the same value
        while (column <= columnsCount - 1) {
          if (!board[row][column].equals(stone)) {
            // if not, leave this loop
            break;
          }
          column++;
        }
      }
      // if the loop could check the very last field, then there is a winner
      if (column == columnsCount) {
        return stone;
      }
    }
    return null;
  }
  
  private static String checkColumns(String[][] board, int rowsCount,
      int columnsCount) {
    // Diese Methode ist darauf ausgelegt, auch eine Zeile mit mehr als 3 Feldern zu prüfen.
    // Sie geht also über ein klassisches Tic Tac Toe hinaus.
    // Wenn man immer von einem 3 x 3 - Feld ausgeht, könnte man das mit weniger Code-Zeilen programmieren.
    
    // Loop over all columns
    for (int column = 0; column < columnsCount; column++) {
      
      // Start with the first row and save the value
      int row = 0;
      String stone = board[row][column];
      
      // if there is no value, leave
      if (!stone.equals(" ")) {
        // look, if the following fields have the same value
        while (row <= rowsCount - 1) {
          if (!board[row][column].equals(stone)) {
            // if not, leave this loop
            break;
          }
          row++;
        }
      }
      // if the loop could check the very last field, then there is a winner
      if (row == rowsCount) {
        return stone;
      }
    }
    return null;
  }
  
  private static String checkDiagonals(String[][] board, int rowsCount,
      int columnsCount) {
    // Diese Methode ist darauf ausgelegt, auch eine Zeile mit mehr als 3 Feldern zu prüfen.
    // Sie geht also über ein klassisches Tic Tac Toe hinaus.
    // Wenn man immer von einem 3 x 3 - Feld ausgeht, könnte man das mit weniger Code-Zeilen programmieren.
    
    // Loop over the diagonal from upper left to lower right
    // Start with the position (0, 0) and save the value
    int position = 0;
    String stone = board[0][0];
    
    // if there is no value, leave
    if (!stone.equals(" ")) {
      // look, if the following fields have the same value
      while (position <= rowsCount - 1) {
        if (!board[position][position].equals(stone)) {
          // if not, leave this loop
          break;
        }
        position++;
      }
    }
    // if the loop could check the very last field, then there is a winner
    if (position == rowsCount) {
      return stone;
    }
    
    // -----------------------
    // If we haven't returned so far, check the diagonal from the upper right side to the lower left side
    // Loop over the diagonal from upper left to lower right
    // Start with the position (0, 0) and save the value
    int row = 0;
    int column = columnsCount - 1;
    stone = board[row][column];
    
    // if there is no value, leave
    if (!stone.equals(" ")) {
      // look, if the following fields have the same value
      while (row <= rowsCount - 1) {
        if (!board[row][column].equals(stone)) {
          // if not, leave this loop
          break;
        }
        column--;
        row++;
      }
    }
    // if the loop could check the very last field, then there is a winner
    if (row == rowsCount) {
      return stone;
    }
    
  return null;
  }

  /**
   * @param columnsCount
   * @return
   */
  private static String buildLineForSeperatingRows(int columnsCount) {
    String line = "|";
    int signsNeeded = columnsCount * 4;
    
    for (int i = 1; i <= signsNeeded; i++) {
      if (i % 4 == 0) {
        line += "|";
      } else {
        line += "-";
      }
    }
    return line;
  }

  private static void printDescription() {
    System.out.println("Tic Tac Toe - Spielverlauf");
    System.out.println("Auf einem quadratischen, 3×3 Felder großen Spielfeld setzen 2 Spieler");
    System.out.println("abwechselnd ihr Zeichen (ein Spieler Kreuze, der andere Kreise) in ein freies Feld.");
    System.out.println("Der Spieler, der als Erster drei Zeichen in eine Zeile, Spalte oder Diagonale setzen kann, gewinnt.");
  }

  private static int getColumnFromEntry(String userEntry) {
    String strColumn = userEntry.substring(0, 1);
    
    // Dieser Teil müsste noch umgeschrieben werden um ggf. auch ein größeres Tic Tac Toe zu spielen.
    switch (strColumn) {
      case "A": return 0;
      case "B": return 1;
      default: return 2;
    }
  }

  /**
   * @param rowsCount
   * @param columnsCount
   * @param board
   * @param line
   */
  private static void drawBoard(int rowsCount, int columnsCount,
    String[][] board, String line) {
    
    System.out.println("Aktuelles Spielfeld:");
    System.out.println("\n" + line);
    
    for (int row = 0; row <= rowsCount -1; row++) {
      for (int column = 0; column <= columnsCount - 1; column++) {
        if (column == 0) {
          System.out.print("| ");
        }
        
        System.out.print(board[row][column] + " | ");
      }
      
      System.out.println("\n" + line);
    }
  }
}