package com.example.rewind.tutorial_fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.rewind.R;

public class NewBookmarkFragmentTutorial extends Fragment {

    public NewBookmarkFragmentTutorial() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_bookmark_tutorial, container, false);

        view.findViewById(R.id.new_bookmark_tutorial_left_arrow).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_newBookmarkFragmentTutorial_to_videoPlayerFragmentTutorial);
        });
        view.findViewById(R.id.new_bookmark_tutorial_finish_button).setOnClickListener(v->{
            Navigation.findNavController(view).navigate(R.id.action_newBookmarkFragmentTutorial_to_introFragment);
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle) {

    }

}