package com.example.myapplication.view;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.Toast;

import com.example.myapplication.Notification.ReminderBroadcast;
import com.example.myapplication.R;
import com.example.myapplication.listener.NotifyListener;

import java.util.Calendar;
import java.util.Objects;

public class NotifyFragment extends Fragment {
    private NotifyListener notifyListener;

    public NotifyFragment(NotifyListener notifyListener) {
        this.notifyListener = notifyListener;
    }

    public static long minute, minuteInMilis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notify, container, false);

        Button button = view.findViewById(R.id.button);
        Button stop = view.findViewById(R.id.buttonStop);
        RadioButton btn1, btn5, btn10;
        btn1 = view.findViewById(R.id.radio_1);
        btn5 = view.findViewById(R.id.radio_5);
        btn10 = view.findViewById(R.id.radio_10);


        button.setOnClickListener( v -> {
            if (!btn1.isChecked() && !btn5.isChecked() && !btn10.isChecked() ) {
                Toast.makeText(getContext(), "Reminder not set!", Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getContext(), "Reminder set!", Toast.LENGTH_SHORT).show();
                    if (btn1.isChecked()) minute = 1;
                    if (btn5.isChecked()) minute = 5;
                    if (btn10.isChecked()) minute = 10;

                    minuteInMilis = 1000 * 60 * minute;
                notifyListener.onUpdateFromNotify();
            }
        });

        stop.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Reminder stopped!", Toast.LENGTH_SHORT).show();
            notifyListener.onStopNotify();
        });


        return view;
    }


}

