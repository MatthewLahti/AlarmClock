package com.example.alarmclock;

import android.content.Context;
import android.content.ContextWrapper;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class VibrationAdapter extends ArrayAdapter<VibrationSelector.VibrationPatterns> {
    private Context context;
    private LayoutInflater inflater;
    public VibrationAdapter(Context context, VibrationSelector.VibrationPatterns[] vibrations){
        super(context,0,vibrations);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        final VibrationSelector.VibrationPatterns patternOption = getItem(position);
        if (convertView == null) {
            convertView = inflater.from(context).inflate(R.layout.vibration_list_option, parent, false);
        }
        TextView vibrateName = convertView.findViewById(R.id.vibrateName);
        vibrateName.setText(patternOption.getName());

        return convertView;
    }

}
