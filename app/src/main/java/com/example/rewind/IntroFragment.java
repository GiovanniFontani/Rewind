package com.example.rewind;

import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IntroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IntroFragment extends Fragment {

    public IntroFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     *
     * @return A new instance of fragment IntroFragment.
     */

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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_intro, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle){
        view.findViewById(R.id.BottoneProva).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast myToast = Toast.makeText(getActivity(),"Hello Toast!", Toast.LENGTH_SHORT);
                myToast.show();
            }
        });
        view.findViewById(R.id.video_player_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IntroFragment.this)
                        .navigate(R.id.action_introFragment_to_videoPlayerFragment);
                MediaPlayer.create(getContext(), R.raw.navigation_transition_left).start();
            }
        });
        view.findViewById(R.id.nav_to_bookmarks).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(IntroFragment.this)
                        .navigate(R.id.action_introFragment_to_bookmarkFragment);
                MediaPlayer.create(getContext(), R.raw.navigation_transition_right).start();
            }
        });
    }
}