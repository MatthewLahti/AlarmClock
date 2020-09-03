package com.example.alarmclock;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    private NotificationManager nManager;
    public NotificationHelper(Context base){
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChannels();
        }
    }
    @TargetApi(Build.VERSION_CODES.O)
    public void createChannels(){
        NotificationChannel channel = new NotificationChannel("id","name", NotificationManager.IMPORTANCE_HIGH);
        channel.enableLights(true);
        channel.enableVibration(true);
        channel.setLightColor(R.color.colorPrimary);
        channel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        getManager().createNotificationChannel(channel);
    }

    public NotificationManager getManager(){
        if(nManager == null){
            nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return nManager;
    }

    public NotificationCompat.Builder getChannelNotification(String title,String message,int alarmid){
        Intent dismissIntent = new Intent(this, DismissBroadcast.class);
        dismissIntent.putExtra("dismissMessage","Alarm dismissed");
        dismissIntent.putExtra("ALARMID",alarmid);
        PendingIntent pendingDismissIntent = PendingIntent.getBroadcast(this,0,dismissIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        long[] pattern = {0,100,25};
        NotificationCompat.Builder notification = new NotificationCompat.Builder(getApplicationContext(),"id");
        return new NotificationCompat.Builder(getApplicationContext(), "id")
                .setContentTitle(title)
                .setColor(Color.BLUE)
                .setContentText(message)
                .setOngoing(true)
                .setVibrate(pattern)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .addAction(R.mipmap.ic_launcher,"Dismiss Alarm",pendingDismissIntent)
                .setSmallIcon(R.drawable.ic_access_alarm_black_24dp);
    }
}
