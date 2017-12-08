package com.tayfun.musicplayer;
import com.tayfun.musicplayer.MusicService.MyLocalBinder;
import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Toast;
import java.util.ArrayList;

import static android.graphics.Color.rgb;

public class MainActivity extends MusicPlayerActivity {


    private RecyclerView recyclerView;
    private SongAdapter songAdapter;
    private Thread t;
    private ArrayList<SongInfo> allSongs;

    private MusicService mService;
    boolean isBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initilizeToolbar();

        Intent intent = new Intent(this,MusicService.class);
        bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
        startService(intent);

        miniFragment = (PlaybackControlsFragment) getFragmentManager().findFragmentById(R.id.fragment);
        fullFragment = (FullPlaybackControlsFragment) getFragmentManager().findFragmentById(R.id.fragment2);
        hideFullPlayback();
        initButtons();
        initSongs();
        loadSongs();

        if(getData().equals("Default")){
            setCurrentSong(mSongs.get(0));
        }


        t = new runThread();
        t.start();



        try {
            mediaNotificationManager = new MediaNotificationManager(this);
            mediaNotificationManager.startNotification();
        } catch (RemoteException e) {
            Log.e("hello","media notification could'nt create");
        }
    }
    public void onResume() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_HEADSET_PLUG);
        registerReceiver(mediaNotificationManager, filter);
        super.onResume();
    }
    @Override
    public void onBackPressed() {
    }
    @Override
    protected void initButtons(){
        mSongs = new ArrayList<SongInfo>();
        previousSongs = new ArrayList<SongInfo>();
        miniPlayButton = miniFragment.getPlayButton();
        fullPlayButton = fullFragment.getPlayButton();
        miniPreviousButton = miniFragment.getPreviousButton();
        fullPreviousButton = fullFragment.getPreviousButton();
        miniNextButton = miniFragment.getNextButton();
        fullNextButton = fullFragment.getNextButton();
        miniPlaybackTextView = miniFragment.getPlaybackTextView();
        fullPlaybackTextView = fullFragment.getPlaybackTextView();
        shrinkButton = fullFragment.getShrinkButton();
        songArtistView = fullFragment.getPlaybackArtistName();
        replayButton = fullFragment.getReplayButton();
        shuffleButton = fullFragment.getShuffleButton();
        albumArt = fullFragment.getAlbumArt();
        miniPlaybackTextView.setOnClickListener(new View.OnClickListener() {
            //TODO change this to swipe up insted of onClick
            @Override
            public void onClick(View view) {
                hideMiniPlayback();
                showFullPlayback();
            }
        });
        miniPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPause();
            }
        });
        fullPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playOrPause();
            }
        });
        shuffleButton.setColorFilter(rgb(0, 191, 255));//blue
        shuffleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRandomPalying){
                    Log.e("hello","Random play disabled.");
                    shuffleButton.setColorFilter(rgb(0, 191, 255));//blue
                    isRandomPalying = false;
                }else{
                    Log.e("hello","Random play enabled.");
                    shuffleButton.setColorFilter(rgb( 255, 140, 0));//dark orange
                    isRandomPalying = true;
                }
            }
        });
        shrinkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideFullPlayback();
                showMiniPlayback();
            }
        });
        replayButton.setColorFilter(rgb(0, 191, 255));//blue
        replayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isRepeating){
                    isRepeating = false;
                    replayButton.setColorFilter(rgb(0, 191, 255));//blue
                    Log.e("hello","Repeating disabled.");
                }else{
                    isRepeating = true;
                    replayButton.setColorFilter(rgb( 255, 140, 0));//dark orange
                    Log.e("hello","Repeating enabled.");
                }
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        seekBar = (SeekBar) findViewById(R.id.seekBar1);
        songAdapter = new SongAdapter(this,mSongs);
    }
    @Override
    protected void initSongs(){
        recyclerView.setAdapter(songAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        songAdapter.setOnItemClickListener(new SongAdapter.OnItemClickListener() {
             @Override
             public void onItemClick(LinearLayout linearLayout, View view, SongInfo obj, int position) {
                playSongByPosition(mSongs,position);
                }
            @Override
            public void onItemClick(final ImageButton imageButton, View view, final SongInfo obj, int position) {
                //functionality of down arrow on list view
                imageButton.setColorFilter(rgb( 255, 140, 0));//dark orange
                PopupMenu pm = new PopupMenu(MainActivity.this, view);
                pm.getMenuInflater().inflate(R.menu.list_menu, pm.getMenu());

                pm.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())   {
                            case R.id.edit_info:

                                break;
                            default:
                                break;
                        }
                        return false;
                    }
                });
                pm.setOnDismissListener(new PopupMenu.OnDismissListener() {
                    @Override
                    public void onDismiss(PopupMenu popupMenu) {
                        imageButton.setColorFilter(rgb(0, 191, 255));//blue
                    }
                });
                pm.show();

            }}
        );
        checkUserPermission();
    }

    public void onPreviousButtonClick(View view) {
        previous();
    }
    public void onNextButtonClick(View view) {
        next();
    }
    private void checkUserPermission(){
        if(Build.VERSION.SDK_INT>=23){
            if(ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED){
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},123);
                return;
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.option_menu, menu);
        MenuItem item = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener(){
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                if(currentFragment == fullFragment){
                    hideFullPlayback();
                    showMiniPlayback();
                }
                newText = newText.toLowerCase();
                ArrayList<SongInfo> searchedSongs = new ArrayList<SongInfo>();
                for(SongInfo songObj: allSongs){
                    String name = songObj.getSongname().toLowerCase();
                    if(name.contains(newText)) {
                        searchedSongs.add(songObj);
                    }
                }
                mSongs = searchedSongs;
                songAdapter = new SongAdapter(MainActivity.this,mSongs);
                initSongs();
                return true;
            }
        });
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 123:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    loadSongs();
                }else{
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    checkUserPermission();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }
    //this method should only call once onCreate
    private void loadSongs(){
        int position = -1;

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
                    //TODO add album getter here, update songInfo and albumImage imageView

                    SongInfo s = new SongInfo(name,artist,url,++position);
                    if(url.equals(getData()))
                        setCurrentSong(s);

                    mSongs.add(position,s);

                }while (cursor.moveToNext());
            }
            cursor.close();
            songAdapter = new SongAdapter(MainActivity.this,mSongs);
            allSongs = mSongs;
        }

    }
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MyLocalBinder binder = (MyLocalBinder) iBinder;
            mService = binder.getService();
            isBound = true;

        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isBound = false;
        }
    };

}
