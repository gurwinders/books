package com.rajpal.books;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by asus on 26-06-15.
 */
public class KeyService extends Service{
    @Override
    public void onCreate() {


        super.onCreate();
    }



    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
