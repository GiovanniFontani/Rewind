package com.example.rewind;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rewind.audio.Boombox;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class IntroFragment extends Fragment {

    public IntroFragment() {

    }

    public static IntroFragment newInstance() {
        return new IntroFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle){
        view.findViewById(R.id.nav_to_videoplayer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IntroFragment.this)
                        .navigate(R.id.action_introFragment_to_videoPlayerFragment);
                Boombox.getInstance().play(R.raw.navigation_transition_right);
            }
        });
        view.findViewById(R.id.nav_to_bookmarks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IntroFragment.this)
                        .navigate(R.id.action_introFragment_to_bookmarkFragment);
                Boombox.getInstance().play(R.raw.navigation_transition_left);
            }
        });
    }
}