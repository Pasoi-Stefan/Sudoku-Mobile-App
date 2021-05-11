package com.example.sudoku;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;

import com.google.gson.Gson;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class User {
    private static final String TAG = "User";
    private String name;
    private boolean admin = true;
    private boolean adminReset = true;
    private HashMap<String,int[][][]> sudokuGames;
    private HashMap<String,Integer> numberGamesDifficulty;
    private HashMap<String, LocalTime> bestTimes;
    private HashMap<String, LocalTime> currentTimes;
    private Integer numberGames;
    private String profilePicture;
    private SharedPreferences.Editor editor;
    private String chalengeCompleted;
    Gson g;

    public User(Activity context) {
        SharedPreferences sharedPreferences =
                context.getSharedPreferences("data", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        g = new Gson();

        Log.d(TAG, "Loading data from device...");
        this.name = sharedPreferences.getString("name", "");
        Log.d(TAG, "Name: " + this.name);

        this.chalengeCompleted = sharedPreferences.getString("ChallengeCompleted", "");

        this.numberGames = g.fromJson(sharedPreferences.getString("Number", ""), Integer.class);

        this.sudokuGames = new HashMap<>();
        this.numberGamesDifficulty = new HashMap<>();
        this.bestTimes = new HashMap<>();
        this.currentTimes = new HashMap<>();

        ArrayList<String> difficulties = new ArrayList<>(Arrays.asList("Easy", "Medium", "Hard"));
        Log.d(TAG, "ProfilePicture: " + sharedPreferences.getString("profilePicture", ""));

        this.profilePicture = sharedPreferences.getString("profilePicture", "");
        Log.d(TAG, "User: " + this.profilePicture);

        for (String difficulty : difficulties) {
            if (sharedPreferences.contains(difficulty + "Sudoku")) {
                String gameState = sharedPreferences.getString(difficulty + "Sudoku", "");
                //Log.d(TAG, "Sudoku game found for difficulty " + difficulty + ": " + gameState);
                this.sudokuGames.put(difficulty, g.fromJson(gameState, int[][][].class));
            }
            if (sharedPreferences.contains(difficulty + "Number")) {
                String number = sharedPreferences.getString(difficulty + "Number", "");
                this.numberGamesDifficulty.put(difficulty, g.fromJson(number, Integer.class));
            }
            if (sharedPreferences.contains(difficulty + "Time")) {
                String time = sharedPreferences.getString(difficulty + "Time", "");
                this.bestTimes.put(difficulty, LocalTime.parse(time));
            }
            if (sharedPreferences.contains(difficulty + "TimeCurrent")) {
                String currentTime = sharedPreferences.getString(difficulty + "TimeCurrent", "");
                this.currentTimes.put(difficulty, LocalTime.parse(currentTime));
            }

        }
        Log.d(TAG, "User: "+this.bestTimes);
        Log.d(TAG, "User: "+this.currentTimes);
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

    public void setChalengeCompleted(String completed){
        this.chalengeCompleted = completed;
        Log.d(TAG, "ChallengeCompleted: "+ completed);
        editor.putString("ChallengeCompleted", completed);
        editor.apply();
    }
    public String getChallengeCompleted(){
        return this.chalengeCompleted;
    }

    public void setProfilePicture(String picture) {
        this.profilePicture = picture;
        Log.d(TAG, "setProfilePicture: "+ picture);
        editor.putString("profilePicture", picture);
        editor.apply();
    }

    public String getProfilePicture(){
        Log.d(TAG, "getProfilePicture: " + this.profilePicture);
        return this.profilePicture;
    }

    public void initStats(){
        this.numberGames = 0;
        this.chalengeCompleted = "NotStarted";
        editor.putString("ChallengeCompleted", "NotStarted");
        editor.putString("Number",g.toJson(0));
        this.bestTimes = new HashMap<>();
        this.numberGamesDifficulty = new HashMap<>();
        this.currentTimes = new HashMap<>();
        ArrayList<String> difficulties = new ArrayList<>(Arrays.asList("Easy", "Medium", "Hard"));

        for (String difficulty : difficulties) {
            LocalTime time = LocalTime.of(0,0,0);
            bestTimes.put(difficulty,time);
            editor.putString(difficulty + "Time",String.valueOf(time));

            currentTimes.put(difficulty,time);
            editor.putString(difficulty + "TimeCurrent",String.valueOf(time));

            numberGamesDifficulty.put(difficulty,0);
            editor.putString(difficulty + "Number",g.toJson(0));
        }
        Log.d(TAG, "initStats: "+currentTimes);
        editor.apply();
    }

    public void deleteSudokuGame(String name){
        sudokuGames.remove(name);
        editor.remove(name + "Sudoku");
        LocalTime time = LocalTime.of(0,0,0);
        currentTimes.put(name,time);
        editor.putString(name + "TimeCurrent",g.toJson(time));
        editor.apply();
    }



    public void increaseNumber(String name){
        Integer currentNumber = this.numberGamesDifficulty.get(name);
        this.numberGamesDifficulty.put(name, currentNumber + 1);
        this.numberGames += 1;

        editor.putString(name + "Number", g.toJson( currentNumber + 1));
        editor.putString("Number", g.toJson(this.numberGames));
        editor.apply();
    }

    public String status(){
        Integer max = 0;
        String string = "";
        for (String key : this.numberGamesDifficulty.keySet()){
            if(this.numberGamesDifficulty.get(key) >= max){
                max = this.numberGamesDifficulty.get(key);
                string = key;
            }
        }
        if(max == 0){
            return "Beginner";
        }
        Log.d(TAG, "status: "+string);
        if(string.equals("Easy")){
            return "Beginner";
        } else if (string.equals("Medium")){
            return "Intermediate";
        } else {
            return "Advanced";
        }
    }

    public Integer getNumberDifficulty(String name){
        return this.numberGamesDifficulty.get(name);
    }

    public void setBestTime(String name, LocalTime time){
        bestTimes.put(name,time);
        Log.d(TAG, "setBestTime: "+time);
        Log.d(TAG, "setBestTime: "+g.toJson(String.valueOf(time)));
        editor.putString(name + "Time", String.valueOf(time));
        editor.apply();
    }

    public void setCurrentTime(String name, LocalTime time){
        currentTimes.put(name,time);
        Log.d(TAG, "setCurrentTime: "+g.toJson(String.valueOf(time)));
        editor.putString(name + "TimeCurrent", String.valueOf(time));
        editor.apply();
    }

    public LocalTime getTimeDifficulty(String name){
        Log.d(TAG, "getTimeDifficulty: "+this.bestTimes.get(name));
        return this.bestTimes.get(name);
    }

    public LocalTime getCurrentTimeDifficulty(String name){
        Log.d(TAG, "getCurrentTimeDifficulty "+this.currentTimes.get(name));
        return this.currentTimes.get(name);
    }

    public void setSudokuGame(String name, int[][][] Table) {
        sudokuGames.put(name,Table);

        String array = g.toJson(Table);
        editor.putString(name + "Sudoku", array);
        editor.apply();
    }

    public Integer getNumber(){
        return this.numberGames;
    }

    public boolean checkSudokuGame(String name) {
        return sudokuGames.containsKey(name);
    }

    public int[][][] getSudokuGame(String name) {
        return sudokuGames.get(name);
    }

    public void reset() {
        editor.clear();
        editor.apply();
    }
}
