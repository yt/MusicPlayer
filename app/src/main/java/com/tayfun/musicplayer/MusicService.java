package com.tayfun.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;


//A service only responsible for closing the music playback notfication when app stops
public class MusicService extends Service{
    private final IBinder binder = new MyLocalBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {

        MediaNotificationManager m = MainActivity.mediaNotificationManager;
        m.stopNotification();
        stopSelf();
        super.onTaskRemoved(rootIntent);
    }

    public class MyLocalBinder extends Binder {
        MusicService getService(){
            return  MusicService.this;
        }
    }
}
