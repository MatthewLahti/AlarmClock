package com.example.alarmclock;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;

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
        currentvibrateTitle = alarm.vibratePatternTitle;
        vibrate = alarm.getVibrate();
        sound = alarm.getSound();
        alarmSound = findViewById(R.id.alarmSound);
        alarmVibrate = findViewById(R.id.alarmVibrate);
        alarmSound.setChecked(sound);
        alarmVibrate.setChecked(vibrate);
        setupLayout();
        currentRingtoneTitle = alarm.ringtoneTitle;
        setAlarmValues();
        alarmSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(alarmSound.isChecked()){
                    sound = true;
                    Intent intent = new Intent(getApplicationContext(),AlarmSoundSelector.class);
                    intent.putExtra("volume",alarm.volume);
                    intent.putExtra("title",alarm.ringtoneTitle);
                    startActivityForResult(intent,REQUEST_CODE_SOUND);
                } else{
                    sound = false;
                }
            }
        });
    }

    public void setAlarmValues(){
        hourPicker.setValue(alarm.getHour());
        minutePicker.setValue(alarm.getMinute());
        ringtoneTitle.setText(alarm.ringtoneTitle);
        vibrateTitle.setText(alarm.vibratePatternTitle);
    }

    @Override
    public void saveAlarm(){
        String id = String.format("%02d",hourPicker.getValue()) + String.format("%02d",minutePicker.getValue());
        alarm.setHour(hourPicker.getValue());
        alarm.setMinute(minutePicker.getValue());
        alarm.ringtoneTitle = currentRingtoneTitle;
        alarm.vibratePatternTitle = currentvibrateTitle;
        alarm.setVibratePattern(vibrationPattern);
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_SOUND){
            if(resultCode == RESULT_OK){
                currentRingtoneTitle = data.getStringExtra("ringtonetitle");
                ringtoneTitle.setText(this.currentRingtoneTitle);
                Uri returnedResult = data.getData();
                volume = data.getIntExtra("ringtoneVolume",100);

                SharedPreferences settings = getSharedPreferences("AlarmClock",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(data.getStringExtra("ringtonetitle"),returnedResult.toString());
                editor.commit();
            }
        } else if(requestCode == REQUEST_CODE_VIBRATE){
            if(resultCode == RESULT_OK){
                Bundle bundle = data.getExtras();
                long[] pattern = bundle.getLongArray("pattern");
                this.vibrationPattern = pattern;
                currentvibrateTitle = data.getStringExtra("vibratePatternName");
                vibrateTitle.setText(currentvibrateTitle);
            } else {
                if(alarm.vibratePatternTitle != null){
                    vibrationPattern = alarm.vibratePattern;
                    currentvibrateTitle = alarm.vibratePatternTitle;
                } else {
                    long[] defaultPattern = {0,100,100};
                    vibrationPattern = defaultPattern;
                    currentvibrateTitle = "Default";
                }
                vibrateTitle.setText(currentvibrateTitle);
            }
        }
    }
}
