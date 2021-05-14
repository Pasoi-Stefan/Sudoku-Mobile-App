package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {
    public static final String guideExtra = "com.example.sudoku.Guide.EXTRA_TEXT";
    private static final String TAG = "GuideActivity";

    private Button buttonBack;
    private Button buttonSwitch;
    public boolean switchText;
    private TextView textGuide;
    private String origin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        Intent intent = getIntent();
        origin = intent.getStringExtra(com.example.sudoku.GuideActivity.guideExtra);
        if(origin.equals("Main"))
            switchText = true;
        else
            switchText = false;


        buttonBack = (Button) findViewById(R.id.backButton);
        buttonSwitch = (Button) findViewById(R.id.buttonSwitch);
        textGuide = (TextView) findViewById(R.id.textViewGuide);
        if(switchText) {
            textGuide.setText(R.string.app_tutorial);
            buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_next));
        } else {
            textGuide.setText(R.string.sudoku_tutorial);
            buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_back));
        }
        buttonBack.setOnClickListener(v -> {
            Intent intent1;
            Log.d(TAG, "onCreate: "+origin);
            if (origin.equals("Main")) {
                intent1 = new Intent(GuideActivity.this, MainActivity.class);
                startActivity(intent1);
                overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            } else {
                    intent1 = new Intent(GuideActivity.this, SudokuActivity.class);
                    intent1.putExtra(SudokuActivity.sudokuExtra, origin);
                    startActivity(intent1);
                    overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
            }

        });
        buttonSwitch.setOnClickListener(v -> {
            switchText = !switchText;
            if(switchText) {
                textGuide.setText(R.string.app_tutorial);
                buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_next));
            } else {
                textGuide.setText(R.string.sudoku_tutorial);
                buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_back));
            }
        });
    }
}