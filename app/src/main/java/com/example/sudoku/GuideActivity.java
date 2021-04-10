package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class GuideActivity extends AppCompatActivity {
    public static final String guideExtra = "com.example.sudoku.Guide.EXTRA_TEXT";

    private Button buttonBack;
    private Button buttonSwitch;
    public boolean switchText;
    private TextViewWithImages textGuide;
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
        textGuide = findViewById(R.id.textViewGuide);
        if(switchText) {
            textGuide.setText(R.string.app_tutorial);
            buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_next));
        } else {
            textGuide.setText(R.string.sudoku_tutorial);
            buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_back));
        }
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (origin.equals("Main")) {
                    intent = new Intent(com.example.sudoku.GuideActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
        buttonSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchText = !switchText;
                if(switchText) {
                    textGuide.setText(R.string.app_tutorial);
                    buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_next));
                } else {
                    textGuide.setText(R.string.sudoku_tutorial);
                    buttonSwitch.setBackground(getResources().getDrawable(R.drawable.ic_back));
                }
            }
        });
    }
}