package com.example.alarmclock;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.provider.AlarmClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmAdapter extends ArrayAdapter<Alarm> {
    private LayoutInflater inflater;
    private Context context;
    public AlarmAdapter(Context context, ArrayList<Alarm> alarms){
        super(context, 0, alarms);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final Alarm alarm = getItem(position);
        String hour = alarm.getDisplayHour();
        String minute = alarm.getDisplayMinute();
        String id = "" + alarm.id;
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.alarmlist_alarm, parent, false);
        }
        TextView hourText = convertView.findViewById(R.id.hour);
        TextView minuteText = convertView.findViewById(R.id.minute);
        hourText.setText(hour);
        minuteText.setText(minute);
        final Switch alarmState = convertView.findViewById(R.id.alarmState);
        alarmState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    alarm.setStatus(true);
                    MainActivity.alarmList.saveAlarms(context);
                    MainActivity.adapter.notifyDataSetChanged();
                    alarmState.setText("On");
                } else {
                    alarm.setStatus(false);
                    MainActivity.alarmList.saveAlarms(context);
                    MainActivity.adapter.notifyDataSetChanged();
                    alarmState.setText("Off");
                }
            }
        });
        if(alarm.getStatus()){
            startAlarm(alarm);
            alarmState.setChecked(true);
            alarmState.setText("On");
        } else {
            cancelAlarm(alarm);
            alarmState.setChecked(false);
            alarmState.setText("Off");
        }


        convertView.setOnClickListener(new View.OnClickListener(){
            public void onClick(final View v){
                System.out.println("alarm time: " + alarm.getHour() + ":" + alarm.getMinute());
                Intent intent = new Intent(context,EditAlarmActivity.class);
                intent.putExtra("AlarmPos",position);
                context.startActivity(intent);
            }
        });

        return convertView;
    }

    public void startAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        intent.putExtra("AlarmID",alarm.id);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarm.id,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,alarm.getHour());
        c.set(Calendar.MINUTE,alarm.getMinute());
        c.set(Calendar.SECOND,0);
        //if (c.before(Calendar.getInstance())) {
        //    c.set(Calendar.DAY_OF_MONTH,(Calendar.DAY_OF_MONTH + 1));
        //}
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,c.getTimeInMillis(),pendingIntent);
    }

    public void cancelAlarm(Alarm alarm){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context,alarm.id,intent,0);
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,alarm.getHour());
        c.set(Calendar.MINUTE,alarm.getMinute());
        c.set(Calendar.SECOND,0);
        alarmManager.cancel(pendingIntent);
    }
}
