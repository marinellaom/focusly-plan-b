package com.example.focuslyplanb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class ConfirmTaskActivity extends AppCompatActivity {

    TextView taskNameDisplay;
    TextView OnTimeDisplay;
    TextView OffTimeDisplay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_task);

        /*--DISPLAY USER INPUT--*/
        taskNameDisplay = (TextView) findViewById(R.id.task_name_display);
        OnTimeDisplay = (TextView) findViewById(R.id.on_time_display);
        OffTimeDisplay = (TextView) findViewById(R.id.off_time_display);

        taskNameDisplay.setText(GlobalVariable.taskname);
        OnTimeDisplay.setText(GlobalVariable.OnTimer + " minutes");
        OffTimeDisplay.setText(GlobalVariable.OffTimer + " minutes");

    }


    /*--- BACK BUTTON ---*/
    public void goBack(View back){
        Intent i = new Intent(this, SetTaskTimerActivity.class);
        startActivity(i);
    }


    /*--- CONFIRM BUTTON ---*/
    public void confirmInput(View displayText){
        displayText.setEnabled(false);

        Toast.makeText(this, "Confirmed!", Toast.LENGTH_SHORT).show();
        Log.d("myLog", "User Input Confirmed");

        Intent i = new Intent(this, OnTaskActivity.class);

        startActivity(i);
    }
}