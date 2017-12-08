package com.tayfun.musicplayer;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.session.MediaSession;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import static android.content.Intent.ACTION_HEADSET_PLUG;
import static android.graphics.Color.rgb;

public class MediaNotificationManager extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.example.android.uamp.MUSIC_CHANNEL_ID";

    private static final int NOTIFICATION_ID = 412;
    private static final int REQUEST_CODE = 100;

    public static final String ACTION_PLAY = "play";
    public static final String ACTION_PREV = "prev";
    public static final String ACTION_NEXT = "next";
    public static final String ACTION_REPLAY = "replay";
    public static final String ACTION_SHUFFLE = "shuffle";

    private final MainActivity mService;

    private final NotificationManager mNotificationManager;

    private final PendingIntent mPlayIntent;
    private final PendingIntent mPreviousIntent;
    private final PendingIntent mNextIntent;
    private final PendingIntent mShuffleIntent;
    private final PendingIntent mReplayIntent;

    private int playIcon;
    private MediaSessionCompat.Token mSessionToken;



    public MediaNotificationManager(MainActivity musicActivity) throws RemoteException {

        mService = musicActivity;
        //updateSessionToken();

        mNotificationManager = (NotificationManager) mService.getSystemService(Context.NOTIFICATION_SERVICE);

        String pkg = mService.getPackageName();
        mShuffleIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_SHUFFLE).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPlayIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mPreviousIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_PREV).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mNextIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_NEXT).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);
        mReplayIntent = PendingIntent.getBroadcast(mService, REQUEST_CODE,
                new Intent(ACTION_REPLAY).setPackage(pkg), PendingIntent.FLAG_CANCEL_CURRENT);


        // Cancel all notifications to handle the case where the Service was killed and
        // restarted by the system.
        mNotificationManager.cancelAll();
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.e("hello","pressed "+action);
        switch (action) {
            case ACTION_PLAY:
                mService.playOrPause();
                createNotification();
                break;
            case ACTION_NEXT:
                mService.next();
                createNotification();
                break;
            case ACTION_PREV:
                mService.previous();
                createNotification();
                break;
            case ACTION_REPLAY:
                mService.replayButton.callOnClick();
                createNotification();
                break;
            case ACTION_SHUFFLE:
                Log.e("hello","pressed notification shuffle button");
                mService.shuffleButton.callOnClick();
                createNotification();
                break;
            case ACTION_HEADSET_PLUG:
                int state = intent.getIntExtra("state", -1);
                switch (state) {

                    case 0:
                        mService.stopMusic();
                        createNotification();
                        Log.e("hello", "Headset unplugged");
                        break;
                    case 1:
                        createNotification();
                        Log.e("hello", "Headset plugged");
                        break;
                }
                break;
            default:
                Log.e("hello","default case here");
        }
    }
    public void stopNotification() {
        mNotificationManager.cancelAll();
    }
    //call this when you wanna start
    public void startNotification() {
        Notification notification = createNotification();
        if (notification != null) {
            IntentFilter filter = new IntentFilter();
            filter.addAction(ACTION_REPLAY);
            filter.addAction(ACTION_NEXT);
            filter.addAction(ACTION_PLAY);
            filter.addAction(ACTION_PREV);
            filter.addAction(ACTION_SHUFFLE);
            mService.registerReceiver(this, filter);
        }
    }
    private PendingIntent createContentIntent(String description) {
        Intent openUI = new Intent(mService, MainActivity.class);
        openUI.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        if (description != null) {
            openUI.putExtra("hello", description);
        }
        return PendingIntent.getActivity(mService, REQUEST_CODE, openUI,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }

    public Notification createNotification() {

        final NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(mService, CHANNEL_ID);

        addActions(notificationBuilder);

        notificationBuilder
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(rgb(0,0,139))
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                .setPriority(Notification.PRIORITY_MAX)
                .setContentIntent(createContentIntent("put SongInfo here"))
                .setContentTitle(mService.currentSong.getSongname())
                .setShowWhen(false)
                .setContentText(mService.currentSong.getArtistname());

        notificationBuilder
                .setStyle(new android.support.v4.media.app.NotificationCompat.MediaStyle()
                                // show only play/pause in compact view
                                .setShowActionsInCompactView(1,2,3));

        mNotificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());

        return notificationBuilder.build();
    }


    private void addActions(final NotificationCompat.Builder notificationBuilder) {
        if(mService.isMusicPlaying){
            Log.e("hello","music is playing");
            playIcon = R.drawable.ic_pause_black_24dp;
        }else{
            Log.e("hello","music is not playing");
            playIcon = R.drawable.ic_play_arrow_black_24dp;
        }
        int image = R.drawable.ic_replay_black_24dp;


        notificationBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_replay_black_24dp, ACTION_REPLAY, mReplayIntent)).setColor(rgb( 255, 140, 0));
        notificationBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_previous_black_24dp, ACTION_PREV, mPreviousIntent));
        notificationBuilder.addAction(new NotificationCompat.Action(playIcon, ACTION_PLAY, mPlayIntent));
        notificationBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_skip_next_black_24dp, ACTION_NEXT, mNextIntent));
        notificationBuilder.addAction(new NotificationCompat.Action(R.drawable.ic_shuffle_black_24dp, ACTION_SHUFFLE, mShuffleIntent));

    }

    }

