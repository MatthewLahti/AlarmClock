package com.example.alarmclock;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;

import java.io.File;
import java.util.List;

public class NewAlarmActivity extends AppCompatActivity {
    public int REQUEST_CODE = 100;
    NumberPicker hourPicker;
    NumberPicker minutePicker;
    Switch alarmSound;
    Switch alarmVibrate;
    Button cancel;
    Button save;
    Boolean sound = false;
    Boolean vibrate = false;
    String currentRingtoneTitle;
    TextView ringtoneTitle;
    Ringtone ringtone;
    String ringtoneUriPath;
    String currentringtoneTitle;
    int volume = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        setupLayout();
    }

    public void setupLayout(){
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        System.out.println("NEW ALARM SETUP LAYOUT " + currentTone.getPath());
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),currentTone);
        this.ringtoneUriPath = currentTone.getPath();
        System.out.println("NEW ALARM SETUP HASHCODE: " + currentTone.hashCode());
        this.ringtone = ringtone;
        ringtoneTitle = findViewById(R.id.ringtoneTitle);
        ringtoneTitle.setText(ringtone.getTitle(getApplicationContext()));
        this.currentRingtoneTitle = ringtone.getTitle(getApplicationContext());
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
        alarmSound = findViewById(R.id.alarmSound);
        alarmVibrate = findViewById(R.id.alarmVibrate);
        cancel = findViewById(R.id.cancel);
        save = findViewById(R.id.save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveAlarm();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelAlarm();
            }
        });
        alarmSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(alarmSound.isChecked()){
                    sound = true;
                    Intent intent = new Intent(getApplicationContext(),AlarmSoundSelector.class);
                    startActivityForResult(intent,REQUEST_CODE);
                } else{
                    sound = false;
                }
            }
        });;
        alarmVibrate.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(alarmVibrate.isChecked()){
                    vibrate = true;
                } else {
                    vibrate = false;
                }
            }
        });
        setupPicker(hourPicker,0,24);
        setupPicker(minutePicker,0,60);
    }

    public void setupPicker(NumberPicker picker, int minValue, int displayedNumbers){
        final String[] vals = new String[displayedNumbers];
        for (int i = minValue; i < displayedNumbers; i++){
            String num = String.format("%02d",i);
            vals[i] = "" + num;
        }
        picker.setMinValue(minValue);
        picker.setMaxValue(vals.length - 1);
        picker.setWrapSelectorWheel(true);
        picker.setDisplayedValues(vals);
        changeDividerColor(picker, Color.parseColor("#00ffffff"));
    }


    public void changeDividerColor(NumberPicker picker, int color) {
        java.lang.reflect.Field[] pickerFields = NumberPicker.class.getDeclaredFields();
        for (java.lang.reflect.Field pf : pickerFields) {
            if (pf.getName().equals("mSelectionDivider")) {
                pf.setAccessible(true);
                try {
                    ColorDrawable colorDrawable = new ColorDrawable(color);
                    pf.set(picker, colorDrawable);
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (Resources.NotFoundException e) {
                    e.printStackTrace();
                }
                catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                break;
            }
        }
    }
    public void cancelAlarm(){
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    public void saveAlarm(){
        Alarm alarm = new Alarm(hourPicker.getValue(),minutePicker.getValue(),sound,currentRingtoneTitle);
        String id = String.format("%02d",hourPicker.getValue()) + String.format("%02d",minutePicker.getValue());
        alarm.setId(Integer.parseInt(id));
        alarm.volume = volume;
        alarm.vibrate = vibrate;
        MainActivity.alarmList.addAlarm(alarm);
        MainActivity.alarmList.saveAlarms(getApplicationContext());
        MainActivity.adapter.notifyDataSetChanged();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE){
            if(resultCode == RESULT_OK){
                this.currentRingtoneTitle = data.getStringExtra("ringtonetitle");
                ringtoneTitle.setText(this.currentRingtoneTitle);
                Uri returnedResult = data.getData();
                volume = data.getIntExtra("ringtoneVolume",100);

                SharedPreferences settings = getSharedPreferences("AlarmClock",0);
                SharedPreferences.Editor editor = settings.edit();
                editor.putString(data.getStringExtra("ringtonetitle"),returnedResult.toString());
                editor.commit();
            }
        }
    }
}
