package com.example.exampleproject.BroadCast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;

public class Timerservice  extends BroadcastReceiver {
    public static final int REQUEST_CODE = 12345;
    Context mContext;
    public static final String ACTION = "com.codepath.example.servicesdemo.alarm";

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(Context context, Intent intent) {


        final LocationManager manager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        if ( !manager.isProviderEnabled(LocationManager.GPS_PROVIDER) ) {
            Intent i1 = new Intent(context, BroadCastActivity.class);
            i1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i1);
//               buildAlertMessageNoGps();
        }else{
            Intent i = new Intent(context, GPSTracker.class);
            GPSTracker gpsTracker=new GPSTracker(context);
            gpsTracker.getLocation();
            context.startService(i);
        }
    }

}
