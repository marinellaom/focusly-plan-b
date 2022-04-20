package com.example.focuslyplanb;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
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

public class MusicActivity extends AppCompatActivity {

    ImageButton startCountdownTimer;
    Button breathingExercise;

    boolean isTimerRunning;

    CountDownTimer taskCountdownTimer;

    long startTimeInput = GlobalVariable.OffTimer;
    long timeLeft = startTimeInput * 60000;
    long endTime;

    TextView OffTaskTimer;

    // SOUNDS
    private ImageView natureSounds;
    private ImageView pianoSounds;
    private ImageView spotify;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);

        startCountdownTimer = findViewById(R.id.start_pause);
        breathingExercise = findViewById(R.id.breathe_button);
        breathingExercise.setVisibility(View.INVISIBLE);

        OffTaskTimer = (TextView) findViewById(R.id.music_countdown);

        int minutes = (int) ((timeLeft/1000)%3600)/60;
        int seconds = (int) (timeLeft/1000)%60;

        String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        OffTaskTimer.setText(timeLeftDisplay);


        //EDIT: MUSIC BUTTONS
        natureSounds = (ImageView) findViewById(R.id.rain_button);
        pianoSounds = (ImageView) findViewById(R.id.piano_button);
        final MediaPlayer mediaPlayer1 = MediaPlayer.create(MusicActivity.this, R.raw.nature);
        final MediaPlayer mediaPlayer2 = MediaPlayer.create(MusicActivity.this, R.raw.instrument);

        natureSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer1.isPlaying()){
                    mediaPlayer1.pause();

                } else {
                    mediaPlayer1.start();
                }
            }
        });

        pianoSounds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mediaPlayer2.isPlaying()){
                    mediaPlayer2.pause();

                } else {
                    mediaPlayer2.start();

                }
            }
        });

        spotify = (ImageView) findViewById(R.id.localmusic_button);
        spotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoURL("https://open.spotify.com/episode/2nYaI7vGXMJjcw0Jmb9FM3");
            }
        });


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

                Intent intent = new Intent(MusicActivity.this, BreathingActivity.class);
                startActivity(intent);


            }
        });

    }

    private void gotoURL(String s) {
        Uri uri = Uri.parse(s);
        startActivity(new Intent(Intent.ACTION_VIEW, uri));
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

                /*----FINISHED TIMER ALARM----*/
                final MediaPlayer mediaPlayer = MediaPlayer.create(this, R.raw.timersound);
                mediaPlayer.start();

                /*----DEFAULT TIME----*/
                taskCountdownTimer.cancel();
                timeLeft = GlobalVariable.OffTimer * 60000;
                int minutes = (int) ((timeLeft/1000)%3600)/60;
                int seconds = (int) (timeLeft/1000)%60;

                String timeLeftDisplay = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
                OffTaskTimer.setText(timeLeftDisplay);

                /*---- PROCEEDS TO NEXT PAGE IF TIMER IS DONE----*/
                Intent in = new Intent(MusicActivity.this, OffContinueActivity.class);
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


        AlertDialog.Builder builder = new AlertDialog.Builder(MusicActivity.this);

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
                Intent in = new Intent(MusicActivity.this, OffContinueActivity.class);
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