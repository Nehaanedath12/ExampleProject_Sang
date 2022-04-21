package com.example.exampleproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;

import com.example.exampleproject.databinding.ActivityIMEIBinding;

public class IMEIActivity extends AppCompatActivity {

    ActivityIMEIBinding binding;
    @SuppressLint("HardwareIds")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityIMEIBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);


        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(IMEIActivity.this, new String[]{Manifest.permission.READ_PHONE_STATE}, 100);
        } else {
            TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);

            try {
                if(telephonyManager.getDeviceId()!=null) {
                    binding.textView2.setText("IMEI : "+telephonyManager.getDeviceId() );
                    Log.d("imei1imei1", telephonyManager.getDeviceId() + "" + " IMEI");
                }
            }catch (Exception e){
                Log.d("imei1imei1",e.toString()+e.getMessage());
            }
        }
    }
}