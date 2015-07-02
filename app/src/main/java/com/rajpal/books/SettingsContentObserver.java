package com.rajpal.books;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;
import android.util.Log;

public class SettingsContentObserver extends ContentObserver {
    private  int previousMusic;
    private  int previousNotification;
    private  int previousRing;
    private  int previousAlarm;
    private  int previousSystem;
    private  int previousDtmf;
    private  int previousVoice;
    int previousVolume;
    Context context;

    public SettingsContentObserver(Context c, Handler handler) {
        super(handler);
        context = c;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        previousMusic = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        previousSystem = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
        previousAlarm = audio.getStreamVolume(AudioManager.STREAM_ALARM);
        previousNotification = audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        previousRing = audio.getStreamVolume(AudioManager.STREAM_RING);
        previousVoice = audio.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        previousDtmf = audio.getStreamVolume(AudioManager.STREAM_DTMF);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);

        int currentMusic = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
        int currentSystem = audio.getStreamVolume(AudioManager.STREAM_SYSTEM);
        int currentAlarm = audio.getStreamVolume(AudioManager.STREAM_ALARM);
        int currentNotification = audio.getStreamVolume(AudioManager.STREAM_NOTIFICATION);
        int currentRing = audio.getStreamVolume(AudioManager.STREAM_RING);
        int currentVoice = audio.getStreamVolume(AudioManager.STREAM_VOICE_CALL);
        int currentDtmf = audio.getStreamVolume(AudioManager.STREAM_DTMF);
Log.d("-----","--onchange---");
        int delta = 0;
        if (previousAlarm != currentAlarm) {
            delta = previousAlarm - currentAlarm;
            previousAlarm = currentAlarm;
        }
        if (previousDtmf != currentDtmf) {
            delta = previousDtmf - currentDtmf;
            previousDtmf = currentDtmf;
        }
        if (previousMusic != currentMusic) {
            delta = previousMusic - currentMusic;
            previousMusic = currentMusic;
        }
        if (previousNotification != currentNotification) {
            delta = previousNotification - currentNotification;
            previousNotification = currentNotification;
        }
        if (previousRing != currentRing) {
            delta = previousRing - currentRing;
            previousRing = currentRing;
        }
        if (previousSystem != currentSystem) {
            delta = previousSystem - currentSystem;
            previousSystem = currentSystem;
        }
        if (previousVoice != currentVoice) {
            delta = previousVoice - currentVoice;
            previousVoice = currentVoice;
        }
        //  int delta = previousVolume - currentVolume;

        if (delta > 0) {
            Log.d("-------------", "Decreased...!");


        } else if (delta < 0) {

            Log.d("-------", "increased..!");
        }
    }
}