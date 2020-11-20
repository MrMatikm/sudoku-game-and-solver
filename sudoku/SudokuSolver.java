package sudoku;

import javafx.scene.control.TextField;
import java.util.ArrayList;

//this class is responsible for solving sudoku without printing it on the screen; it saves the result in an array
public class SudokuSolver extends AbstractSudokuSolver {
    @Override
    protected void printNewDigit(int x, int y, int digit, ArrayList<TextField> fields) {
    }

    @Override
    protected void clearField(int x, int y, ArrayList<TextField> fields) {
    }
}
