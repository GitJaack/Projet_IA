package page;

import java.util.Scanner;

public class Gomoku {
    private static final int SIZE = 15; // Taille standard du plateau Gomoku
    private static final char EMPTY = '.';
    private static final char[] PLAYERS = {'X', 'O'};
    private char[][] board = new char[SIZE][SIZE];

    public Gomoku() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
    }

    public void playGame() {
        int currentPlayer = 0;
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Current board:");
            printBoard();

            System.out.println("Player " + PLAYERS[currentPlayer] + "'s turn.");
            System.out.print("Enter row and column numbers (0-14): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            if (row >= 0 && row < SIZE && col >= 0 && col < SIZE && board[row][col] == EMPTY) {
                board[row][col] = PLAYERS[currentPlayer];
                if (isWin(row, col)) {
                    printBoard();
                    System.out.println("Player " + PLAYERS[currentPlayer] + " wins!");
                    break;
                }
                currentPlayer = 1 - currentPlayer; // Switch player
            } else {
                System.out.println("Invalid move, try again.");
            }
        }
        scanner.close();
    }

    private void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private boolean isWin(int row, int col) {
        char player = board[row][col];
        return checkLine(player, row, col, 1, 0) || // Horizontal
               checkLine(player, row, col, 0, 1) || // Vertical
               checkLine(player, row, col, 1, 1) || // Diagonal
               checkLine(player, row, col, 1, -1);  // Anti-diagonal
    }

    private boolean checkLine(char player, int row, int col, int dRow, int dCol) {
        int count = 1;
        int r, c;

        // Check in one direction
        r = row + dRow;
        c = col + dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }

        // Check in the opposite direction
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
            count++;
            r -= dRow;
            c -= dCol;
        }

        return count >= 5;
    }

    public static void main(String[] args) {
        Gomoku game = new Gomoku();
        game.playGame();
    }
}
