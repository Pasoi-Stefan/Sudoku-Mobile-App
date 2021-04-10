package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class SudokuActivity extends AppCompatActivity {

    private static final String TAG = "SudokuActivity";

    private Sudoku sudoku;
    private int[][] table;
    private int[][] tableUnsolved;
    private int[][] tableSolved;
    private Button buttonComplete;
    private String difficultyString;
    private Button[][] buttonsSudoku = new Button[9][9];
    private Button[] buttonsDigit = new Button[9];
    private Button buttonEmpty;
    private Button buttonBack;
    private Button buttonHelp;

    private int xCurrent = 1;
    private int yCurrent = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        Intent intent = getIntent();
        difficultyString = intent.getStringExtra(MainActivity.mainExtra);
        Log.d(TAG, "onCreate: "+difficultyString);
        init();
    }

    private void init() {
        buttonComplete = (Button) findViewById(R.id.buttonComplete);
        buttonEmpty = (Button) findViewById(R.id.buttonEmpty);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonHelp = (Button) findViewById(R.id.buttonHelp);
        buttonHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.user.setSudokuGame(difficultyString, sudoku.getCurrentState());
                Intent intent = new Intent(SudokuActivity.this, GuideActivity.class);
                intent.putExtra(GuideActivity.guideExtra, difficultyString);
                startActivity(intent);
            }
        });
        if(MainActivity.user.checkSudokuGame(difficultyString)){
            sudoku = new Sudoku(MainActivity.user.getSudokuGame(difficultyString));
            Log.d(TAG, "init: was created");
        }
        else {
            Log.d(TAG, "init: was not created");
            int difficulty = 0;

            if (difficultyString.equals("Easy")) {
               difficulty = 20;
            }
            else if (difficultyString.equals("Medium")) {
                difficulty = 30;
            }
            else if (difficultyString.equals("Hard")) {
                difficulty = 40;
            }

            sudoku = new Sudoku(difficulty);
            MainActivity.user.setSudokuGame(difficultyString, sudoku.getCurrentState());
        }

        table = sudoku.getTable();
        tableSolved = sudoku.getTableSolved();
        tableUnsolved = sudoku.getTableUnsolved();

        for (int i = 0;i<9;i++){
            String string = "";
            for(int j=0;j<9;j++){
                string+=table[i][j] + " ";
            }
            //Log.d(TAG, "init: "+string);
        }

        for(int i = 0; i < 9; i++){
            for(int j = 0; j < 9; j++){
                String stringID = "buttonSudoku" + (i + 1) + (j + 1);
                int ID = SudokuActivity.this.getResources().getIdentifier(stringID, "id", SudokuActivity.this.getPackageName());
                buttonsSudoku[i][j] = (Button) findViewById(ID);
                if(table[i][j]!=0) {
                    String string = String.valueOf(table[i][j]);
                    SpannableStringBuilder builder = new SpannableStringBuilder(string);
                    builder.setSpan(new StyleSpan(Typeface.BOLD), 0, string.length(), 0);
                    //Log.d(TAG, "init: "+builder);
                    buttonsSudoku[i][j].setText(string);

                } else {
                    buttonsSudoku[i][j].setText("");
                }
                final int finalI = i;
                final int finalJ = j;
                buttonsSudoku[i][j].setOnClickListener(v -> {
                    xCurrent= finalI;
                    yCurrent= finalJ;
                });
            }
        }

        for(int i = 0; i < 9; i++) {
            String string = "buttonDigit" + (i + 1);
            int ID = SudokuActivity.this.getResources().getIdentifier(string, "id", SudokuActivity.this.getPackageName());
            buttonsDigit[i] = (Button) findViewById(ID);
            final int finalI = i;
            buttonsDigit[i].setOnClickListener(v -> {
                if (sudoku.isChangeable(xCurrent,yCurrent)) {
                    int digit = Integer.parseInt(buttonsDigit[finalI].getText().toString());
                    buttonsSudoku[xCurrent][yCurrent].setText(String.valueOf(digit));
                    if (sudoku.checkElement(xCurrent, yCurrent, digit)) {
                        Log.d(TAG, "onClick: 1");
                        table[xCurrent][yCurrent] = digit;
                        sudoku.setElement(xCurrent, yCurrent, digit);
                        MainActivity.user.setSudokuGame(difficultyString, sudoku.getCurrentState());
                        buttonsSudoku[xCurrent][yCurrent].setTextColor(getApplication().getResources().getColor(R.color.black));
                        if (sudoku.checkIfSolved()) {
                            buttonComplete.performClick();
                        }
                    } else {
                        buttonsSudoku[xCurrent][yCurrent].setTextColor(getApplication().getResources().getColor(R.color.red));
                    }
                }
            });
        }

        buttonEmpty.setOnClickListener(v -> {
            if (sudoku.isChangeable(xCurrent,yCurrent)) {
                buttonsSudoku[xCurrent][yCurrent].setText("");
                table[xCurrent][yCurrent] = 0;
                sudoku.setElement(xCurrent, yCurrent, 0);
                MainActivity.user.setSudokuGame(difficultyString, sudoku.getCurrentState());
            }
        });

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(SudokuActivity.this, MainActivity.class);
            startActivity(intent);
        });

        buttonComplete.setOnClickListener(v -> {
            if(sudoku.checkIfSolved() || MainActivity.user.isAdmin()){
                buttonBack.performClick();
            } else {
                Toast.makeText(SudokuActivity.this, "Incorrect solution", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
