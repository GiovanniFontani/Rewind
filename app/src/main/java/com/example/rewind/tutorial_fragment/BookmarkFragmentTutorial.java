package com.example.rewind.tutorial_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rewind.R;
import com.example.rewind.VideoPlayerFragment;

public class BookmarkFragmentTutorial extends Fragment {

    public BookmarkFragmentTutorial() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_tutorial, container, false);

        view.findViewById(R.id.bookmark_tutorial_right_arrow).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_bookmarkFragmentTutorial_to_videoPlayerFragmentTutorial);
        });

        view.findViewById(R.id.bookmark_tutorial_closer_button).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_bookmarkFragmentTutorial_to_introFragment);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle) {

    }
}