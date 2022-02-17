package com.example.rewind.tutorial_fragment;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.rewind.R;
import com.example.rewind.VideoPlayerFragment;

public class BookmarkFragmentTutorial extends Fragment {
    private TutorialFragment tf;

    public BookmarkFragmentTutorial(TutorialFragment tf) {
        this.tf = tf;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bookmark_tutorial, container, false);

        view.findViewById(R.id.bookmark_tutorial_right_arrow).setOnClickListener(v->{
            tf.getViewPager2().setCurrentItem(1);
        });

        view.findViewById(R.id.bookmark_tutorial_closer_button).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_tutorialFragment_to_introFragment);
        });
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle) {
    /*
        ImageButton leftArrow = view.findViewById(R.id.bookmark_tutorial_left_arrow);
        leftArrow.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_tutorialFragment_to_introFragment);
        });

        ImageButton rightArrow = view.findViewById(R.id.bookmark_tutorial_right_arrow);
        rightArrow.setOnClickListener(v->{
        //    Navigation.findNavController(view).navigate(R.id.a);
        });

        ImageButton closerButton = view.findViewById(R.id.bookmark_tutorial_closer_button);
        rightArrow.setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_tutorialFragment_to_introFragment);
        });
        */
    }
}