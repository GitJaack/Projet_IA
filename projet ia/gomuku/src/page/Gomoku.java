package page;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Gomoku {
    private final int SIZE = 15; // Taille standard du plateau Gomoku
    private final char EMPTY = '.';
    private final char[] PLAYERS = { 'X', 'O' };
    private int currentPlayerIndex = 0;
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
            try {
                int col = scanner.nextInt();
                int row = scanner.nextInt();

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
            } catch (InputMismatchException e) {
                System.out.println("Please enter valid integer coordinates.");
                scanner.nextLine();
            }
        }
        scanner.close();
    }

    public void printBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    public boolean isWin(int row, int col) {
        char player = board[row][col];
        return checkLine(player, row, col, 1, 0) || // Horizontal
                checkLine(player, row, col, 0, 1) || // Vertical
                checkLine(player, row, col, 1, 1) || // Diagonal
                checkLine(player, row, col, 1, -1); // Anti-diagonal
    }

    public boolean checkLine(char player, int row, int col, int dRow, int dCol) {
        int count = 1;
        int r, c;

        // Vérifiez dans la direction positive (dRow, dCol)
        r = row + dRow;
        c = col + dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
            count++;
            r += dRow;
            c += dCol;
        }

        // Vérifiez dans la direction négative (-dRow, -dCol)
        r = row - dRow;
        c = col - dCol;
        while (r >= 0 && r < SIZE && c >= 0 && c < SIZE && board[r][c] == player) {
            count++;
            r -= dRow;
            c -= dCol;
        }
        // Vérifie si la ligne contient au moins 5 pierres
        return count >= 5;
    }

    public boolean placeStone(int x, int y) {
        if (board[x][y] == EMPTY) {
            board[x][y] = PLAYERS[currentPlayerIndex];
            return true;
        }
        return false;
    }

    public void reset() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                board[i][j] = EMPTY;
            }
        }
        currentPlayerIndex = 0;
    }

    public void switchPlayer() {
        currentPlayerIndex = 1 - currentPlayerIndex; // Alterne entre 0 et 1
    }

    public char getCurrentPlayer() {
        return PLAYERS[currentPlayerIndex];
    }

    public char[][] getBoard() {
        return board;
    }

    public int getSize() {
        return this.SIZE;
    }

    public static void main(String[] args) {
        Gomoku game = new Gomoku();
        game.playGame();
    }
}
