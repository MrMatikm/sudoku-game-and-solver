public class Sudoku {

    private static int[][] sudoku = {
            {7, 0, 0,  0, 0, 0,  0, 0, 0},
            {0, 0, 3,  4, 0, 6,  0, 0, 0},
            {6, 9, 0,  5, 0, 1,  0, 0, 2},

            {0, 1, 8,  6, 0, 0,  7, 0, 5},
            {0, 0, 0,  2, 3, 7,  1, 0, 4},
            {0, 4, 0,  0, 0, 0,  0, 9, 0},

            {1, 7, 5,  8, 0, 0,  0, 0, 0},
            {0, 0, 0,  7, 5, 0,  3, 0, 0},
            {8, 0, 0,  0, 0, 0,  0, 0, 7}
    };

    public static int[][] getSudoku() {
        return sudoku;
    }

    public static void printBoard(int[][] board) {
        for(int i = 0;i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                System.out.print(board[i][j] + " ");
                if (j % 3 == 2)
                    System.out.print("  ");
            }
            System.out.println();
            if (i % 3 == 2)
                System.out.println();
        }
    }

    public static boolean solveSudoku(int y, int x, int[][] sudokuSolution) {
        //sets the the coordinates of the field which will be dealt with next
        int newX, newY;
        if (x == 8) {
            newX = 0;
            newY = y + 1;
        }
        else {
            newX = x + 1;
            newY = y;
        }
        // if y is equal to 9, the method successfully filled the last field of sudoku
        if (y == 9)
            return true;

        // core of the method
        if (sudokuSolution[y][x] == 0) {
            for (int i = 1; i <= 9; i++) {
                if (isNewNumberCorrect(y, x, i, sudokuSolution)) {
                    sudokuSolution[y][x] = i;
                    if (solveSudoku(newY, newX, sudokuSolution))
                        return true;
                    else {
                        sudokuSolution[y][x] = 0;
                    }
                }
            }
        }
        else
            return solveSudoku(newY, newX, sudokuSolution);

        // if the field cannot be filled with any of the numbers (0 - 9) return false
        // and possibly change the previous field
        return false;
    }

    public static boolean isNewNumberCorrect(int y, int x, int newNumber, int[][] board) {
        //checks if the newNumber is unique in its row and column
        for(int i = 0; i < 9; i++) {
            if (board[y][i] == newNumber) // x!= i not necessary I think
                return false;
            if (board[i][x] == newNumber)
                return false;
        }

        //checks if the newNumber is unique in its small sqare (3x3)
        int startX = x - (x % 3);
        int startY = y - (y % 3);
        for(int i = 0; i < 3; i++)
            for(int j = 0; j < 3; j++)
                if (board[startY + i][startX + j] == newNumber)
                    return false;

        return true;
    }
}
