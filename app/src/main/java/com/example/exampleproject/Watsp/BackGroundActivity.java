package com.example.exampleproject.Watsp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.exampleproject.databinding.ActivityBackGroundBinding;
import com.example.exampleproject.databinding.ActivityPaymentBinding;
import com.google.android.gms.common.api.GoogleApiClient;

import org.jetbrains.annotations.NotNull;

public class BackGroundActivity extends AppCompatActivity {
    ActivityBackGroundBinding binding;
    Intent mServiceIntent;
    private AutoStartService mAutoStartService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackGroundBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        mAutoStartService = new AutoStartService(this);
        mServiceIntent = new Intent(this, AutoStartService.class);

        if (!isMyServiceRunning(AutoStartService.class)) {
            startService(mServiceIntent);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i ("isMyServiceRunning?", true+"");
                return true;
            }
        }
        Log.i ("isMyServiceRunning?", false+"");
        return false;
    }

    @Override
    protected void onDestroy() {
        stopService(mServiceIntent);
        Log.i("main", "onDestroy!");
        super.onDestroy();

    }
}