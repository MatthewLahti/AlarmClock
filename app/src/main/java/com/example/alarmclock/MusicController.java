package com.example.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import java.io.File;
import java.nio.file.Paths;

public class MusicController {
    private static MusicController instance;
    private Context context;
    private MediaPlayer player;
    private Ringtone ringtone;
    private int alarmID;
    public MusicController(Context context, int alarmid){
        this.alarmID = alarmid;
        this.context = context;
        instance = this;
    }
    public static MusicController getInstance(Context context, int alarmid){
        if (instance == null){
            instance = new MusicController(context, alarmid);
        }
        return instance;
    }
    public void playMusic(){
        Uri uri = null;
        System.out.println("TRYING TO PLAY MUSIC LOOKING FOR ALARM ID: " + this.alarmID);
        for(Alarm a:MainActivity.alarmList.getAlarms()){
            if(a.id == this.alarmID){
                SharedPreferences settings = this.context.getSharedPreferences("AlarmClock",0);
                String toneString = settings.getString(a.ringtoneTitle,null);
                uri = Uri.parse(toneString);
            }
        }
        if(uri != null){
            ringtone = RingtoneManager.getRingtone(context, uri);
            ringtone.setLooping(true);
            ringtone.play();
        } else{
            player = MediaPlayer.create(context,R.raw.samsung_galaxy_s8_over_the_horizon);
            player.setLooping(true);
            player.start();
        }
    }
    public void stopMusic(){
        if(ringtone != null){
            ringtone.stop();
        }
        if(player != null){
            player.stop();
        }
    }

}
