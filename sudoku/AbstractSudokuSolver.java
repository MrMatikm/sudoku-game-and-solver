package sudoku;

import javafx.scene.control.TextField;

import java.util.ArrayList;

public abstract class AbstractSudokuSolver {
    public final boolean solveSudoku(int y, int x, int[][] sudokuBoard, ArrayList<TextField> fields) {
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
        if (sudokuBoard[y][x] == 0) {
            for (int i = 1; i <= 9; i++) {
                if (isNewNumberCorrect(y, x, i, sudokuBoard)) {
                    sudokuBoard[y][x] = i;
                    //prints new digit in an appropriate field
                    printNewDigit(x, y, i, fields);
                    if (solveSudoku(newY, newX, sudokuBoard, fields))
                        return true;
                    else {
                        sudokuBoard[y][x] = 0;
                        //clears the appropriate field
                        clearField(x, y, fields);
                    }
                }
            }
        }
        else
            return solveSudoku(newY, newX, sudokuBoard, fields);

        // if the field cannot be filled with any of the numbers (0 - 9) return false
        // and possibly change the previous field
        return false;
    }

    public final boolean isNewNumberCorrect(int y, int x, int newNumber, int[][] board) {
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

    protected abstract void printNewDigit(int x, int y, int digit, ArrayList<TextField> fields);

    protected abstract void clearField(int x, int y, ArrayList<TextField> fields);

}
