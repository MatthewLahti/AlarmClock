package com.example.alarmclock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Switch;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public static String ALARMIDKEY = "hello";
    ImageButton newAlarm;
    public static ArrayAdapter<Alarm> adapter;
    Switch alarmState;
    public static AlarmList alarmList = new AlarmList();
    ListView alarmView;
    Context context;
    private NotificationHelper mNotificationHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mNotificationHelper = new NotificationHelper(this);
        context = getApplicationContext();
        alarmList.loadAlarms(context);
        alarmView = (ListView) findViewById(R.id.alarmView);
        adapter = new AlarmAdapter(MainActivity.this,alarmList.getAlarms());
        alarmView.setAdapter(adapter);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        alarmState = findViewById(R.id.alarmState);
        newAlarm = findViewById(R.id.newAlarm);
        newAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openActivityNewAlarm();
            }
        });
    }
    @Override
    protected void onStart(){
        super.onStart();
        Context context = getApplicationContext();
    }
    public void openActivityNewAlarm(){
        Intent intent = new Intent(this,NewAlarmActivity.class);
        startActivity(intent);
    }

}