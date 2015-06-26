package com.rajpal.books;

import android.app.Application;

import com.facebook.FacebookSdk;

/**
 * Created by asus on 26-06-15.
 */
public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FacebookSdk.sdkInitialize(getApplicationContext());
    }
}