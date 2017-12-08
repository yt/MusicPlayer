package com.tayfun.musicplayer;

import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import java.io.File;

public class EditSongInfoActivity extends AppCompatActivity {
    EditText editName;
    EditText editArtist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_song_info);
        editName = (EditText) findViewById(R.id.name);
        editArtist = (EditText) findViewById(R.id.artist);
        editName.setText(getIntent().getStringExtra("name"));
        editArtist.setText(getIntent().getStringExtra("artist"));


        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC+"!=0";
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    String name = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    String artist = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                    String url = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));

                    if(url.equals( getIntent().getStringExtra("url"))){
                        Log.e("hello","found matching song");
                    }



                }while (cursor.moveToNext());
            }
            cursor.close();
        }
    }
}
