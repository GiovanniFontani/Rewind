package com.example.rewind.tutorial_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.rewind.R;

public class VideoPlayerFragmentTutorial extends Fragment {

    public VideoPlayerFragmentTutorial() {


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player_tutorial, container, false);

        view.findViewById(R.id.video_player_tutorial_left_arrow).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_videoPlayerFragmentTutorial_to_bookmarkFragmentTutorial);
        });
        view.findViewById(R.id.video_player_tutorial_right_arrow).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_videoPlayerFragmentTutorial_to_newBookmarkFragmentTutorial);
        });
        view.findViewById(R.id.video_player_tutorial_closer_button).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_videoPlayerFragmentTutorial_to_introFragment);

        });
        return view;
    }

}