package com.tayfun.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by tayfun on 18-Nov-17.
 */

public class FullPlaybackControlsFragment extends Fragment {
    private ImageButton playButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private ImageButton shuffleButton;
    private ImageButton replayButton;
    private ImageButton shrinkButton;
    private TextView playbackArtistName;
    private TextView playbackTextView;
    private ImageView albumArt;

    public ImageButton getShrinkButton() {
        return shrinkButton;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_full_playback_controls, container, false);
        playButton = (ImageButton) rootView.findViewById(R.id.playback_play_button);
        previousButton = (ImageButton) rootView.findViewById(R.id.previousButton);
        nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        playbackTextView =(TextView)rootView.findViewById(R.id.playback_title);
        playbackArtistName = (TextView)rootView.findViewById(R.id.songArtist);
        replayButton = (ImageButton) rootView.findViewById(R.id.replayButton);
        shuffleButton = (ImageButton) rootView.findViewById(R.id.shuffleButton);
        shrinkButton = (ImageButton) rootView.findViewById(R.id.close_full_screen_button);
        albumArt = (ImageView) rootView.findViewById(R.id.albumImage);


        return rootView;
    }
    public ImageButton getPlayButton() {
        return playButton;
    }

    public ImageButton getPreviousButton() {
        return previousButton;
    }

    public ImageButton getNextButton() {
        return nextButton;
    }

    public TextView getPlaybackArtistName() {
        return playbackArtistName;
    }

    public ImageButton getShuffleButton() {
        return shuffleButton;
    }

    public ImageButton getReplayButton() {
        return replayButton;
    }

    public TextView getPlaybackTextView() {
        return playbackTextView;
    }

    public ImageView getAlbumArt() {
        return albumArt;
    }

    public void setAlbumArt(ImageView albumArt) {
        this.albumArt = albumArt;
    }
}
