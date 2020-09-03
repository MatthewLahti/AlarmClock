package com.example.alarmclock;
import android.content.Context;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class AlarmList {
    private ArrayList<Alarm> alarms;
    private String FILENAME = "alarms.sav";
    public AlarmList(){
        alarms = new ArrayList<Alarm>();
    }
    public void addAlarm(Alarm alarm){
        this.alarms.add(alarm);
    }
    public ArrayList<Alarm> getAlarms(){
        return this.alarms;
    }
    public Alarm getAlarm(int id){
        return this.alarms.get(id);
    }

    public void deleteAlarm(Alarm alarm){
        this.alarms.remove(alarm);
    }
    public void loadAlarms(Context context){
       try {
            FileInputStream fis = context.openFileInput(FILENAME);
            InputStreamReader isr = new InputStreamReader(fis);
            Gson gson = new Gson();
            Type listType = new TypeToken<ArrayList<Alarm>>() {}.getType();
            alarms = gson.fromJson(isr, listType);
            fis.close();
        } catch (FileNotFoundException e) {
            alarms = new ArrayList<Alarm>();
        } catch (IOException e) {
            alarms = new ArrayList<Alarm>();
        }
    }
    public void saveAlarms(Context context){
        try {
            FileOutputStream fos = context.openFileOutput(FILENAME, 0);
            OutputStreamWriter osw = new OutputStreamWriter(fos);
            Gson gson = new Gson();
            gson.toJson(alarms, osw);
            osw.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
