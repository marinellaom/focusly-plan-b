package com.example.focuslyplanb;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import android.text.TextUtils;


public class SetTaskTimerActivity extends AppCompatActivity{

    EditText taskName;
    Spinner OnSpinner;
    Spinner OffSpinner;
    Button setButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_task_timer);
        setTitle("On Task");

        OnSpinner = (Spinner) findViewById(R.id.on_spinner);
        OffSpinner = (Spinner) findViewById(R.id.off_spinner);
        setButton = (Button) findViewById(R.id.set_button);
        taskName = (EditText) findViewById(R.id.taskname);

        //DEFAULT TIME LIST
        Integer[] ontime = new Integer[]{15, 20, 25, 30, 35, 40, 45};
        Integer[] offtime = new Integer[]{10, 15, 20, 25, 30};

        //ADAPTER describes how the items (time list) are displayed
        ArrayAdapter<Integer> OnAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_layout,  ontime);
        ArrayAdapter<Integer> OffAdapter = new ArrayAdapter<>(this, R.layout.spinner_text_layout, offtime);

        OnSpinner.setAdapter(OnAdapter);
        OffSpinner.setAdapter(OffAdapter);


        setButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*--- GET AND DISPLAY INFO TO OTHER ACTIVITY (PAGE) ---*/
                GlobalVariable.taskname = taskName.getText().toString();
                GlobalVariable.OnTimer = Long.parseLong(OnSpinner.getSelectedItem().toString());
                GlobalVariable.OffTimer = Long.parseLong(OffSpinner.getSelectedItem().toString());

                Intent i = new Intent(SetTaskTimerActivity.this, ConfirmTaskActivity.class);


                /*--- VALIDATE USER INPUT IF EMPTY---*/
                if (TextUtils.isEmpty(GlobalVariable.taskname)){
                    Toast.makeText(SetTaskTimerActivity.this, "Task name cannot be empty!", Toast.LENGTH_SHORT).show();
                    return;
                } else if ((GlobalVariable.taskname).trim().length()==0){
                    Toast.makeText(SetTaskTimerActivity.this, "Invalid Input!", Toast.LENGTH_SHORT).show();
                    return;
                }
                startActivity(i);
            }
        });
    }


    public void goBack(View back){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

}
