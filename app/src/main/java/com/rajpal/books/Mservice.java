package com.rajpal.books;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

/**
 * Created by asus on 30-06-15.
 */
public class Mservice extends Service {
    private SettingsContentObserver mSettingsContentObserver;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mSettingsContentObserver = new SettingsContentObserver(getApplication(),new Handler());
        getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver );
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        getApplicationContext().getContentResolver().unregisterContentObserver(mSettingsContentObserver);

        super.onDestroy();
    }
}
