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
    public int REQUEST_CODE_SOUND = 100;
    public int REQUEST_CODE_VIBRATE = 101;
    long[] vibrationPattern;
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
    TextView vibrateTitle;
    Ringtone ringtone;
    String ringtoneUriPath;
    String currentringtoneTitle;
    String currentvibrateTitle;
    int volume = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_alarm);
        alarmSound = findViewById(R.id.alarmSound);
        alarmVibrate = findViewById(R.id.alarmVibrate);
        currentvibrateTitle = "Default";
        setupLayout();
    }

    public void setupLayout(){
        final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
        Ringtone ringtone = RingtoneManager.getRingtone(getApplicationContext(),currentTone);
        this.ringtoneUriPath = currentTone.getPath();
        this.ringtone = ringtone;
        ringtoneTitle = findViewById(R.id.ringtoneTitle);
        vibrateTitle = findViewById(R.id.vibrateTitle);
        ringtoneTitle.setText(ringtone.getTitle(getApplicationContext()));
        vibrateTitle.setText(currentvibrateTitle);
        currentRingtoneTitle = ringtone.getTitle(getApplicationContext());
        hourPicker = findViewById(R.id.hourPicker);
        minutePicker = findViewById(R.id.minutePicker);
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
                    startActivityForResult(intent,REQUEST_CODE_SOUND);
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
                    Intent intent = new Intent(getApplicationContext(),VibrationSelector.class);
                    startActivityForResult(intent,REQUEST_CODE_VIBRATE);
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
        alarm.setVibratePattern(vibrationPattern);
        alarm.vibratePatternTitle = currentvibrateTitle;
        MainActivity.alarmList.addAlarm(alarm);
        MainActivity.alarmList.saveAlarms(getApplicationContext());
        MainActivity.adapter.notifyDataSetChanged();

        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
    }

    // This is only going to work for new alarms, i need to figure something out for editing the alarms
    // as when the activity ends, it
    // ok thats entirely false actually, I never actually start the intent from the selector activity
    // i can leave this as is
    // do i even want to set a default pattern? idk, it could make sense to just have no vibration if no custom pattern is selected?
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_SOUND){
            if(resultCode == RESULT_OK){
                currentRingtoneTitle = data.getStringExtra("ringtonetitle");
                ringtoneTitle.setText(currentRingtoneTitle);
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
                long[] defaultPattern = {0,100,100};
                this.vibrationPattern = defaultPattern;
                currentvibrateTitle = "Default";
                vibrateTitle.setText(currentvibrateTitle);
            }
        }
    }
}
