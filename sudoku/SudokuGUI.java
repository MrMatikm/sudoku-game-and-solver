package sudoku;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import java.util.ArrayList;

public class SudokuGUI extends Application {
    private ArrayList<TextField> fields = new ArrayList<>();
    private AbstractSudokuSolver solver = new SudokuSolver();
    private final int[][] sudokuBoard = {
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

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Scene scene = new Scene(createLayout(), 300, 330);
        printSudoku();
        stage.setScene(scene);
        stage.setTitle("Sudoku");
        stage.show();
    }

    private GridPane createLayout() {
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(1);
        grid.setHgap(1);

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                int x = j;
                int y = i;
                TextField field = new TextField();
                field.setMinSize(30, 30);
                field.setMaxSize(30, 30);
                field.setAlignment(Pos.CENTER);
                if (x >= 3 && x <=5 && y <= 2 || x <= 2 && y >= 3 && y <= 5 ||
                        x >= 6 && y >= 3 && y <=5 || x >= 3 && x <= 5 && y >= 6)
                    field.setStyle("-fx-control-inner-background: #ECECEC;");
                field.setOnAction(e -> getActionForField(x, y, field));
                GridPane.setConstraints(field, j, i);
                grid.getChildren().add(field);
                fields.add(field);
            }
        }
        Button button = new Button("Solve!");
        button.setMinSize(92, 30);
        button.setMaxSize(92, 30);
        button.setOnAction(e -> {
            SolvingThread thread = new SolvingThread();
            thread.start();
        });
        GridPane.setConstraints(button, 3, 9, 3, 1);
        grid.getChildren().add(button);
        return grid;
    }

    //checks if the entered number is a correct solution; if so it is accepted and the field cannot be edited any more
    private void getActionForField(int x, int y, TextField field) {
        try {
            int number = Integer.parseInt(field.getText());
            if (number >= 1 && number <= 9 && solver.isNewNumberCorrect(y, x, number, sudokuBoard)) {
                sudokuBoard[y][x] = number;
                if (solver.solveSudoku(0, 0, getDeepCopy(), fields))
                    field.setEditable(false);
                else {
                    sudokuBoard[y][x] = 0;
                    field.clear();
                }
            } else field.clear();
        } catch (NumberFormatException exception) {
            field.clear();
        }
    }

    //prints the sudoku from the array on the screen
    private void printSudoku() {
        int count = 0;
        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                if (sudokuBoard[i][j] != 0) {
                    fields.get(count).setText(Integer.toString(sudokuBoard[i][j]));
                    fields.get(count).setEditable(false);
                }
                count++;
            }
        }
    }

    //makes a deep copy of the sudoku board
    private int[][] getDeepCopy() {
        int[][] copy = new int[9][9];
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                copy[i][j] = sudokuBoard[i][j];
        return copy;
    }

    // a thread responsible for solving the sudoku and printing it on the screen
    private class SolvingThread extends Thread {
        @Override
        public void run() {
            solver = new SudokuSolverPrinter();
            solver.solveSudoku(0, 0, sudokuBoard, fields);
        }
    }
}
