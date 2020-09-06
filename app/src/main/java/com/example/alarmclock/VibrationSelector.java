package com.example.alarmclock;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Vibrator;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.lang.reflect.Array;

public class VibrationSelector extends AppCompatActivity {
    ListView vibrateView;
    public static ArrayAdapter<VibrationPatterns> adapter;
    VibrationList list = new VibrationList();
    Button saveButton;
    Button cancelButton;
    private VibrationPatterns pattern;
    private Vibrator vibrator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vibration_selector);
        long[] _p = {0,300,300};
        pattern = new VibrationPatterns("Default",_p);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrateView = findViewById(R.id.vibrateView);
        adapter = new VibrationAdapter(VibrationSelector.this,list.getPatterns());
        vibrateView.setAdapter(adapter);
        saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent data = new Intent(getApplicationContext(),NewAlarmActivity.class);
                Bundle bundle = new Bundle();
                bundle.putLongArray("pattern",pattern.getPattern());
                data.putExtras(bundle);
                data.putExtra("vibratePatternName",pattern.name);
                setResult(RESULT_OK,data);
                finish();
            }
        });
        cancelButton = findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        });
        vibrateView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                pattern = (VibrationPatterns) vibrateView.getItemAtPosition(position);
                vibrator.vibrate(pattern.getPattern(),-1);
            }
        });
    }

    public class VibrationPatterns {
        private String name;
        private long[] pattern;
        public VibrationPatterns(String n, long[] pattern){
            this.name = n;
            this.pattern = pattern;
        }
        public String getName(){
            return this.name;
        }
        public long[] getPattern(){
            return this.pattern;
        }
    }
    public class VibrationList {
        private VibrationPatterns[] patterns;
        public VibrationList(){
            this.patterns = new VibrationPatterns[6];
            long[] heartBeatPattern = {0,500,250,125};
            VibrationPatterns heartBeat = new VibrationPatterns("Heart Beat",heartBeatPattern);
            long[] starWarPattern = {0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
            VibrationPatterns starWars = new VibrationPatterns("Star war",starWarPattern);
            long[] marioPattern = {0, 125,75,125,275,200,275,125,75,125,275,200,600,200,600};
            VibrationPatterns mario = new VibrationPatterns("mario",marioPattern);
            long[] mortalKombatPattern = {0, 100,200,100,200,100,200,100,200,100,100,100,100,100,200,100,200,100,200,100,200,100,100,100,100,100,200,100,200,100,200,100,200,100,100,100,100,100,100,100,100,100,100,50,50,100,800};
            VibrationPatterns mortalKombat = new VibrationPatterns("Mortal Kombat",mortalKombatPattern);
            long[] tesseracTPerfectionPattern = {0,75,225,75,75,75,75,75,225,75,225,75,225,75,75,75,225,75,225,75,75,75,75,75,225,75,225,75,225,75,75,75,225,75,225,75,75,75,75,75,225,75,225,150,150,75,75,75,225,75,375,75,75,75,75,75,225,75,225,75,225,75,75,75,225,75,225,75,75,75,75,75,225,75,225,75,225,75,75,75,225,75,225,75,75,75,75,150,150};
            VibrationPatterns tesseracTPerfection = new VibrationPatterns("TesseracT - Perfection",tesseracTPerfectionPattern);
            long[] museMadnessPatern = {80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,80,320,160,320,160,320};
            VibrationPatterns museMadness = new VibrationPatterns("Muse - Madness",museMadnessPatern);
            this.patterns[0] = heartBeat;
            this.patterns[1] = starWars;
            this.patterns[2] = mario;
            this.patterns[3] = mortalKombat;
            this.patterns[4] = tesseracTPerfection;
            this.patterns[5] = museMadness;
        }
        public VibrationPatterns[] getPatterns(){
            return this.patterns;
        }
    }

}
