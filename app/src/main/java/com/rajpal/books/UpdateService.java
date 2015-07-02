package com.rajpal.books;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

public class UpdateService extends Service {

    private boolean doubleBackToExitPressedOnce;

    @Override
    public void onCreate() {
        super.onCreate();
        // REGISTER RECEIVER THAT HANDLES SCREEN ON AND SCREEN OFF LOGIC
        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
        filter.addAction(Intent.ACTION_SCREEN_OFF);
        BroadcastReceiver mReceiver = new ScreenReceiver();
        registerReceiver(mReceiver, filter);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        Log.e("----screen--", "-state = " + screenOn);
        if (doubleBackToExitPressedOnce) {
      Log.e("----screen--", "---run succeeed...");
            return;
        }

        this.doubleBackToExitPressedOnce = true;
       /// Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
        Log.e("----screen--", "press power agnin to perform");
        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 800);
//        if (!screenOn) {
//            // YOUR CODE
//        } else {
//            // YOUR CODE
//        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}