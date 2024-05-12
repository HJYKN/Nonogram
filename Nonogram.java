import java.util.Scanner;

public class Nonogram {
    private char[][] grid;
    private int size;

    public Nonogram(int size) {
        this.size = size;
        this.grid = new char[size][size];
        // Initialize grid with unknown cells
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '?';
            }
        }
    }

    private void displayGrid() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void solvePuzzle() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            displayGrid();
            System.out.println("Enter row and column indices (separated by space) to toggle a cell (e.g., '3 5'), or 'quit' to exit:");
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("quit")) {
                System.out.println("Exiting...");
                break;
            }
            String[] parts = input.split(" ");
            if (parts.length != 2) {
                System.out.println("Invalid input. Please enter two integers separated by space.");
                continue;
            }
            try {
                int row = Integer.parseInt(parts[0]);
                int col = Integer.parseInt(parts[1]);
                if (row >= 0 && row < size && col >= 0 && col < size) {
                    if (grid[row][col] == '?') {
                        grid[row][col] = 'W';
                    } else if (grid[row][col] == 'W') {
                        grid[row][col] = 'B';
                    } else {
                        grid[row][col] = '?';
                    }
                } else {
                    System.out.println("Invalid row or column index.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter two integers separated by space.");
            }
        }
        scanner.close();
    }

    public static void main(String[] args) {
        Nonogram nonogram = new Nonogram(5);
        nonogram.solvePuzzle();
    }
}


