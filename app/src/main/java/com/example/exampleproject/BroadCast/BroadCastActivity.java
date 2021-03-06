package com.example.exampleproject.BroadCast;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.exampleproject.databinding.ActivityBroadCastBinding;
import com.example.exampleproject.databinding.ActivityCaptureAndGalleryBinding;

public class BroadCastActivity extends AppCompatActivity {

    ActivityBroadCastBinding binding;
    PendingIntent pIntent;
    AlarmManager alarm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBroadCastBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        scheduleAlarm();
    }

    public void scheduleAlarm() {
        // Construct an intent that will execute the AlarmReceiver
        Intent intent = new Intent(getApplicationContext(), Timerservice.class);
        // Create a PendingIntent to be triggered when the alarm goes off
        pIntent = PendingIntent.getBroadcast(this, Timerservice.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        // Setup periodic alarm every 5 seconds
        long firstMillis = System.currentTimeMillis(); // alarm is set right away
        alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        // First parameter is the type: ELAPSED_REALTIME, ELAPSED_REALTIME_WAKEUP, RTC_WAKEUP
        // Interval can be INTERVAL_FIFTEEN_MINUTES, INTERVAL_HALF_HOUR, INTERVAL_HOUR, INTERVAL_DAY
        alarm.setInexactRepeating(AlarmManager.RTC_WAKEUP, firstMillis,
                60000, pIntent);
    }
}