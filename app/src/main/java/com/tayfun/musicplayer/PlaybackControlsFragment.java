package com.tayfun.musicplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;


public class PlaybackControlsFragment extends Fragment {
    private ImageButton playButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private TextView playbackTextView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_playback_controls, container, false);
        playButton = (ImageButton) rootView.findViewById(R.id.playback_play_button);
        previousButton = (ImageButton) rootView.findViewById(R.id.previousButton);
        nextButton = (ImageButton) rootView.findViewById(R.id.nextButton);
        playbackTextView =(TextView)rootView.findViewById(R.id.playback_title);
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

    public TextView getPlaybackTextView() {
        return playbackTextView;
    }
}
