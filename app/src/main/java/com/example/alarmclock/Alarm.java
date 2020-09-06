package com.example.alarmclock;


import android.media.Ringtone;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Alarm implements Serializable{
    private int hour;
    private int minute;
    private String displayMinute;
    private String displayHour;
    public boolean vibrate;
    public boolean sound;
    public String label;
    public boolean status;
    public int id;
    public int volume;
    public String ringtoneTitle;
    public long[] vibratePattern;
    public String vibratePatternTitle;

    public Alarm(int hour, int minute, Boolean sound,String ringtoneTitle){
        this.displayMinute = String.format("%02d", minute);
        this.displayHour = String.format("%02d", hour);
        this.hour = hour;
        this.minute = minute;
        this.status = true;
        this.label = "" + hour + ":" + minute;
        this.sound = sound;
        this.ringtoneTitle = ringtoneTitle;
    }
    public void setVibratePattern(long[] p){
        this.vibratePattern = p;
    }
    public void setId(int n) {
        this.id = n;
    }

    public int getHour() {
        return this.hour;
    }

    public int getMinute() {
        return this.minute;
    }

    public String getDisplayHour() {
        return this.displayHour;
    }

    public String getDisplayMinute() {
        return this.displayMinute;
    }

    public boolean getStatus() {
        return this.status;
    }

    public boolean getVibrate() {
        return this.vibrate;
    }

    public boolean getSound() {
        return this.sound;
    }

    public void setStatus(boolean status) {
        if (status) {
            this.status = true;
        } else {
            this.status = false;
        }
    }

    public int getId() {
        return this.id;
    }

    public void setHour(int hour) {
        this.hour = hour;
        this.displayHour = String.format("%02d", hour);
    }

    public void setMinute(int minute) {
        this.minute = minute;
        this.displayMinute = String.format("%02d", minute);
    }
}

