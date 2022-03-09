package com.example.focuslyplanb;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;


public class SettingsActivity extends AppCompatActivity {

    ToggleButton wifi, sound;
    AudioManager audioManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        sound = findViewById(R.id.sound_switch);
        wifi = findViewById(R.id.wifi_switch);
        audioManager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);


        //AUTOMATICALLY OPEN WIFI SETTINGS FOR MANUAL DISABLING OF WIFI
        wifi.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    Intent settingsIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                    startActivity(settingsIntent);

                }
                else
                {
                    Intent settingsIntent = new Intent(Settings.Panel.ACTION_INTERNET_CONNECTIVITY);
                    startActivity(settingsIntent);

                }

            }
        });


        //MUTE OR UNMUTE NOTIFICATION RING SOUNDS OF THE DEVICE
        sound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {

                    audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                    Toast.makeText(SettingsActivity.this, "Mute Sound", Toast.LENGTH_SHORT).show();

                }
                else
                {

                    audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_UNMUTE, 0);
                    Toast.makeText(SettingsActivity.this, "Unmute Sound", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }


    public void goBack(View back){
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }


}