package com.example.focuslyplanb;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OffContinueActivity extends AppCompatActivity {

    private Button cont, setNew, offcont;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_off_continue);

        cont = findViewById(R.id.off_continue_btn);
        setNew = findViewById(R.id.set_new_task_btn);
        offcont = findViewById(R.id.off_cont_button);


        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OffContinueActivity.this);

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
                        Intent in = new Intent(OffContinueActivity.this, OnTaskActivity.class);
                        startActivity(in);
                    }
                });
                builder.show();
            }
        });


        offcont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(OffContinueActivity.this);

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
                        OffContinueActivity.this.finish();
                    }
                });
                builder.show();
            }
        });

        setNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(OffContinueActivity.this, SetTaskTimerActivity.class);
                startActivity(i);

            }
        });


//        overview.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//
//                Intent i = new Intent(OffContinueActivity.this, OverviewActivity.class);
//                startActivity(i);
//
//            }
//        });



    }
}