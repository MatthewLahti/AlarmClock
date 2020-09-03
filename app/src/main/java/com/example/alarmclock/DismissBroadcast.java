package com.example.alarmclock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

public class DismissBroadcast extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
        int id = intent.getIntExtra("ALARMID",-1);
        MusicController.getInstance(context,id).stopMusic();
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder nb = notificationHelper.getChannelNotification("title","message",-1);
        notificationHelper.getManager().cancel(1);

        for (Alarm a:MainActivity.alarmList.getAlarms()) {
            if(a.id == id){
                a.setStatus(false);
                MainActivity.alarmList.saveAlarms(context);
                MainActivity.adapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
