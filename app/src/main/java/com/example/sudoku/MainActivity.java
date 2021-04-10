package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String mainExtra = "com.example.Sudoku.EXTRA_TEXT";

    //Model
    public static User user;

    //widgets
    private Button btnMap;
    private TextView textName;
    private Button btnReset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: asd");
        init();
    }

    private void init() {
        user = new User(this);
        btnMap = (Button) findViewById(R.id.btnMap);
        textName = (TextView) findViewById(R.id.textName);
        textName.setText(user.getName());
        btnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = textName.getText().toString();
                user.setName(name);
                Log.d(TAG, "onClick: " + user.getName());
            }
        });

        Button btnGuide = (Button) findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.sudoku.MainActivity.this, com.example.sudoku.GuideActivity.class);
                intent.putExtra(com.example.sudoku.GuideActivity.guideExtra, "Main");
                startActivity(intent);
            }
        });

        Button btnCredits = (Button) findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.sudoku.MainActivity.this, CreditsActivity.class);
                startActivity(intent);
            }
        });

        btnReset = (Button) findViewById(R.id.btnReset);
        if(user.isAdminReset()) {
            btnReset.setVisibility(View.VISIBLE);
        }
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.reset();
            }
        });
    }

}
