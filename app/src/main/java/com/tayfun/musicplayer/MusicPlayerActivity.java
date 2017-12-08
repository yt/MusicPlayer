package com.tayfun.musicplayer;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;

/**Abstract class that holds necessary tools for mediaplayer utilities and bottom playback utilities
 *
 */

public abstract class MusicPlayerActivity extends ActionBarCastActivity {
    protected boolean isMusicPlaying = false;
    protected boolean isTouched = false;
    protected ArrayList<SongInfo> mSongs ;
    protected ArrayList<SongInfo> previousSongs ;

    protected ImageButton miniPlayButton;
    protected ImageButton fullPlayButton;
    protected ImageButton miniPreviousButton;
    protected ImageButton fullPreviousButton;
    protected ImageButton miniNextButton;
    protected ImageButton fullNextButton;
    protected ImageButton shrinkButton;
    protected ImageButton replayButton;
    protected ImageButton shuffleButton;
    protected TextView miniPlaybackTextView;
    protected TextView fullPlaybackTextView;
    protected TextView songArtistView;

    protected ImageView albumArt;

    protected MediaPlayer mediaPlayer;
    protected SeekBar seekBar;

    protected Handler myHandler = new Handler();
    protected SongInfo currentSong = null;
    protected Fragment currentFragment;
    protected FullPlaybackControlsFragment fullFragment;
    protected PlaybackControlsFragment miniFragment;


    public static MediaNotificationManager mediaNotificationManager;

    protected boolean isRepeating = false;
    protected boolean isRandomPalying = false;

    protected abstract void initButtons();//setup buttons
    protected abstract void initSongs();//setup recyclerView and

