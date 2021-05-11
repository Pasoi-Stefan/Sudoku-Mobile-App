package com.example.sudoku;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Calendar;

public class ChallengeActivity extends AppCompatActivity {

    private static final String TAG = "ChallengeActivity";
    private static final int NOTIFICATION_ID = 0;
    // Notification channel ID.
    private static final String PRIMARY_CHANNEL_ID =
            "primary_notification_channel";
    private NotificationManager mNotificationManager;

    private ToggleButton alarmToggle;
    private Button buttonChallenge;
    private Button buttonBack;
    private TextView textStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_challenge);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        alarmToggle= findViewById(R.id.alarmToggle);
        buttonChallenge = (Button) findViewById(R.id.buttonGoToChallenge);
        buttonBack = (Button) findViewById(R.id.button_back);
        textStatus = (TextView) findViewById(R.id.textChallengeStatus);
        textStatus.setText(MainActivity.user.getChallengeCompleted());
        if(MainActivity.user.getChallengeCompleted().equals("NotStarted") || MainActivity.user.getChallengeCompleted().equals("InProgress")){
            buttonChallenge.setVisibility(View.VISIBLE);
        }
        buttonChallenge.setOnClickListener(view -> {
            String level = MainActivity.user.status();
            String difficulty = "";

            if (level.equals("Beginner")){
                difficulty = "Easy";
            } else if (level.equals("Intermediate")){
                difficulty = "Medium";
            } else if (level.equals("Advanced")){
                difficulty = "Hard";
            }
            Log.d(TAG, "onCreate: " + difficulty);
            if(MainActivity.user.getChallengeCompleted().equals("NotStarted")){
                MainActivity.user.setChalengeCompleted("InProgress");

                MainActivity.user.deleteSudokuGame(difficulty);

            }
            Intent intent = new Intent(ChallengeActivity.this, SudokuActivity.class);
            intent.putExtra(SudokuActivity.sudokuExtra, difficulty);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_right, R.anim.exit_to_left);
        });

        buttonBack.setOnClickListener(v -> {
            Intent intent = new Intent(ChallengeActivity.this, StatsActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.enter_from_left, R.anim.exit_to_right);
        });

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        boolean alarmUp = (PendingIntent.getBroadcast(this, NOTIFICATION_ID,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        alarmToggle.setChecked(alarmUp);
        final PendingIntent notifyPendingIntent = PendingIntent.getBroadcast
                (this, NOTIFICATION_ID, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        if (alarmToggle.isChecked()){
            String toastMessage;
            long repeatInterval = 5000;


            long triggerTime = SystemClock.elapsedRealtime()
                    + repeatInterval;
            Log.d(TAG, "onCheckedChanged: " + triggerTime);
            if (alarmManager != null) {
                alarmManager.setRepeating
                        (AlarmManager.RTC_WAKEUP,
                                SystemClock.elapsedRealtime() + 5000, 5000,
                                notifyPendingIntent);
            }
            // Set the toast message for the "on" case.
            toastMessage = "Notification on";
        }
        alarmToggle.setOnCheckedChangeListener(
                (compoundButton, isChecked) -> {
                    String toastMessage;
                    if (isChecked) {

                        long repeatInterval = 5000;


                        long triggerTime = SystemClock.elapsedRealtime()
                                + repeatInterval;
                        Log.d(TAG, "onCheckedChanged: " + triggerTime);
                        if (alarmManager != null) {
                            alarmManager.setRepeating
                                    (AlarmManager.RTC_WAKEUP,
                                            SystemClock.elapsedRealtime() + 5000, 5000,
                                            notifyPendingIntent);
                        }
                        // Set the toast message for the "on" case.
                        toastMessage = "Notification on";

                    } else {
                        // Cancel notification if the alarm is turned off.
                        mNotificationManager.cancelAll();

                        if (alarmManager != null) {
                            alarmManager.cancel(notifyPendingIntent);
                        }
                        // Set the toast message for the "off" case.
                        toastMessage = "Notification off";

                    }


                    //Show a toast to say the alarm is turned on or off.
                    Toast.makeText(ChallengeActivity.this, toastMessage,Toast.LENGTH_SHORT)
                            .show();
                });
        createNotificationChannel();
    }

    public void createNotificationChannel() {

        // Create a notification manager object.
        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (android.os.Build.VERSION.SDK_INT >=
                android.os.Build.VERSION_CODES.O) {

            // Create the NotificationChannel with all the parameters.
            NotificationChannel notificationChannel = new NotificationChannel
                    (PRIMARY_CHANNEL_ID,
                            "Stand up notification",
                            NotificationManager.IMPORTANCE_HIGH);

            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            notificationChannel.setDescription
                    ("Notifies every 15 minutes to stand up and walk");
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
    }
}