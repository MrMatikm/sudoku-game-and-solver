import javafx.application.Application;
import javafx.application.Platform;
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
    private int[][] sudokuBoard = Sudoku.getSudoku();

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

    public GridPane createLayout() {
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
    public void getActionForField(int x, int y, TextField field) {
        try {
            int number = Integer.parseInt(field.getText());
            if (number >= 1 && number <= 9 && Sudoku.isNewNumberCorrect(y, x, number, sudokuBoard)) {
                sudokuBoard[y][x] = number;
                if (Sudoku.solveSudoku(0, 0, getDeepCopy()))
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

    //return a field with the given coordinates
    public TextField getField(int x, int y) {
        for(int i = 0; i < 81; i++) {
            if (i % 9 == x && i / 9 == y)
                return fields.get(i);
        }
        return null;
    }

    //prints the sudoku from the array on the screen
    public void printSudoku() {
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
    public int[][] getDeepCopy() {
        int[][] copy = new int[9][9];
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                copy[i][j] = sudokuBoard[i][j];
        return copy;
    }

    //solves the sudoku and prints the result on the screen step by step showing how backtracking works
    public boolean solveSudokuAndPrint(int y, int x, int[][] sudokuSolution) {
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
                if (Sudoku.isNewNumberCorrect(y, x, i, sudokuSolution)) {
                    sudokuSolution[y][x] = i;
                    Platform.runLater(() -> {
                        getField(x, y).setText(Integer.toString(sudokuSolution[y][x]));
                        getField(x, y).setEditable(false);
                    });
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if (solveSudokuAndPrint(newY, newX, sudokuSolution))
                        return true;
                    else {
                        sudokuSolution[y][x] = 0;
                        Platform.runLater(() -> {
                            getField(x, y).clear();
                            getField(x, y).setEditable(true);
                        });
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        else
            return solveSudokuAndPrint(newY, newX, sudokuSolution);

        // if the field cannot be filled with any of the numbers (0 - 9) return false
        // and possibly change the previous field
        return false;
    }

    // a thread responsible for solving the sudoku and printing it on the screen
    public class SolvingThread extends Thread {
        @Override
        public void run() {
            solveSudokuAndPrint(0, 0, sudokuBoard);
        }
    }
}
