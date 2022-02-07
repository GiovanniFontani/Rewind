package com.example.rewind;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
            // TODO: Rename and change types of parameters
            String msg = getArguments().getString(MSG);
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
        playButton.setOnClickListener(v -> {
            if(playButton.isActivated()) {
                playButton.setActivated(false);
                playButton.setBackgroundResource(R.drawable.rounded_button);
            }
            else {
                playButton.setActivated(true);
                playButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
        });

        ImageButton forwardButton = view.findViewById(R.id.forward_button);
        forwardButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                forwardButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                forwardButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        ImageButton speedUpButton = view.findViewById(R.id.speed_up_button);
        speedUpButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                speedUpButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedUpButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        ImageButton speedDownButton = view.findViewById(R.id.speed_down_button);
        speedDownButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                speedDownButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedDownButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        ImageButton backwardButton = view.findViewById(R.id.backward_button);
        backwardButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("Pressed", "Button pressed");
                backwardButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                backwardButton.setBackgroundResource(R.drawable.rounded_button);
                Log.d("Released", "Button released");
            }
            return false;
        });

        ImageButton backTenButton = view.findViewById(R.id.backwardten_button);
        backTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("Pressed", "Button pressed");
                backTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                backTenButton.setBackgroundResource(R.drawable.rounded_button);
                Log.d("Released", "Button released");
            }
            return false;
        });

        ImageButton forwardTenButton = view.findViewById(R.id.forwardten_button);
        forwardTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("Pressed", "Button pressed");
                forwardTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                forwardTenButton.setBackgroundResource(R.drawable.rounded_button);
                Log.d("Released", "Button released");
            }
            return false;
        });

        ImageButton addBookmarkButton = view.findViewById(R.id.addbookmark_button);
        addBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("Pressed", "Button pressed");
                addBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                addBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
                Log.d("Released", "Button released");
            }
            return false;
        });

        ImageButton nextBookmarkButton = view.findViewById(R.id.next_bookmark_button);
        nextBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("Pressed", "Button pressed");
                nextBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                nextBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
                Log.d("Released", "Button released");
            }
            return false;
        });

        ImageButton previousBookmarkButton = view.findViewById(R.id.previous_bookmark_button);
        previousBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                Log.d("Pressed", "Button pressed");
                previousBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                previousBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
                Log.d("Released", "Button released");
            }
            return false;
        });


        Button bookmarksViewButton = view.findViewById(R.id.bookmarks_view_button);
        bookmarksViewButton.setOnClickListener(v -> {
            if (bookmarksViewButton.getVisibility() == View.VISIBLE) {
                ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setEnabled(false);
                }
                view.findViewById(R.id.inner_videoplayer).setAlpha((float)0.2);
                view.findViewById(R.id.recyclerview_closer_button).setVisibility(View.VISIBLE);
                view.findViewById(R.id.recyclerView).setVisibility(View.VISIBLE);
            }
        });

        ImageButton recyclerViewCloserButton = view.findViewById(R.id.recyclerview_closer_button);
        recyclerViewCloserButton.setOnClickListener(v -> {
            if (recyclerViewCloserButton.getVisibility() == View.VISIBLE){
                ConstraintLayout layout = (ConstraintLayout) view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setEnabled(true);
                }
                view.findViewById(R.id.inner_videoplayer).setAlpha((float)1);
                view.findViewById(R.id.recyclerview_closer_button).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.recyclerView).setVisibility(View.INVISIBLE);
            }
        });





        view.findViewById(R.id.return_to_intro_button).setOnClickListener(view12 -> NavHostFragment.findNavController(VideoPlayerFragment.this)
                .navigate(R.id.action_videoPlayerFragment_to_introFragment));
    }
}