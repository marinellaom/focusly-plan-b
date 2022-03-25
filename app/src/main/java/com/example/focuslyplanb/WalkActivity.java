package com.example.focuslyplanb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Locale;

public class WalkActivity extends AppCompatActivity {

    ImageButton startCountdownTimer;
    Button breathingExercise;
    TextView OffTaskTimer;
    CountDownTimer taskCountdownTimer;

    boolean isTimerRunning;

    long startTimeInput = GlobalVariable.OffTimer;
    long timeLeft = startTimeInput * 60000;
    long endTime;


    private static final long IMAGE_UPDATE_DELAY_MILLIS = 8000;

    private static final int[] ALL_DRAWABLE_RES = new int[] {
            R.drawable.mwalk1,
            R.drawable.mwalk2,
            R.drawable.mwalk3,
            R.drawable.mwalk4,
            R.drawable.mwalk5,
            R.drawable.twalk1,
            R.drawable.twalk2,
            R.drawable.twalk3,
            R.drawable.twalk4,
            R.drawable.twalk5,
            R.drawable.bwalk1,
            R.drawable.bwalk2,
            R.drawable.bwalk3,
            R.drawable.bwalk4,
            R.drawable.bwalk5
    };

    private int currentDrawableResIndex;
    private ImageView imageView;
    private Runnable updateImageTask;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walk);

        startCountdownTimer = findViewById(R.id.start_pause);
        OffTaskTimer = (TextView) findViewById(R.id.walk_countdown);
        breathingExercise = findViewById(R.id.breathe_button);
        breathingExercise.setVisibility(View.INVISIBLE);

        /*--TIMER DISPLAY--*/
        int minutes = (int) ((timeLeft/1000)%3600)/60;
        int seconds = (int) (timeLeft/1000)%60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OffTaskTimer.setText(timeLeftDisplay);


        /*--MULTIPLE IMAGE DISPLAY--*/
        updateImageTask = new UpdateImageTask();

        imageView = (ImageView) findViewById(R.id.image_display);

        currentDrawableResIndex = 0;
        imageView.setImageResource(ALL_DRAWABLE_RES[0]);
        imageView.removeCallbacks(updateImageTask);
        imageView.postDelayed(updateImageTask, IMAGE_UPDATE_DELAY_MILLIS);



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

                Intent intent = new Intent(WalkActivity.this, BreathingActivity.class);
                startActivity(intent);


            }
        });

    }


    //FOR IMAGES
    private class UpdateImageTask implements Runnable {
        @Override
        public void run() {
            currentDrawableResIndex++;

            if (currentDrawableResIndex < ALL_DRAWABLE_RES.length) {
                imageView.setImageResource(ALL_DRAWABLE_RES[currentDrawableResIndex]);
                imageView.postDelayed(this, IMAGE_UPDATE_DELAY_MILLIS);
            } else {
                imageView.setImageResource(R.drawable.mwalk1);
            }
        }
    }

    //FOR IMAGES
    @Override
    protected void onStop() {
        super.onStop();
        imageView.setImageResource(R.drawable.mwalk1);
        imageView.removeCallbacks(updateImageTask);
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
//                timeLeft = startTimeInput;
//                updateCountdownText();
                taskCountdownTimer.cancel();
                timeLeft = GlobalVariable.OffTimer * 60000;
                int minutes = (int) ((timeLeft/1000)%3600)/60;
                int seconds = (int) (timeLeft/1000)%60;

                String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                OffTaskTimer.setText(timeLeftDisplay);

                /*---- PROCEEDS TO NEXT PAGE IF TIMER IS DONE----*/
                Intent in = new Intent(WalkActivity.this, OffContinueActivity.class);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(WalkActivity.this);

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
                Intent in = new Intent(WalkActivity.this, OffContinueActivity.class);
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