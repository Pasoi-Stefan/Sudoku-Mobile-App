package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.BlendMode;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.time.LocalTime;


public class SudokuActivity extends AppCompatActivity {

    private static final String TAG = "SudokuActivity";
    public static final String sudokuExtra = "com.example.sudoku.Sudoku.EXTRA_TEXT";


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

    private TextView timer;
    private boolean isPaused = false;
    private int gameSeconds;
    private int gameMinutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        Intent intent = getIntent();
        isPaused = false;
        Log.d(TAG, "onCreate: asd");
        difficultyString = intent.getStringExtra(sudokuExtra);
        Log.d(TAG, "onCreate: "+difficultyString);
        init();
    }

    private void showTime(int minutes,int seconds) {
        String sec = "";
        if(seconds > 9) {
            sec = String.valueOf(seconds);
        }
        else {
            sec = "0" + seconds;
        }
        timer.setText(minutes + ":" + sec);
    }

    private void mainTimer() {
        new android.os.Handler().postDelayed(
                () -> {
                    if (!isPaused) {
                        gameSeconds++;
                        if (gameSeconds == 60) {
                            gameMinutes++;
                            gameSeconds = 0;
                        }
                        showTime(gameMinutes, gameSeconds);
                        mainTimer();
                    }
                }, 1000);
    }

    private void saveTime() {
        LocalTime time = LocalTime.of(0,gameMinutes,gameSeconds);
        Log.d(TAG, "saveTime: "+time);
        MainActivity.user.setCurrentTime(difficultyString,time);
    }

    private void getTime(){
        LocalTime time = MainActivity.user.getCurrentTimeDifficulty(difficultyString);
        Log.d(TAG, "getTime: "+time);
        gameMinutes = time.getMinute();
        gameSeconds = time.getSecond();
    }

    private void updateBest(){
        LocalTime zeroTime = LocalTime.of(0,0,0);
        LocalTime currentTime = LocalTime.of(0,gameMinutes,gameSeconds);
        LocalTime bestTime = MainActivity.user.getTimeDifficulty(difficultyString);

        if(bestTime.compareTo(zeroTime) == 0 || bestTime.compareTo(currentTime) > 0){
            MainActivity.user.setBestTime(difficultyString,currentTime);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        saveTime();
        isPaused = true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        isPaused = true;
        saveTime();
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveTime();
        Log.d(TAG, "onPause: "+MainActivity.user.getCurrentTimeDifficulty(difficultyString));
        Log.d(TAG, "onPause: "+"paused");
        isPaused = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume: "+"unpaused");
        isPaused = false;
    }

    private void init() {
        buttonComplete = (Button) findViewById(R.id.buttonComplete);
        buttonEmpty = (Button) findViewById(R.id.buttonEmpty);
        buttonBack = (Button) findViewById(R.id.buttonBack);
        buttonHelp = (Button) findViewById(R.id.buttonHelp);
        timer = (TextView) findViewById(R.id.Timer);
        getTime();
        buttonHelp.setOnClickListener(v -> {
            MainActivity.user.setSudokuGame(difficultyString, sudoku.getCurrentState());
            Intent intent = new Intent(SudokuActivity.this, GuideActivity.class);
            intent.putExtra(GuideActivity.guideExtra, difficultyString);
            startActivity(intent);
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
                    if(tableUnsolved[i][j]==0)
                        buttonsSudoku[i][j].setTextColor(getApplication().getResources().getColor(R.color.blue));

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

                    for(int l = 0; l < 9; l++)
                        for(int c = 0; c < 9; c++)
                            buttonsSudoku[l][c].setBackgroundTintList(null);

                    int highlightColor = getColor(R.color.BackgroundColorSudoku);

                    for (int k = 0; k < 9; k++){
                        buttonsSudoku[finalI][k].setBackgroundTintList(ColorStateList.valueOf(highlightColor));
                        buttonsSudoku[k][finalJ].setBackgroundTintList(ColorStateList.valueOf(highlightColor));
                    }

                    for (int l = (finalI / 3) * 3; l < (finalI / 3) * 3 + 3; l++)
                        for (int c = (finalJ / 3) * 3; c < (finalJ / 3) * 3 + 3; c++)
                            buttonsSudoku[l][c].setBackgroundTintList(ColorStateList.valueOf(highlightColor));

                    int selectColor = getColor(R.color.SelectColor);
                    buttonsSudoku[finalI][finalJ].setBackgroundTintList(ColorStateList.valueOf(selectColor));
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
                        buttonsSudoku[xCurrent][yCurrent].setTextColor(getApplication().getResources().getColor(R.color.blue));
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
            Intent intent = new Intent(SudokuActivity.this, StatsActivity.class);
            //Log.d(TAG, "Back: "+difficultyString);
            //saveTime();
            intent.putExtra(StatsActivity.statsExtra, difficultyString);
            startActivity(intent);
            //Navigation.findNavController(SudokuActivity.this,null).navigate(R.id.action_SudokuActivity_to_EasyStatsFragment);
        });

        buttonComplete.setOnClickListener(v -> {
            if(sudoku.checkIfSolved() || MainActivity.user.isAdmin()){
                MainActivity.user.deleteSudokuGame(difficultyString);
                MainActivity.user.increaseNumber(difficultyString);
                updateBest();
                buttonBack.performClick();
            } else {
                Toast.makeText(SudokuActivity.this, "Incorrect solution", Toast.LENGTH_SHORT).show();
            }
        });
        mainTimer();
    }
}
