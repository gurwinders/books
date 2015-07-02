package com.rajpal.books;

import android.app.NotificationManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import java.util.Random;

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
        if (intent !=null) {
        boolean screenOn = intent.getBooleanExtra("screen_state", false);
        Log.e("----screen--", "-state = " + screenOn);
        if (doubleBackToExitPressedOnce) {
            Log.e("----screen--", "---run succeeed...");
          ShowNotification();
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
        }else {
            Log.e("---screen---", "Intent null");

        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void ShowNotification() {
        NotificationManager mNotifyManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
        Bitmap bm = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        mBuilder.setContentTitle("Message Notification")
                .setContentText("Message sent")
                .setTicker("Messaging...")
                .setSmallIcon(R.mipmap.ic_download)
                .setLargeIcon(bm);

        Random rand = new Random();

        // nextInt is normally exclusive of the top value,
        // so add 1 to make it inclusive
        int randomNum = rand.nextInt((20 - 1) + 1) + 1;
        mNotifyManager.notify(randomNum, mBuilder.build());
    }
}