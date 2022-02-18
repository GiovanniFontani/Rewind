package com.example.rewind;

import android.annotation.SuppressLint;

import android.app.Activity;
import android.content.Intent;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import com.example.rewind.audio.Boombox;
import com.example.rewind.bookmarking.NewBookmarkActivity;
import com.example.rewind.bookmarking.VideoBookmarkListAdapter;
import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.BookmarkViewModel;
import com.example.rewind.bookmarking.database.DateGetter;
import com.example.rewind.button.ConnectionStatusButton;

public class VideoPlayerFragment extends Fragment {
    private static final String MSG = "param1";
    private BookmarkViewModel bookmarkViewModel;
    public VideoPlayerFragment() {

    }

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
            String msg = getArguments().getString(MSG);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        RecyclerView recycler = view.findViewById(R.id.bookmarks_in_videoPlayer);
        if (recycler != null) {
            Context context = recycler.getContext();
            recycler.setLayoutManager(new LinearLayoutManager(context));
            final VideoBookmarkListAdapter adapter = new VideoBookmarkListAdapter(new VideoBookmarkListAdapter.BookmarkDiff());
            recycler.setAdapter(adapter);
            bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);
            String videoName = ((TextView) view.findViewById(R.id.video_player_name)).getText().toString();
            bookmarkViewModel.getByVideoName(videoName).observe(getViewLifecycleOwner(), adapter::submitList);
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle){

        ImageButton playButton = view.findViewById(R.id.play_button);
        playButton.setOnClickListener(v -> {
            Boombox.getInstance().play(R.raw.ui_tap1);
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
                Boombox.getInstance().play(R.raw.ui_tap1);
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
                Boombox.getInstance().play(R.raw.ui_tap1);
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
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedDownButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        ImageButton backwardButton = view.findViewById(R.id.backward_button);
        backwardButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                backwardButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                backwardButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        ImageButton backTenButton = view.findViewById(R.id.backwardten_button);
        backTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                backTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                backTenButton.setBackgroundResource(R.drawable.rounded_button);

            }
            return false;
        });

        ImageButton forwardTenButton = view.findViewById(R.id.forwardten_button);
        forwardTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                forwardTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                forwardTenButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });



        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String video_name = ((TextView)view.findViewById(R.id.video_player_name)).getText().toString();
                        String bookmarkName = data.getStringExtra(NewBookmarkActivity.EXTRA_REPLY);
                        String documentName = data.getStringExtra("documentName");
                        Uri documentPath;
                        int documentPage;
                        if (documentName.equals("null")) {
                            documentName = null;
                            documentPath = null;
                            documentPage = -1;
                        }else
                        {
                            documentPath = Uri.parse(data.getStringExtra("documentPath"));
                            documentPage = Integer.parseInt(data.getStringExtra("documentPage"));
                        }
                        //TODO :fix local time
                        Bookmark bookmark = new Bookmark(bookmarkName, documentName, DateGetter.getLocalDateTime(), video_name, documentPath, documentPage, DateGetter.stringToLocalTime(((TextView)view.findViewById(R.id.current_time_text)).getText().toString()));
                        bookmarkViewModel.insert(bookmark);
                    }
                });

        ImageButton addBookmarkButton = view.findViewById(R.id.addbookmark_button);
        addBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                addBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.save_bookmark_vp);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                addBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
                Intent intent = new Intent(this.getActivity(), NewBookmarkActivity.class);
                launcher.launch(intent);
            }
            return false;
        });

        ImageButton nextBookmarkButton = view.findViewById(R.id.next_bookmark_button);
        nextBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                nextBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.bookmark_forward_vp);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                nextBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        ImageButton previousBookmarkButton = view.findViewById(R.id.previous_bookmark_button);
        previousBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                previousBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.bookmark_backward_vp);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                previousBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });


        Button bookmarksViewButton = view.findViewById(R.id.bookmarks_view_button);
        bookmarksViewButton.setOnClickListener(v -> {
            if (bookmarksViewButton.getVisibility() == View.VISIBLE) {
                ConstraintLayout layout = view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setEnabled(false);
                }
                view.findViewById(R.id.recyclerview_closer_button).setVisibility(View.VISIBLE);
                view.findViewById(R.id.bookmarks_in_videoPlayer).setVisibility(View.VISIBLE);
                Boombox.getInstance().play(R.raw.navigation_transition_left);
            }
        });


        View connectingButton;
        Button disconnectButton = view.findViewById(R.id.disconnect_button);
        connectingButton = view.findViewById(R.id.connecting_status_button);
        ConnectionStatusButton connectionStatusButton = new ConnectionStatusButton( view.getContext(),view);
        connectingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (connectionStatusButton.getConnectionStatus() == false) {
                    connectionStatusButton.buttonConnecting();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connectionStatusButton.buttonConnected();
                            disconnectButton.setVisibility(View.VISIBLE);
                        }
                    }, 5000);
                }
            }
        });

        disconnectButton.setOnClickListener(v -> {
            connectionStatusButton.buttonDisconnect();
            disconnectButton.setVisibility(View.INVISIBLE);
        });


        ImageButton recyclerViewCloserButton = view.findViewById(R.id.recyclerview_closer_button);
        recyclerViewCloserButton.setOnClickListener(v -> {
            if (recyclerViewCloserButton.getVisibility() == View.VISIBLE){
                ConstraintLayout layout = view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setEnabled(true);
                }
                view.findViewById(R.id.recyclerview_closer_button).setVisibility(View.INVISIBLE);
                view.findViewById(R.id.bookmarks_in_videoPlayer).setVisibility(View.INVISIBLE);
                Boombox.getInstance().play(R.raw.navigation_transition_right);
            }
        });
        Boombox.getInstance().play(R.raw.navigation_transition_right);
    }
}

//TODO: Modify deconnect when vlc works...