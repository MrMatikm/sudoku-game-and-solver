import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
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
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10, 10, 10, 10));
        grid.setVgap(1);
        grid.setHgap(1);

        for(int i = 0; i < 9; i++) {
            for(int j = 0; j < 9; j++) {
                int x = j;
                int y = i;
                TextField field = new TextField();
                field.setMaxWidth(30);
                field.setMinWidth(30);
                field.setMinHeight(30);
                field.setMaxHeight(30);
                if (x >= 3 && x <=5 && y <= 2 || x <= 2 && y >= 3 && y <= 5 ||
                x >= 6 && y >= 3 && y <=5 || x >= 3 && x <= 5 && y >= 6)
                    field.setStyle("-fx-control-inner-background: #ECECEC;");
                field.setOnAction(e -> {
                    if (Sudoku.isNewNumberCorrect(y, x, Integer.parseInt(field.getText()), sudokuBoard)) {
                        sudokuBoard[y][x] = Integer.parseInt(field.getText());
                        if (Sudoku.solveSudoku(0, 0, getDeepCopy()))
                            field.setEditable(false);
                        else {
                            sudokuBoard[y][x] = 0;
                            field.clear();
                        }
                    }
                    else field.clear();
                });

                GridPane.setConstraints(field, j, i);
                grid.getChildren().add(field);
                fields.add(field);
            }
        }
        printSudoku();

        //grid.getChildren().get();
        Scene scene = new Scene(grid, 300, 300);

        stage.setScene(scene);
        stage.setTitle("Sudoku");
        stage.show();
    }

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

    public int[][] getDeepCopy() {
        int[][] copy = new int[9][9];
        for(int i = 0; i < 9; i++)
            for(int j = 0; j < 9; j++)
                copy[i][j] = sudokuBoard[i][j];
            return copy;
    }
}
