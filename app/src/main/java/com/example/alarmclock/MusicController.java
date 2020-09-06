package com.example.alarmclock;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioAttributes;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.VibrationEffect;
import android.os.Vibrator;

import java.io.File;
import java.nio.file.Paths;

public class MusicController {
    private static MusicController instance;
    private Context context;
    private MediaPlayer player;
    private Ringtone ringtone;
    private int alarmID;
    private boolean vibrate = true;
    private int volume = 100;
    private boolean playSound = true;
    private AudioAttributes attributes;
    private Vibrator vibrator;
    private long[] vibratePattern;
    public MusicController(Context context, int alarmid){
        this.alarmID = alarmid;
        this.context = context;
        instance = this;
        this.attributes = new AudioAttributes
                .Builder()
                .setUsage(AudioAttributes.USAGE_ALARM)
                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                .build();
        this.vibrator = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
    }
    public static MusicController getInstance(Context context, int alarmid){
        if (instance == null){
            instance = new MusicController(context, alarmid);
        }
        return instance;
    }
    public void playMusic(){
        Uri uri = null;
        for(Alarm a:MainActivity.alarmList.getAlarms()){
            if(a.id == this.alarmID){
                SharedPreferences settings = this.context.getSharedPreferences("AlarmClock",0);
                String toneString = settings.getString(a.ringtoneTitle,null);
                uri = Uri.parse(toneString);
                vibratePattern = a.vibratePattern;
                if(!a.sound){
                    playSound = false;
                }
                if(!a.vibrate){
                    vibrate = false;
                }
                volume = a.volume;
                break;
            }
        }
        if(uri != null && playSound){
            ringtone = RingtoneManager.getRingtone(context, uri);
            ringtone.setLooping(true);
            ringtone.setVolume(this.volume);
            ringtone.setAudioAttributes(attributes);
            ringtone.play();
        } else if (playSound){
            player = MediaPlayer.create(context,R.raw.samsung_galaxy_s8_over_the_horizon);
            player.setLooping(true);
            player.setVolume(this.volume,this.volume);
            player.setAudioAttributes(this.attributes);
            player.start();
        }
        if(vibrate){
            vibrator.vibrate(vibratePattern,-1);
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
