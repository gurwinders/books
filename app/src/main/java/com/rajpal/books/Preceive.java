package com.rajpal.books;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by asus on 02-07-15.
 */
public class Preceive extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.v("#@%@%#", "Power button is pressed.");

        Toast.makeText(context, "power button clicked", Toast.LENGTH_LONG).show();
    }
}
