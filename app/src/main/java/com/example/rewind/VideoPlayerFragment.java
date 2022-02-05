package com.example.rewind;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link VideoPlayerFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VideoPlayerFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String MSG = "param1";

    // TODO: Rename and change types of parameters
    private String msg;

    public VideoPlayerFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param msg Parameter 1.

     * @return A new instance of fragment video_player.
     */
    // TODO: Rename and change types and number of parameters
    public static VideoPlayerFragment newInstance(String msg) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        args.putString(MSG, msg);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            msg = getArguments().getString(MSG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_video_player, container, false);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle){
        ImageButton playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(playButton.isActivated()) {
                    playButton.setActivated(false);
                    playButton.setBackgroundResource(R.drawable.rounded_button);
                }
                else {
                    playButton.setActivated(true);
                    playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                }
            }
        });

        ImageButton forwardButton = view.findViewById(R.id.forward_button);
        forwardButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
                public boolean onTouch(View v, MotionEvent event)
                {
                    if (event.getAction() == MotionEvent.ACTION_DOWN){
                        forwardButton.setBackgroundResource(R.drawable.activated_rounded_button);
                    }
                    else if (event.getAction() == MotionEvent.ACTION_UP) {
                        forwardButton.setBackgroundResource(R.drawable.rounded_button);
                    }
                    return false;
                }
        });

        ImageButton speedUpButton = view.findViewById(R.id.speed_up_button);
        speedUpButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    speedUpButton.setBackgroundResource(R.drawable.activated_rounded_button);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    speedUpButton.setBackgroundResource(R.drawable.rounded_button);
                }
                return false;
            }
        });

        ImageButton speedDownButton = view.findViewById(R.id.speed_down_button);
        speedDownButton.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    speedDownButton.setBackgroundResource(R.drawable.activated_rounded_button);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    speedDownButton.setBackgroundResource(R.drawable.rounded_button);
                }
                return false;
            }
        });

        ImageButton backward_Button = view.findViewById(R.id.backward_button);
        backward_Button.setOnTouchListener(new View.OnTouchListener(){
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_DOWN){
                    Log.d("Pressed", "Button pressed");
                    backward_Button.setBackgroundResource(R.drawable.activated_rounded_button);
                }
                else if (event.getAction() == MotionEvent.ACTION_UP) {
                    backward_Button.setBackgroundResource(R.drawable.rounded_button);
                    Log.d("Released", "Button released");
                }
                return false;
            }
        });

        view.findViewById(R.id.return_to_intro_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(VideoPlayerFragment.this)
                        .navigate(R.id.action_videoPlayerFragment_to_introFragment);
            }
        });
    }
}