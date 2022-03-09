package com.example.focuslyplanb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ContinueActivity extends AppCompatActivity {

    private Button cont;
    private Button rest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue);

        cont = findViewById(R.id.continute_task_btn);
        rest = findViewById(R.id.rest_btn);

//        cont.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent i = new Intent(ContinueActivity.this, OnTaskActivity.class);
//                startActivity(i);
//            }
//        });


        // POP UP CONFIRMATION
        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ContinueActivity.this);

                builder.setCancelable(true);
                builder.setTitle("Are you sure?");
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
                        Intent in = new Intent(ContinueActivity.this, OnTaskActivity.class);
                        startActivity(in);
                    }
                });
                builder.show();
            }
        });


        rest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ContinueActivity.this, OffTaskOptionsActivity.class);
                startActivity(i);
            }
        });
    }
}