    protected void playOrPause(){
        if(isMusicPlaying){
            Log.e("hello","Pressed pause button");
            stopMusic();
        }else{
            Log.e("hello","Pressed play button");
            resume();
        }
        mediaNotificationManager.createNotification(); //update notification
    }
    protected void resume(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.start();
                    miniPlayButton.setImageResource(R.drawable.ic_pause_black_48dp);
                    miniPlayButton.setContentDescription("Pause");
                    fullPlayButton.setImageResource(R.drawable.ic_pause_black_48dp);
                    fullPlayButton.setContentDescription("Pause");
                    Log.e("hello","Resuming music");
                }catch (Exception e){
                    Log.e("hello","exception when resuming music");
                }
            }
        };
        isMusicPlaying = true;
        myHandler.postDelayed(runnable,100);
    }
    protected void next(){
            //TODO smashing buttons cause a bug that makes multiple songs play
            if(isRandomPalying){
                playSongByPosition(mSongs,(int)(Math.random()* mSongs.size()));
            }else{
                if(currentSong.getPosition()+1<mSongs.size())
                    playSongByPosition( mSongs,currentSong.getPosition()+1);
                else
                    playSongByPosition( mSongs,0);
            }
            isMusicPlaying = true;
            mediaNotificationManager.createNotification(); //update notification
    }
    protected void previous(){
        //TODO make this place previous song instead of -1 position
        if(previousSongs.size()>0){
            playSongByPosition(previousSongs,previousSongs.size()-1);
            previousSongs.remove(previousSongs.size()-1);//same line twice because playSongByPoistion adds 1 music to the list too
            previousSongs.remove(previousSongs.size()-1);
            //Log.e("hello","previous song is "+previousSongs.get(previousSongs.size()));
            Log.e("hello","previous song is "+previousSongs.size());
        }else if(currentSong.getPosition()-1 <0){
            playSongByPosition( mSongs, mSongs.size()-1);
        }else{
            playSongByPosition(mSongs,currentSong.getPosition()-1);
        }
        isMusicPlaying = true;
        mediaNotificationManager.createNotification(); //update notification
    }

    protected void hideMiniPlayback(){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(miniFragment)
                .commit();
    }
    protected void showMiniPlayback(){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(miniFragment)
                .commit();
        currentFragment = (android.app.Fragment) fullFragment;
    }
    protected void hideFullPlayback(){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .hide(fullFragment)
                .commit();
    }
    protected void showFullPlayback(){
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                .show(fullFragment)
                .commit();
        currentFragment = (android.app.Fragment) fullFragment;
    }
    protected void playSongByPosition(ArrayList<SongInfo> mSongs, final int position){
        Log.e("hello",""+position);
        final SongInfo info = mSongs.get(position);
        if(isMusicPlaying){
            stopMusic();
        }
        miniPlaybackTextView.setText(info.getSongname());
        fullPlaybackTextView.setText(info.getSongname());
        songArtistView.setText(info.getArtistname());

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(info.getSongUrl());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            mp.start();
                            seekBar.setProgress(0);
                            seekBar.setMax(mediaPlayer.getDuration());
                        }
                    });
                    isMusicPlaying = true;
                    miniPlayButton.setContentDescription("Pause");
                    miniPlayButton.setImageResource(R.drawable.ic_pause_black_48dp);
                    fullPlayButton.setContentDescription("Pause");
                    fullPlayButton.setImageResource(R.drawable.ic_pause_black_48dp);
                }catch (Exception e){}
            }
        };
        myHandler.post(runnable);
        //myHandler.postDelayed(runnable,1);
        previousSongs.add(currentSong);
        currentSong = mSongs.get(position);
        isMusicPlaying = true;
        mediaNotificationManager.createNotification();
        saveInfo();
    }

    public void setCurrentSong(final SongInfo currentSong) {
        miniPlaybackTextView.setText(currentSong.getSongname());
        fullPlaybackTextView.setText(currentSong.getSongname());
        songArtistView.setText(currentSong.getArtistname());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer = new MediaPlayer();
                    mediaPlayer.setDataSource(currentSong.getSongUrl());
                    mediaPlayer.prepareAsync();
                    mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                        @Override
                        public void onPrepared(MediaPlayer mp) {
                            seekBar.setProgress(0);
                            seekBar.setMax(mediaPlayer.getDuration());
                        }
                    });
                }catch (Exception e){}
            }
        };
        myHandler.postDelayed(runnable,100);
        this.currentSong = currentSong;
    }


    protected void stopMusic(){
        if(!isMusicPlaying){
        }else {
            mediaPlayer.pause();
            miniPlayButton.setContentDescription("Play");
            miniPlayButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);
            fullPlayButton.setContentDescription("Play");
            fullPlayButton.setImageResource(R.drawable.ic_play_arrow_black_48dp);

            isMusicPlaying = false;
        }
    }

    protected class runThread extends Thread {
        //seekBar thread
        //includes functionality for when music reaches to end or user interactions with seekbar
        @Override
        public void run() {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (mediaPlayer != null) {
                    seekBar.post(new Runnable() {
                        @Override
                        public void run() {
                            seekBar.setProgress(mediaPlayer.getCurrentPosition());
                            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
                            {
                                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
                                {
                                    if(fromUser){
                                        mediaPlayer.seekTo(progress);
                                        seekBar.setProgress(progress);}
                                    if(progress+2000>=seekBar.getMax()){
                                        //TODO find more efficent way here
                                        Log.e("hello","Song reached to end");
                                        //mediaPlayer.stop();
                                        if(isRepeating){
                                            Log.e("hello","repeating");
                                            playSongByPosition(mSongs,currentSong.getPosition());
                                        }else{
                                            Log.e("hello","song ended going to next");
                                            next();
                                        }
                                        mediaNotificationManager.createNotification();  //update notification
                                    }//TODO handle the error: music gets stuck
                                }

                                @Override
                                public void onStartTrackingTouch(SeekBar seekBar) {

                                }
                                @Override
                                public void onStopTrackingTouch(SeekBar seekBar) {

                                }
                            });

                        }
                    });
                }
            }
        }
    }
    public void saveInfo(){
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusic",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("LastSongUrl",currentSong.getSongUrl());
        editor.apply();
    }
    public String getData(){
        SharedPreferences sharedPreferences = getSharedPreferences("lastMusic",Context.MODE_PRIVATE);
        return  sharedPreferences.getString("LastSongUrl","Default");
    }
}
