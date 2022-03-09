package com.example.focuslyplanb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Locale;

public class OnTaskActivity extends AppCompatActivity {

//    Button startCountdownTimer;

    ImageButton startCountdownTimer;

    Button breathingExercise;

    boolean isTimerRunning;

    CountDownTimer taskCountdownTimer;

    long startTimeInput = GlobalVariable.OnTimer;
    long timeLeft = startTimeInput * 60000;
    long endTime;

    TextView taskNameDisplay, OnTaskTimer, SensorDisplay;

//    bluetoothControl nBT = new bluetoothControl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_on_task);


//        startCountdownTimer = findViewById(R.id.button_start_pause);
        startCountdownTimer = findViewById(R.id.start_pause);
        breathingExercise = findViewById(R.id.breathe_button);
        breathingExercise.setVisibility(View.INVISIBLE);

        taskNameDisplay = (TextView) findViewById(R.id.task_display);
        taskNameDisplay.setText(GlobalVariable.taskname);

        OnTaskTimer = (TextView) findViewById(R.id.on_countdown);



        int minutes = (int) ((timeLeft / 1000) % 3600) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OnTaskTimer.setText(timeLeftDisplay);


        // START BUTTON
//        startCountdownTimer.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (isTimerRunning) {
//                    pauseTimer();
//                } else {
//                    startTimer();
//                }
//            }
//        });

        startCountdownTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTimerRunning) {
                    pauseTimer();

                } else {
                    startTimer();

                }
            }
        });

        breathingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(OnTaskActivity.this, BreathingActivity.class);
                startActivity(intent);


            }
        });

    }



    /*---- COUNTDOWN | START PAUSE BUTTON ----*/
    public void startTimer(){

        endTime = System.currentTimeMillis() + timeLeft;

        taskCountdownTimer = new CountDownTimer(timeLeft, 1000) {
            @Override
            public void onTick(long timeUntilFinished) {
                timeLeft = timeUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                isTimerRunning = false;
                updateTimeInterface();

            }
        }.start();

        isTimerRunning = true;
        updateTimeInterface();
    }


    /*---- PAUSE COUNTDOWN TIMER ----*/
    public void pauseTimer(){

        taskCountdownTimer.cancel();
        isTimerRunning = false;
        updateTimeInterface();

    }

    /*---- RESET COUNTDOWN TIMER ----*/
    public void resetTimer(){

        taskCountdownTimer.cancel();
        timeLeft = GlobalVariable.OnTimer * 60000;
        int minutes = (int) ((timeLeft/1000)%3600)/60;
        int seconds = (int) (timeLeft/1000)%60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OnTaskTimer.setText(timeLeftDisplay);

        updateTimeInterface();

    }

    /*---- UPDATE COUNTDOWN TEXT DISPLAY ----*/
    public void updateCountdownText(){
//        timeLeft = GlobalVariable.OnTimer * 60000;
        int minutes = (int) ((timeLeft/1000)%3600)/60;
        int seconds = (int) (timeLeft/1000)%60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OnTaskTimer.setText(timeLeftDisplay);
    }

    /*---- UPDATE COUNTDOWN INTERFACE ----*/
    public void updateTimeInterface() {
        int icon;
        if (isTimerRunning) {
            icon = R.drawable.pausebutton;
            startCountdownTimer.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), icon));
//            startCountdownTimer.setText("PAUSE");

            breathingExercise.setVisibility(View.INVISIBLE);
        } else {
            icon = R.drawable.playbutton;
            startCountdownTimer.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), icon));
//            startCountdownTimer.setText("START");
            breathingExercise.setVisibility(View.VISIBLE);

            if (timeLeft < 1000) {

                /*----DEFAULT TIME----*/
                timeLeft = startTimeInput;
                updateCountdownText();

                /*---- PROCEEDS TO NEXT PAGE IF TIMER IS DONE----*/
                Intent in = new Intent(OnTaskActivity.this, ContinueActivity.class);
                startActivity(in);
            }
        }
    }

    /*---- DEFAULT VIEW & KEEPS THE TIMER RUNNING ON BACKGROUND ----*/

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("millisLeft", timeLeft);
        outState.putBoolean("timerRunning", isTimerRunning);
        outState.putLong("timeEnd", endTime);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        timeLeft = savedInstanceState.getLong("millisLeft");
        isTimerRunning = savedInstanceState.getBoolean("timerRunning");

        updateCountdownText();
        updateTimeInterface();

        if(isTimerRunning){
            endTime = savedInstanceState.getLong("timeEnd");
            timeLeft = endTime - System.currentTimeMillis();
            startTimer();
        }
    }


    /*---- DONE BUTTON | IF THE USER FINISHED TASK EARLY BEFORE TIME RUNS OUT----*/

    public void doneTaskEarly1(View view){

            AlertDialog.Builder builder = new AlertDialog.Builder(OnTaskActivity.this);

            builder.setCancelable(true);
            builder.setTitle("Are you sure you're done?");
//                builder.setMessage("Click 'OK' if you would like to continue your task");

            builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.cancel();
                }
            });

            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    Intent in = new Intent(OnTaskActivity.this, DoneOnTaskActivity.class);
                    startActivity(in);

                    if(isTimerRunning){
                        timeLeft = startTimeInput;
                        taskCountdownTimer.cancel();
                        updateCountdownText();
                    }

//                    taskCountdownTimer.cancel();
//                    timeLeft = GlobalVariable.OnTimer * 60000;
//                    int minutes = (int) ((timeLeft/1000)%3600)/60;
//                    int seconds = (int) (timeLeft/1000)%60;
//
//                    String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
//                    OnTaskTimer.setText(timeLeftDisplay);
//
//                    updateTimeInterface();

                }
            });
            builder.show();
    }


}