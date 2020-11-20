package sudoku;

import javafx.application.Platform;
import javafx.scene.control.TextField;
import java.util.ArrayList;

//this class is responsible for solving sudoku and printing the result on the screen, showing how the algorithm works
public class SudokuSolverPrinter extends AbstractSudokuSolver {
    @Override
    protected void printNewDigit(int x, int y, int digit, ArrayList<TextField> fields) {
        Platform.runLater(() -> {
            getField(x, y, fields).setText(Integer.toString(digit));
            getField(x, y, fields).setEditable(false);
        });
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void clearField(int x, int y, ArrayList<TextField> fields) {
        Platform.runLater(() -> {
            getField(x, y, fields).clear();
            getField(x, y, fields).setEditable(true);
        });
        try {
            Thread.sleep(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //return a field with the given coordinates
    private TextField getField(int x, int y, ArrayList<TextField> fields) {
        for(int i = 0; i < 81; i++) {
            if (i % 9 == x && i / 9 == y)
                return fields.get(i);
        }
        return null;
    }
}
