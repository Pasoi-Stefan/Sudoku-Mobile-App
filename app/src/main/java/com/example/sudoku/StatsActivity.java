package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.NavGraph;
import androidx.navigation.NavInflater;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class StatsActivity extends AppCompatActivity {
    private static final String TAG = "StatsActivity";
    public static final String statsExtra = "com.example.sudoku.Stats.EXTRA_TEXT";

    private String difficultyString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        Intent intent = getIntent();
        Log.d(TAG, "onCreate: asd");
        difficultyString = intent.getStringExtra(statsExtra);
        Log.d(TAG, "onCreate: "+difficultyString);
        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
        NavController navController = navHost.getNavController();
        if(difficultyString != null) {
            if(difficultyString.equals("Easy")){
                navController.navigate(R.id.action_OptionsFragment_to_EasyStatsFragment);
            } else if(difficultyString.equals("Medium")){
                navController.navigate(R.id.action_OptionsFragment_to_MediumStatsFragment);
            } else {
                navController.navigate(R.id.action_OptionsFragment_to_HardStatsFragment);
            }
        }
    }
}