package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;

import java.io.File;

public class EditAlarmActivity extends NewAlarmActivity {
    Alarm alarm;
    Button delete;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_alarm);
        delete = findViewById(R.id.deleteAlarm);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAlarm();
            }
        });
        Intent intent = getIntent();
        int id = intent.getIntExtra("AlarmPos",-1);
        alarm = MainActivity.adapter.getItem(id);
        setupLayout();
        hourPicker.setValue(alarm.getHour());
        minutePicker.setValue(alarm.getMinute());
        ringtoneTitle.setText(alarm.ringtoneTitle);
        alarmSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(alarmSound.isChecked()){
                    sound = true;
                    Intent intent = new Intent(getApplicationContext(),AlarmSoundSelector.class);
                    intent.putExtra("volume",alarm.volume);
                    intent.putExtra("title",alarm.ringtoneTitle);
                    startActivityForResult(intent,REQUEST_CODE);
                } else{
                    sound = false;
                }
            }
        });;
    }

    @Override
    public void saveAlarm(){
        String id = String.format("%02d",hourPicker.getValue()) + String.format("%02d",minutePicker.getValue());
        alarm.setHour(hourPicker.getValue());
        alarm.setMinute(minutePicker.getValue());
        alarm.ringtoneTitle = this.currentRingtoneTitle;
        alarm.sound = sound;
        alarm.volume = volume;
        alarm.vibrate = vibrate;
        alarm.setId(Integer.parseInt(id));

        MainActivity.alarmList.saveAlarms(getApplicationContext());
        MainActivity.adapter.notifyDataSetChanged();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
    public void deleteAlarm(){
        MainActivity.alarmList.deleteAlarm(alarm);
        MainActivity.alarmList.saveAlarms(getApplicationContext());
        MainActivity.adapter.notifyDataSetChanged();
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }
}
