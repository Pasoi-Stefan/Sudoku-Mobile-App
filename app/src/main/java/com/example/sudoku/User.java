package com.example.sudoku;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class User {
    private static final String TAG = "User";
    private String name;
    private boolean admin = true;
    private boolean adminReset = true;
    private HashMap<String,int[][][]> sudokuGames;
    private SharedPreferences.Editor editor;
    Gson g;

    public User(Activity context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        g = new Gson();

        Log.d(TAG, "Loading data from device...");
        this.name = sharedPreferences.getString("name", "");
        Log.d(TAG, "Name: " + this.name);

        this.sudokuGames = new HashMap<>();

        ArrayList<String> difficulties = new ArrayList<>(Arrays.asList("Easy", "Medium", "Hard"));

        for (String difficulty : difficulties) {
            if (sharedPreferences.contains(difficulty + "Sudoku")) {
                String gameState = sharedPreferences.getString(difficulty + "Sudoku", "");
                Log.d(TAG, "Sudoku game found for difficulty " + difficulty + ": " + gameState);
                this.sudokuGames.put(difficulty, g.fromJson(gameState, int[][][].class));
            }
        }

    }

    public boolean isAdminReset() {
        return adminReset;
    }

    public boolean isAdmin() {
        return admin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        Log.d(TAG, "setName: " + name);
        editor.putString("name", name);
        editor.apply();
    }

    public void setSudokuGame(String name, int[][][] Table) {
        sudokuGames.put(name,Table);

        String array = g.toJson(Table);
        Log.d(TAG, "setSudokuGame for region " + name + ": " + array);
        editor.putString(name + "Sudoku", array);
        editor.apply();
    }

    public boolean checkSudokuGame(String name) {
        return sudokuGames.containsKey(name);
    }

    public int[][][] getSudokuGame(String name) {
        return sudokuGames.get(name);
    }

    public void reset() {
        editor.clear();
    }
}
