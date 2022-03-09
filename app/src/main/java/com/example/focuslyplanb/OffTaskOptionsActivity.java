package com.example.focuslyplanb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OffTaskOptionsActivity extends AppCompatActivity {

    private Button PlayMusic;
    private Button Stretching;
    private Button Walk;
    private Button Eat;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_task_options);

        PlayMusic = findViewById(R.id.music_button);
        Stretching = findViewById(R.id.stretch_button);
        Walk = findViewById(R.id.walk_button);
        Eat = findViewById(R.id.eat_button);

        PlayMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(OffTaskOptionsActivity.this, MusicActivity.class);
                startActivity(i);
            }
        });

        Stretching.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OffTaskOptionsActivity.this, StretchingActivity.class);
                startActivity(in);
            }
        });

        Walk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OffTaskOptionsActivity.this, WalkActivity.class);
                startActivity(in);
            }
        });

       Eat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(OffTaskOptionsActivity.this, EatActivity.class);
                startActivity(in);
            }
        });


    }
}