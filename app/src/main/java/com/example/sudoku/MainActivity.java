package com.example.sudoku;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    public static final String mainExtra = "com.example.Sudoku.EXTRA_TEXT";

    //Model
    public static User user;

    //widgets
    private Button btnPlay;
    private TextView textEnterName;
    private TextView textWaring;
    private Button btnReset;
    private Button btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: asd");
        init();
    }

    private void init() {
        user = new User(this);
        btnPlay = (Button) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(v -> {
            if(user.getName().equals("")) {
                LayoutInflater inflater = (LayoutInflater)
                        getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_name, null);

                // create the popup window
                int width = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                int height = ConstraintLayout.LayoutParams.WRAP_CONTENT;
                boolean focusable = true; // lets taps outside the popup also dismiss it
                final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);
                popupWindow.showAtLocation(v, Gravity.CENTER, 0, 0);
                btnDone = (Button) popupView.findViewById(R.id.buttonDone);
                textEnterName = (TextView) popupView.findViewById(R.id.textEnterName);
                textWaring = (TextView) popupView.findViewById(R.id.textWarning);
                btnDone.setOnClickListener(view -> {
                    String userName = textEnterName.getText().toString();
                    Log.d(TAG, "init: " + userName);
                    if (userName.equals("")) {
                        textWaring.setVisibility(View.VISIBLE);
                    } else {
                        user.initStats();
                        user.setName(userName);
                        Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                        Log.d(TAG, "onClick: " + user.getName());
                        startActivity(intent);
                    }
                });
            } else {
                Intent intent = new Intent(MainActivity.this, StatsActivity.class);
                Log.d(TAG, "onClick: " + user.getName());
                startActivity(intent);
            }
        });

        Button btnGuide = (Button) findViewById(R.id.btnGuide);
        btnGuide.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, GuideActivity.class);
            intent.putExtra(GuideActivity.guideExtra, "Main");
            startActivity(intent);
        });

        Button btnCredits = (Button) findViewById(R.id.btnCredits);
        btnCredits.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, CreditsActivity.class);
            startActivity(intent);
        });

        btnReset = (Button) findViewById(R.id.btnReset);
        if(user.isAdminReset()) {
            btnReset.setVisibility(View.VISIBLE);
        }
        btnReset.setOnClickListener(view -> {
            Log.d(TAG, "init: " + "reset");
            user.reset();
            user = new User(this);
        });
    }

}
