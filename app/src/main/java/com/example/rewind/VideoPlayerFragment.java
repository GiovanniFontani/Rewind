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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.rewind.audio.Boombox;
import com.example.rewind.bookmarking.NewBookmarkActivity;
import com.example.rewind.bookmarking.VideoBookmarkListAdapter;
import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.BookmarkViewModel;
import com.example.rewind.bookmarking.database.DateGetter;
import com.example.rewind.button.ConnectionStatusButton;
import com.example.rewind.connection.Connection;
import com.example.rewind.connection.RepeatListener;
import com.google.android.material.textfield.TextInputLayout;

import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

public class VideoPlayerFragment extends Fragment {
    private BookmarkViewModel bookmarkViewModel;
    private ImageButton playButton;
    private ImageButton forwardButton;
    private ImageButton speedUpButton;
    private ImageButton speedDownButton;
    private ImageButton backwardButton;
    private ImageButton backTenButton;
    private ImageButton forwardTenButton;
    private ImageButton addBookmarkButton;
    private ImageButton nextBookmarkButton;
    private ImageButton recyclerViewCloserButton;
    private ImageButton previousBookmarkButton;
    private Button bookmarksViewButton;
    private Button disconnectButton;
    private SeekBar seekbar;
    private View connectingButton;
    private VideoBookmarkListAdapter adapter;
    private ConnectionStatusButton connectionStatusButton;
    ActivityResultLauncher<Intent> launcherNewBookmarkActivity;
    private TextView video_name;
    private  Connection connection;

    public VideoPlayerFragment(){

    }

