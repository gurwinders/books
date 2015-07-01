package com.rajpal.books;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

public class SettingsContentObserver extends ContentObserver {
    int previousVolume;
    Context context;

    public SettingsContentObserver(Context c, Handler handler) {
        super(handler);
        context=c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int jj = audio.getRingerMode();
        previousVolume = audio.getStreamVolume(jj);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int jj = audio.getRingerMode();

        int currentVolume = audio.getStreamVolume(jj);

        int delta=previousVolume-currentVolume;

        if(delta>0)
        {
            Log.d("-------","Ściszył!");
            previousVolume=currentVolume;
        }
        else if(delta<0)
        {
            Log.d("-------------","Zrobił głośniej!");
            previousVolume=currentVolume;
        }
    }
}