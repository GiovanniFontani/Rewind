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
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rewind.audio.Boombox;
import com.example.rewind.bookmarking.NewBookmarkActivity;
import com.example.rewind.bookmarking.VideoBookmarkListAdapter;
import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.BookmarkViewModel;
import com.example.rewind.bookmarking.database.DateGetter;
import com.example.rewind.button.ConnectionStatusButton;
import com.example.rewind.connection.Connection;

import java.util.HashMap;
import java.util.Map;

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
    private View connectingButton;
    private ConnectionStatusButton connectionStatusButton;
    ActivityResultLauncher<Intent> launcherNewBookmarkActivity;
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
        /* playButton = (ImageButton) this.getActivity().findViewById(R.id.play_button);
        forwardButton = (ImageButton) this.getActivity().findViewById(R.id.forward_button);
        speedUpButton = (ImageButton) this.getActivity().findViewById(R.id.speed_up_button);
        speedDownButton = (ImageButton) this.getActivity().findViewById(R.id.speed_down_button);
        backwardButton = (ImageButton) this.getActivity().findViewById(R.id.backward_button);
        backTenButton = (ImageButton) this.getActivity().findViewById(R.id.backwardten_button);
        forwardTenButton = (ImageButton) this.getActivity().findViewById(R.id.forwardten_button);
        addBookmarkButton = (ImageButton) this.getActivity().findViewById(R.id.addbookmark_button);
        nextBookmarkButton = (ImageButton) this.getActivity().findViewById(R.id.next_bookmark_button);
        recyclerViewCloserButton = (ImageButton) this.getActivity().findViewById(R.id.recyclerview_closer_button);
        previousBookmarkButton = (ImageButton) this.getActivity().findViewById(R.id.previous_bookmark_button);
        bookmarksViewButton = (Button) this.getActivity().findViewById(R.id.bookmarks_view_button);
        disconnectButton = (Button) this.getActivity().findViewById(R.id.disconnect_button);
        connectingButton = (Button) this.getActivity().findViewById(R.id.connecting_status_button);
        connectionStatusButton = new ConnectionStatusButton(this.getContext(),this.getView());

        if(savedInstanceState != null){

        }*/
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
        connectionStatusButton = new ConnectionStatusButton(view.getContext(),view);
        connection = new Connection(view);
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
                        //TODO :fix local time
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
                Intent intent = new Intent(this.getActivity(), NewBookmarkActivity.class);
                launcherNewBookmarkActivity.launch(intent);
            }
            return false;
        });


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


        connectingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionStatusButton.getConnectionStatus()) {
                    connectionStatusButton.buttonConnecting();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            connection.start();
                            connectionStatusButton.buttonConnected();
                            disconnectButton.setVisibility(View.VISIBLE);
                        }
                    }, 1000);
                }
            }
        });

        disconnectButton.setOnClickListener(v -> {
            connection.stop();
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