    public static VideoPlayerFragment newInstance(String msg) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_video_player, container, false);
        RecyclerView recycler = view.findViewById(R.id.bookmarks_in_videoPlayer);
        if (recycler != null) {
            Context context = recycler.getContext();
            recycler.setLayoutManager(new LinearLayoutManager(context));
            adapter = new VideoBookmarkListAdapter(new VideoBookmarkListAdapter.BookmarkDiff());
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
        playButton = view.findViewById(R.id.play_button);
        forwardButton = view.findViewById(R.id.forward_button);
        speedUpButton = view.findViewById(R.id.speed_up_button);
        speedDownButton = view.findViewById(R.id.speed_down_button);
        backwardButton = view.findViewById(R.id.backward_button);
        backTenButton = view.findViewById(R.id.backwardten_button);
        forwardTenButton = view.findViewById(R.id.forwardten_button);
        addBookmarkButton = view.findViewById(R.id.addbookmark_button);
        nextBookmarkButton = view.findViewById(R.id.next_bookmark_button);
        recyclerViewCloserButton = view.findViewById(R.id.recyclerview_closer_button);
        previousBookmarkButton = view.findViewById(R.id.previous_bookmark_button);
        bookmarksViewButton = view.findViewById(R.id.bookmarks_view_button);
        disconnectButton = view.findViewById(R.id.disconnect_button);
        connectingButton = view.findViewById(R.id.connecting_status_button);
        seekbar = view.findViewById(R.id.video_seekbar);
        video_name = view.findViewById(R.id.video_player_name);
        connectionStatusButton = new ConnectionStatusButton(view.getContext(),view);
        connection = new Connection(view);
        playButton.setOnClickListener(v -> {
            Boombox.getInstance().play(R.raw.ui_tap1);
            if(connection.isConnected()) {
                if(playButton.isActivated()) {
                    playButton.setActivated(false);
                    playButton.setBackgroundResource(R.drawable.rounded_button);
                }
                else {
                    playButton.setActivated(true);
                    playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                }
                connection.play_pause();
            }
        });

        forwardButton.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connection.isConnected()) {
                    connection.moveTo(String.valueOf(seekbar.getProgress() +5));
                }
            }
        }));

        backwardButton.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connection.isConnected()) {
                    connection.moveTo(String.valueOf(seekbar.getProgress() - 5));
                }
            }
        }));

        speedUpButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                speedUpButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.setSpeed(+0.25);
                }
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedUpButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        speedDownButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                speedDownButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.setSpeed(-0.25);
                }
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedDownButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        backTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                backTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.moveTo("-10");
                }
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                backTenButton.setBackgroundResource(R.drawable.rounded_button);

            }
            return false;
        });

        forwardTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                forwardTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.moveTo("+10");
                }
                Boombox.getInstance().play(R.raw.ui_tap1);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                forwardTenButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        launcherNewBookmarkActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
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
                        Bookmark bookmark = new Bookmark(bookmarkName, documentName, DateGetter.getLocalDateTime(), video_name, documentPath, documentPage, DateGetter.stringToLocalTime(((TextView)view.findViewById(R.id.current_time_text)).getText().toString()));
                        bookmarkViewModel.insert(bookmark);
                    }
                });

        addBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                addBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                Boombox.getInstance().play(R.raw.save_bookmark_vp);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                addBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
                if(connection.isConnected()) {
                    Intent intent = new Intent(this.getActivity(), NewBookmarkActivity.class);
                    launcherNewBookmarkActivity.launch(intent);
                }
            }
            return false;
        });


        nextBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                nextBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    LocalTime currentTime = DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.current_time_text)).getText().toString());
                    LocalTime target= DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.total_time_text)).getText().toString());
                    LocalTime temp = DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.total_time_text)).getText().toString());
                    for(Bookmark bookmark : adapter.getCurrentList()){
                        if(currentTime.isBefore(bookmark.videoTime) && temp.isAfter(bookmark.videoTime)){
                            temp = bookmark.videoTime;
                        }
                    }
                    if(target.isAfter(temp)){
                        target = temp;
                    }
                    String[] string_target =DateGetter.LocalTimeToString(target).split(":");
                    int seconds = Integer.parseInt(string_target[2]) + Integer.parseInt(string_target[1])*60 + Integer.parseInt(string_target[0])*3600;
                    connection.moveTo(String.valueOf(seconds));
                }
                Boombox.getInstance().play(R.raw.bookmark_forward_vp);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                nextBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });


        previousBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN){
                previousBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    LocalTime currentTime = DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.current_time_text)).getText().toString());
                    LocalTime target= DateGetter.stringToLocalTime("00:00:00");
                    LocalTime temp = DateGetter.stringToLocalTime("00:00:00");
                    for(Bookmark bookmark : adapter.getCurrentList()){
                        if(currentTime.isAfter(bookmark.videoTime) && temp.isBefore(bookmark.videoTime)){
                            temp = bookmark.videoTime;
                        }
                    }
                    if(target.isBefore(temp)){
                        target = temp;
                    }
                    String[] string_target =DateGetter.LocalTimeToString(target).split(":");
                    int seconds = Integer.parseInt(string_target[2]) + Integer.parseInt(string_target[1])*60 + Integer.parseInt(string_target[0])*3600;
                    connection.moveTo(String.valueOf(seconds));
                }
                Boombox.getInstance().play(R.raw.bookmark_backward_vp);
            }
            else if (event.getAction() == MotionEvent.ACTION_UP) {
                previousBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        bookmarksViewButton.setOnClickListener(v -> {
            String videoName = ((TextView) video_name).getText().toString();
            bookmarkViewModel.getByVideoName(videoName).observe(getViewLifecycleOwner(), adapter::submitList);
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

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int progress = seekbar.getProgress();
                if(connection.isConnected()){
                    connection.moveTo(String.valueOf(progress));
                }
            }
        });

        connectingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionStatusButton.getConnectionStatus()) {
                    connection.start();
                    connectionStatusButton.buttonConnecting();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if(!playButton.isActivated() && connection.isPlaying()) {
                                playButton.setActivated(true);
                                playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                            }
                            connectionStatusButton.buttonConnected();
                            disconnectButton.setVisibility(View.VISIBLE);

                        }
                    }, 1000);
                }
            }
        });

        video_name.addTextChangedListener(new TextWatcher() {
            String title;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title = video_name.getText().toString();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!title.equals(video_name.getText().toString())){
                    String videoName = ((TextView) view.findViewById(R.id.video_player_name)).getText().toString();
                    bookmarkViewModel.getByVideoName(videoName).observe(getViewLifecycleOwner(), adapter::submitList);
                }
            }
        });

        disconnectButton.setOnClickListener(v -> {
            connection.stop();
            if(playButton.isActivated()) {
                playButton.setActivated(false);
                playButton.setBackgroundResource(R.drawable.rounded_button);
            }
            connectionStatusButton.buttonDisconnect();
            disconnectButton.setVisibility(View.INVISIBLE);
        });


        recyclerViewCloserButton.setOnClickListener(v -> {
            if (recyclerViewCloserButton.getVisibility() == View.VISIBLE){
                ConstraintLayout layout = view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++){
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

//TODO: solve landscape problem on switch in tutorial section
//TODO: make tutorial <- last thing
//TODO: boombox issues