package com.tayfun.musicplayer;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongHolder> {

    private ArrayList<SongInfo> mSongs = new ArrayList<SongInfo>();
    private Context context;
    private OnItemClickListener mOnItemClickListener;

    public SongAdapter(Context context, ArrayList<SongInfo> songs) {
        this.context = context;
        this.mSongs = songs;
    }

    public interface OnItemClickListener {
        void onItemClick(LinearLayout linearLayout, View view, SongInfo obj, int position);
        void onItemClick(ImageButton imageButton, View view, SongInfo obj, int position);
    }

    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }


    @Override
    public SongHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View myView = LayoutInflater.from(context).inflate(R.layout.fragment_song_list_element,viewGroup,false);
        return new SongHolder(myView);
    }

    @Override
    public void onBindViewHolder(final SongHolder songHolder, final int i) {
        final SongInfo s = mSongs.get(i);
        songHolder.tvSongName.setText(mSongs.get(i).getSongname());
        songHolder.tvSongArtist.setText(mSongs.get(i).getArtistname());
        songHolder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(songHolder.linearLayout,v, s, i);
                }
            }
        });
        songHolder.listButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    mOnItemClickListener.onItemClick(songHolder.listButton,v, s, i);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mSongs.size();
    }

    public class SongHolder extends RecyclerView.ViewHolder {
        TextView tvSongName,tvSongArtist;
        LinearLayout linearLayout;
        ImageButton listButton;
        public SongHolder(View itemView) {
            super(itemView);
            tvSongName = (TextView) itemView.findViewById(R.id.tvSongName);
            tvSongArtist = (TextView) itemView.findViewById(R.id.tvArtistName);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.song_list_text);
            listButton = (ImageButton) itemView.findViewById(R.id.list_option_button);
        }
    }
}
