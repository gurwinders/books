package com.rajpal.books;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by asus on 30-06-15.
 */
public class Mreceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"hello",Toast.LENGTH_SHORT).show();
    }
}
