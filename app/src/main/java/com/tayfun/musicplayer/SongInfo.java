package com.tayfun.musicplayer;

import android.graphics.Bitmap;

public class SongInfo {
    private String Songname;
    private String Artistname;
    private String SongUrl;
    private int Position;

    public SongInfo() {
    }

    public SongInfo(String songname, String artistname, String songUrl,int position) {
        Songname = songname;
        Artistname = artistname;
        SongUrl = songUrl;
        Position = position;
    }

    public int getPosition(){return Position;}

    public int setPosition(int position){return position;}

    public String getSongname() {
        return Songname;
    }

    public void setSongname(String songname) {
        Songname = songname;
    }

    public String getArtistname() {
        return Artistname;
    }

    public void setArtistname(String artistname) {
        Artistname = artistname;
    }

    public String getSongUrl() {
        return SongUrl;
    }

    public void setSongUrl(String songUrl) {
        SongUrl = songUrl;
    }
}