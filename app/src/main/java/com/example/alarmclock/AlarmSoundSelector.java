package com.example.alarmclock;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;


public class AlarmSoundSelector extends AppCompatActivity {
    Switch alarmSound;
    Button ringtoneSelector;
    Button saveButton;
    Uri ringtone;
    String ringtoneTitle;
    String existingRingtoneTitle = null;
    SeekBar volumeBar;
    Ringtone _ringtone;
    int volume = 100;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_sound_selector);
        ringtoneSelector = findViewById(R.id.ringtoneSelector);
        Intent receive = getIntent();
        existingRingtoneTitle = receive.getStringExtra("title");
        volume = receive.getIntExtra("volume",100);
        if(existingRingtoneTitle != null){
            SharedPreferences settings = getSharedPreferences("AlarmClock",0);
            String toneString = settings.getString(existingRingtoneTitle,null);
            ringtone = Uri.parse(toneString);
            ringtoneSelector.setText(existingRingtoneTitle);
            this.ringtoneTitle = existingRingtoneTitle;
        } else {
            ringtone = RingtoneManager.getActualDefaultRingtoneUri(this, RingtoneManager.TYPE_ALARM);
            _ringtone = RingtoneManager.getRingtone(getApplicationContext(),ringtone);
            ringtoneSelector.setText(_ringtone.getTitle(getApplicationContext()));
            this.ringtoneTitle = _ringtone.getTitle(getApplicationContext());
        }
        volumeBar = findViewById(R.id.volumeBar);
        saveButton = findViewById(R.id.saveButton);
        ringtoneSelector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Uri currentTone= RingtoneManager.getActualDefaultRingtoneUri(AlarmSoundSelector.this, RingtoneManager.TYPE_ALARM);
                Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE, RingtoneManager.TYPE_ALARM);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI, currentTone);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
                intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
                startActivityForResult(intent, 1);

            }
        });
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent data = new Intent(getApplicationContext(),NewAlarmActivity.class);
                data.setData(ringtone);
                data.putExtra("ringtonetitle",ringtoneTitle);
                data.putExtra("ringtoneVolume",volume);
                setResult(RESULT_OK,data);
                finish();
            }
        });
        volumeBar.setProgress(volume);
        volumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                volume = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_OK){
            switch (requestCode){
                case 1:
                    ringtone = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
                    Ringtone temp = RingtoneManager.getRingtone(getApplicationContext(),ringtone);
                    ringtoneSelector.setText(temp.getTitle(getApplicationContext()));
                    ringtoneTitle = temp.getTitle(getApplicationContext());
            }
        } else{
            finish();
        }
    }
}
