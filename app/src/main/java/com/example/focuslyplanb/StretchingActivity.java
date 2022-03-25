package com.example.focuslyplanb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;


import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;


public class StretchingActivity extends AppCompatActivity {

    ImageButton startCountdownTimer;
    Button breathingExercise;

    boolean isTimerRunning;

    CountDownTimer taskCountdownTimer;

    long startTimeInput = GlobalVariable.OffTimer;
    long timeLeft = startTimeInput * 60000;
    long endTime;

    TextView OffTaskTimer;

    //GIF Slider
    SliderView sliderView;
    int[] images = {R.drawable.overhead_reach,
            R.drawable.arm_stretch,
            R.drawable.triceps_stretches,
            R.drawable.shoulder,
            R.drawable.forward,
            R.drawable.torso,
            R.drawable.hip,
            R.drawable.hamstrings,
            R.drawable.shrug,
            R.drawable.neck,
            R.drawable.upper};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stretching);

        startCountdownTimer = findViewById(R.id.start_pause);
        OffTaskTimer = (TextView) findViewById(R.id.stretch_countdown);
        breathingExercise = findViewById(R.id.breathe_button);
        breathingExercise.setVisibility(View.INVISIBLE);

        /*--TIMER DISPLAY--*/
        int minutes = (int) ((timeLeft/1000)%3600)/60;
        int seconds = (int) (timeLeft/1000)%60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OffTaskTimer.setText(timeLeftDisplay);


        /*--IMAGE SLIDER--*/
        sliderView = findViewById(R.id.gif_slider);

        SliderAdapter sliderAdapter = new SliderAdapter(images);

        sliderView.setSliderAdapter(sliderAdapter);
        sliderView.setIndicatorAnimation(IndicatorAnimationType.WORM);
        sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
        sliderView.startAutoCycle();


        // START BUTTON
        startCountdownTimer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimerRunning){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        breathingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(StretchingActivity.this, BreathingActivity.class);
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



    /*---- UPDATE COUNTDOWN TEXT DISPLAY ----*/
    public void updateCountdownText(){
//        taskCountdownTimer.cancel();
//        timeLeft = GlobalVariable.OnTimer * 60000;
        int minutes = (int) ((timeLeft/1000)%3600)/60;
        int seconds = (int) (timeLeft/1000)%60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OffTaskTimer.setText(timeLeftDisplay);
    }


    /*---- UPDATE COUNTDOWN INTERFACE ----*/
    public void updateTimeInterface() {
        int icon;
        if (isTimerRunning) {
            icon = R.drawable.pause_ver2_button;
            startCountdownTimer.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), icon));

            breathingExercise.setVisibility(View.INVISIBLE);

        } else {
            icon = R.drawable.play_ver2_button;
            startCountdownTimer.setImageDrawable(
                    ContextCompat.getDrawable(getApplicationContext(), icon));

            breathingExercise.setVisibility(View.VISIBLE);


            if (timeLeft < 1000){

                /*----DEFAULT TIME----*/
                taskCountdownTimer.cancel();
                timeLeft = GlobalVariable.OffTimer * 60000;
                int minutes = (int) ((timeLeft/1000)%3600)/60;
                int seconds = (int) (timeLeft/1000)%60;

                String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                OffTaskTimer.setText(timeLeftDisplay);

                /*---- PROCEEDS TO NEXT PAGE IF TIMER IS DONE----*/
                Intent in = new Intent(StretchingActivity.this, OffContinueActivity.class);
                startActivity(in);
            }
        }
    }


    /*---- KEEPS THE TIMER RUNNING ON BACKGROUND OR WHEN ORIENTATION CHANGES----*/
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


    /*---- DONE BUTTON IF THE USER FINISHED TASK EARLY BEFORE TIME RUNS OUT----*/
    public void doneTaskEarly1(View view){


        AlertDialog.Builder builder = new AlertDialog.Builder(StretchingActivity.this);

        builder.setCancelable(true);
        builder.setTitle("Are you sure you're done?");

        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent in = new Intent(StretchingActivity.this, OffContinueActivity.class);
                startActivity(in);

                if(isTimerRunning){
                    taskCountdownTimer.cancel();
                    timeLeft = GlobalVariable.OffTimer * 60000;
                    int minutes = (int) ((timeLeft/1000)%3600)/60;
                    int seconds = (int) (timeLeft/1000)%60;

                    String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                    OffTaskTimer.setText(timeLeftDisplay);
                }

            }
        });
        builder.show();
    }


}