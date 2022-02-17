package com.example.rewind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.rewind.audio.Boombox;

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
        Boombox.getInstance().play(R.raw.navigation_transition_left);

        ImageButton tutorialButton = view.findViewById(R.id.tutorial_button);
        tutorialButton.setOnClickListener( v-> {
            Navigation.findNavController(view).navigate(R.id.action_introFragment_to_tutorialFragment);
        });

    }
}