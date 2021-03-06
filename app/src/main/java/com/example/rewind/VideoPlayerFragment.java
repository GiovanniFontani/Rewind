package com.example.rewind;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rewind.audio.Boombox;
import com.example.rewind.bookmarking.NewBookmarkActivity;
import com.example.rewind.bookmarking.VideoBookmarkListAdapter;
import com.example.rewind.bookmarking.VideoPlayerItemTouchListener;
import com.example.rewind.bookmarking.database.Bookmark;
import com.example.rewind.bookmarking.database.BookmarkViewModel;
import com.example.rewind.bookmarking.database.DateGetter;
import com.example.rewind.button.ConnectionStatusButton;
import com.example.rewind.connection.Connection;
import com.example.rewind.connection.RepeatListener;
import com.example.rewind.pdf.PDFReader;
import com.example.rewind.pdf.PageViewerActivity;
import com.pdftron.pdf.Action;
import com.pdftron.pdf.Destination;
import com.pdftron.pdf.PDFDoc;
import com.pdftron.sdf.SDFDoc;

import java.time.LocalTime;


public class VideoPlayerFragment extends Fragment implements VideoPlayerItemTouchListener {
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
    private ImageButton previousBookmarkButton;
    private ImageButton recyclerViewCloserButton;
    private RecyclerView bookmarksRecyclerInVideoplayer;
    private Button changePDFBookmarkButton;
    private Button changePDFPageBookmarkButton;
    private Button bookmarksViewButton;
    private Button disconnectButton;
    private SeekBar seekbar;
    private View connectingButton;
    private VideoBookmarkListAdapter adapter;
    private ConnectionStatusButton connectionStatusButton;
    ActivityResultLauncher<Intent> launcherNewBookmarkActivity;
    private TextView videoName;
    private TextView volumePercentage;
    private String ipv4Address = null;
    private ImageButton volumeImageButton;
    private SeekBar volumeSeekBar;
    ActivityResultLauncher<Intent> launcherWiFiDetector;
    private  Connection connection;
    private ActivityResultLauncher<Intent> launcherPageChange;
    private ActivityResultLauncher<Intent> launcherPDFChange;
    private View view;
  
    public VideoPlayerFragment(){

    }

    public static VideoPlayerFragment newInstance(String msg) {
        VideoPlayerFragment fragment = new VideoPlayerFragment();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("ipv4Address", ipv4Address);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_video_player, container, false);
        playButton = view.findViewById(R.id.play_button);
        forwardButton = view.findViewById(R.id.forward_button);
        speedUpButton = view.findViewById(R.id.speed_up_button);
        speedDownButton = view.findViewById(R.id.speed_down_button);
        backwardButton = view.findViewById(R.id.backward_button);
        backTenButton = view.findViewById(R.id.backwardten_button);
        forwardTenButton = view.findViewById(R.id.forwardten_button);
        addBookmarkButton = view.findViewById(R.id.addbookmark_button);
        nextBookmarkButton = view.findViewById(R.id.next_bookmark_button);
        previousBookmarkButton = view.findViewById(R.id.previous_bookmark_button);
        recyclerViewCloserButton = view.findViewById(R.id.recyclerview_closer_button);
        bookmarksRecyclerInVideoplayer = view.findViewById(R.id.bookmarks_in_videoPlayer);
        bookmarksViewButton = view.findViewById(R.id.bookmarks_view_button);
        changePDFPageBookmarkButton = view.findViewById(R.id.change_pdf_page_videoplayer_button);
        changePDFBookmarkButton = view.findViewById(R.id.change_pdf_videoplayer_button);
        disconnectButton = view.findViewById(R.id.disconnect_button);
        connectingButton = view.findViewById(R.id.connecting_status_button);
        seekbar = view.findViewById(R.id.video_seekbar);
        videoName = view.findViewById(R.id.video_player_name);
        volumeImageButton = view.findViewById(R.id.volume_image_button);
        volumeSeekBar = view.findViewById(R.id.volume_seekbar);
        volumePercentage = view.findViewById(R.id.volume_percentage_text_view);
        connectionStatusButton = new ConnectionStatusButton(view.getContext(),view);
        launcherPDFChange = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        Uri pdfUri = Uri.parse(data.getStringExtra("pdfUri"));
                        String fileName = pdfUri.toString();
                        fileName = fileName.substring(fileName.lastIndexOf("/") + 1);
                        if(adapter.getSelectedPositionBookmark().documentPath != null){
                            try
                            {
                                PDFDoc doc = new PDFDoc(adapter.getSelectedPositionBookmark().documentPath.getPath());
                                doc.initSecurityHandler();
                                String bookmarkName = adapter.getSelectedPositionBookmark().name;
                                com.pdftron.pdf.Bookmark bookmarkDel = doc.getFirstBookmark().find(bookmarkName);
                                if (bookmarkDel.isValid()) {
                                    bookmarkDel.delete();
                                } else {
                                    throw new Exception("bookmarkDel is not Valid");
                                }
                                com.pdftron.pdf.Bookmark bookmark = com.pdftron.pdf.Bookmark.create(doc, bookmarkName);
                                doc.addRootBookmark(bookmark);
                                bookmark.setAction(Action.createGoto(
                                        Destination.createFit(doc.getPage(Integer.parseInt(data.getStringExtra("page"))+1))));
                                doc.save(adapter.getSelectedPositionBookmark().documentPath.getPath(), SDFDoc.SaveMode.NO_FLAGS, null);
                                doc.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        bookmarkViewModel.update(adapter.getSelectedPositionBookmark().bk_id,fileName,pdfUri,Integer.parseInt(data.getStringExtra("page")));
                    }
                });
        launcherPageChange = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        try
                        {
                            PDFDoc doc = new PDFDoc(adapter.getSelectedPositionBookmark().documentPath.getPath());
                            doc.initSecurityHandler();
                            String bookmarkName = adapter.getSelectedPositionBookmark().name;
                            com.pdftron.pdf.Bookmark bookmarkDel = doc.getFirstBookmark().find(bookmarkName);
                            if (bookmarkDel.isValid()) {
                                bookmarkDel.delete();
                            } else {
                                throw new Exception("bookmarkDel is not Valid");
                            }
                            // Lets first create the root bookmark items.
                            com.pdftron.pdf.Bookmark bookmark = com.pdftron.pdf.Bookmark.create(doc, bookmarkName);
                            doc.addRootBookmark(bookmark);
                            bookmark.setAction(Action.createGoto(
                                    Destination.createFit(doc.getPage(Integer.parseInt(data.getStringExtra("page"))+1))));
                            doc.save(adapter.getSelectedPositionBookmark().documentPath.getPath(), SDFDoc.SaveMode.NO_FLAGS, null);
                            doc.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        bookmarkViewModel.update(adapter.getSelectedPositionBookmark().bk_id,adapter.getSelectedPositionBookmark().documentName,adapter.getSelectedPositionBookmark().documentPath,Integer.parseInt(data.getStringExtra("page")));
                        adapter.refresh();
                    }
                });

        if (bookmarksRecyclerInVideoplayer != null) {
            Context context = bookmarksRecyclerInVideoplayer.getContext();
            bookmarksRecyclerInVideoplayer.setLayoutManager(new LinearLayoutManager(context));
            adapter = new VideoBookmarkListAdapter(new VideoBookmarkListAdapter.BookmarkDiff());
            adapter.setClickListener(this);
            bookmarksRecyclerInVideoplayer.setAdapter(adapter);
            bookmarkViewModel = new ViewModelProvider(requireActivity()).get(BookmarkViewModel.class);
            String videoName = (this.videoName).getText().toString();
            bookmarkViewModel.getByVideoName(videoName).observe(getViewLifecycleOwner(), adapter::submitList);
            changePDFPageBookmarkButton.setOnClickListener(view -> {
                if(adapter.isRowSelected()) {
                    Intent intent = new Intent(this.getActivity(), PageViewerActivity.class).putExtra("pdfUri", adapter.getSelectedPositionBookmark().documentPath.toString());
                    intent.putExtra("pageNumber", Integer.toString(adapter.getSelectedPositionBookmark().pageNumber));
                    launcherPageChange.launch(intent);
                }
            });
            changePDFBookmarkButton.setOnClickListener(view -> {
                if(adapter.isRowSelected()) {
                    Intent intent = new Intent(this.getActivity(), PDFReader.class);
                    if(adapter.getSelectedPositionBookmark().documentPath != null) {
                        intent.putExtra("documentPath", adapter.getSelectedPositionBookmark().documentPath.toString());
                        intent.putExtra("pageNumber", Integer.toString(adapter.getSelectedPositionBookmark().pageNumber));
                    }
                    launcherPDFChange.launch(intent);
                }else{
                    Toast errorSelectedPdf = Toast.makeText(getActivity(),"Select a bookmark first!", Toast.LENGTH_SHORT);
                    errorSelectedPdf.show();
                }
            });
        }
        if (savedInstanceState != null) {
            ipv4Address = savedInstanceState.getString("ipv4Address");
            if(ipv4Address != null) {
                connection= new Connection(view);
                connection.start(ipv4Address, connectionStatusButton);
                if (!playButton.isActivated() && connection.isPlaying()) {
                    playButton.setActivated(true);
                    playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                }
                if(connection.isConnected()){
                    view.findViewById(R.id.disconnect_button).setVisibility(View.VISIBLE);
                }else{
                    view.findViewById(R.id.disconnect_button).setVisibility(View.INVISIBLE);
                }
            }
        }
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onViewCreated(@NonNull View view , Bundle bundle) {
        Boombox.getInstance().play(R.raw.navigation_transition_right, getContext());
        if(connection == null) {
            connection = new Connection(view);
        }


        playButton.setOnClickListener(v -> {
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
            }else
                Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
        });

        forwardButton.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connection.isConnected()) {
                    connection.moveTo(String.valueOf(seekbar.getProgress() +5));
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            }
        }));

        backwardButton.setOnTouchListener(new RepeatListener(400, 100, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(connection.isConnected()) {
                    connection.moveTo(String.valueOf(seekbar.getProgress() - 5));
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            }
        }));

        speedUpButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speedUpButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.setSpeed(+0.25);
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedUpButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        speedDownButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                speedDownButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.setSpeed(-0.25);
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                speedDownButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        backTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                backTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.moveTo("-10");
                }else
                Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                backTenButton.setBackgroundResource(R.drawable.rounded_button);

            }
            return false;
        });

        forwardTenButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                forwardTenButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    connection.moveTo("+10");
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                forwardTenButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        launcherNewBookmarkActivity = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        String video_name = ((TextView) view.findViewById(R.id.video_player_name)).getText().toString();
                        String bookmarkName = data.getStringExtra(NewBookmarkActivity.EXTRA_REPLY);
                        String documentName = data.getStringExtra("documentName");
                        Uri documentPath;
                        int documentPage;
                        if (documentName.equals("null")) {
                            documentName = null;
                            documentPath = null;
                            documentPage = -1;
                        } else {
                            documentPath = Uri.parse(data.getStringExtra("documentPath"));
                            documentPage = Integer.parseInt(data.getStringExtra("documentPage"));
                        }
                        try
                        {
                            PDFDoc doc = new PDFDoc(documentPath.getPath());
                            doc.initSecurityHandler();
                            com.pdftron.pdf.Bookmark bookmark = com.pdftron.pdf.Bookmark.create(doc, bookmarkName);
                            doc.addRootBookmark(bookmark);
                            // +1 because PDFviewer Library start from 0 and PDFThron start from 1
                            bookmark.setAction(Action.createGoto(Destination.createFit(doc.getPage(documentPage+1))));
                            doc.save(documentPath.getPath(), SDFDoc.SaveMode.NO_FLAGS, null);
                            doc.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        Bookmark bookmark = new Bookmark(bookmarkName, documentName, DateGetter.getLocalDateTime(), video_name, documentPath, documentPage, DateGetter.stringToLocalTime(((TextView)view.findViewById(R.id.current_time_text)).getText().toString()));
                        bookmarkViewModel.insert(bookmark);
                    }
                });

        addBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                addBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                addBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
                if(connection.isConnected()) {
                    if(connection.isPlaying())
                            connection.play_pause();
                    Intent intent = new Intent(this.getActivity(), NewBookmarkActivity.class);
                    launcherNewBookmarkActivity.launch(intent);
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            }
            return false;
        });


        nextBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                nextBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    LocalTime currentTime = DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.current_time_text)).getText().toString());
                    LocalTime target= DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.total_time_text)).getText().toString());
                    LocalTime temp = DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.total_time_text)).getText().toString());
                    String bookmark_name = "End of the video";
                    for(Bookmark bookmark : adapter.getCurrentList()){
                        if(currentTime.isBefore(bookmark.videoTime) && temp.isAfter(bookmark.videoTime)){
                            temp = bookmark.videoTime;
                            bookmark_name ="- "+ bookmark.name +" -";
                        }
                    }
                    if(target.isAfter(temp)){
                        target = temp;
                    }
                    String[] string_target =DateGetter.LocalTimeToString(target).split(":");
                    int seconds = Integer.parseInt(string_target[2]) + Integer.parseInt(string_target[1])*60 + Integer.parseInt(string_target[0])*3600;
                    connection.moveTo(String.valueOf(seconds));
                    Toast.makeText(getContext(), bookmark_name, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                nextBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });


        previousBookmarkButton.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                previousBookmarkButton.setBackgroundResource(R.drawable.activated_rounded_button);
                if(connection.isConnected()) {
                    LocalTime currentTime = DateGetter.stringToLocalTime(((TextView) view.findViewById(R.id.current_time_text)).getText().toString());
                    LocalTime target= DateGetter.stringToLocalTime("00:00:00");
                    LocalTime temp = DateGetter.stringToLocalTime("00:00:00");
                    String bookmark_name = "-Start of the video-";
                    for(Bookmark bookmark : adapter.getCurrentList()){
                        if(currentTime.isAfter(bookmark.videoTime) && temp.isBefore(bookmark.videoTime)){
                            temp = bookmark.videoTime;
                            bookmark_name = " "+ bookmark.name +" -";
                        }
                    }
                    if(target.isBefore(temp)){
                        target = temp;
                    }
                    String[] string_target =DateGetter.LocalTimeToString(target).split(":");
                    int seconds = Integer.parseInt(string_target[2]) + Integer.parseInt(string_target[1])*60 + Integer.parseInt(string_target[0])*3600;
                    connection.moveTo(String.valueOf(seconds));
                    Toast.makeText(getContext(), bookmark_name, Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getContext(), "First connect to a video", Toast.LENGTH_SHORT).show();
            } else if (event.getAction() == MotionEvent.ACTION_UP) {
                previousBookmarkButton.setBackgroundResource(R.drawable.rounded_button);
            }
            return false;
        });

        bookmarksViewButton.setOnClickListener(v -> {
            String videoName = ((TextView) this.videoName).getText().toString();
            bookmarkViewModel.getByVideoName(videoName).observe(getViewLifecycleOwner(), adapter::submitList);
            if (bookmarksViewButton.getVisibility() == View.VISIBLE){
                ConstraintLayout layout = view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setEnabled(false);
                }
                recyclerViewCloserButton.setVisibility(View.VISIBLE);
                bookmarksRecyclerInVideoplayer.setVisibility(View.VISIBLE);
                changePDFPageBookmarkButton.setVisibility(View.VISIBLE);
                changePDFBookmarkButton.setVisibility(View.VISIBLE);
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

        launcherWiFiDetector = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        ipv4Address = data.getStringExtra("ipaddress").toString();
                        connectionStatusButton.buttonConnecting();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                connection.start(ipv4Address, connectionStatusButton);
                                if(!playButton.isActivated() && connection.isPlaying()) {
                                    playButton.setActivated(true);
                                    playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                                }
                            }
                        }, 2000);

                    }
                });

        connectingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!connectionStatusButton.getConnectionStatus()) {
                    if(ipv4Address == null) {
                        Intent intent = new Intent(getActivity(), WiFiDetectorActivity.class);
                        launcherWiFiDetector.launch(intent);
                    }else{
                        connectionStatusButton.buttonConnecting();
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                connection.start(ipv4Address, connectionStatusButton);
                                if(!playButton.isActivated() && connection.isPlaying()) {
                                    playButton.setActivated(true);
                                    playButton.setBackgroundResource(R.drawable.activated_rounded_button);
                                }
                            }
                        }, 2000);
                    }
                }
            }
        });

        videoName.addTextChangedListener(new TextWatcher() {
            String title;
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                title = videoName.getText().toString();
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
            @Override
            public void afterTextChanged(Editable editable) {
                if(!title.equals(videoName.getText().toString())){
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
            ipv4Address = null;
            disconnectButton.setVisibility(View.INVISIBLE);
            connectionStatusButton.buttonDisconnect();
        });


        recyclerViewCloserButton.setOnClickListener(v -> {
            if (recyclerViewCloserButton.getVisibility() == View.VISIBLE) {
                ConstraintLayout layout = view.findViewById(R.id.inner_videoplayer);
                for (int i = 0; i < layout.getChildCount(); i++) {
                    View child = layout.getChildAt(i);
                    child.setEnabled(true);
                }
                recyclerViewCloserButton.setVisibility(View.INVISIBLE);
                bookmarksRecyclerInVideoplayer.setVisibility(View.INVISIBLE);
                changePDFPageBookmarkButton.setVisibility(View.INVISIBLE);
                changePDFBookmarkButton.setVisibility(View.INVISIBLE);
            }
        });


        volumeImageButton.setOnClickListener(v->{
                if (volumeSeekBar.getProgress() != 0 ) {
                    if (!volumeImageButton.isSaveEnabled()) {
                        volumeImageButton.setImageResource(R.drawable.ic_volume);
                        volumeImageButton.setSaveEnabled(true);
                        connection.setVolume(volumeSeekBar.getProgress());
                    } else {
                        volumeImageButton.setImageResource(R.drawable.ic_volume_off);
                        volumeImageButton.setSaveEnabled(false);
                        connection.setVolume(0);
                    }
                }
                else{
                    if (!volumeImageButton.isSaveEnabled()) {
                        volumeImageButton.setImageResource(R.drawable.ic_volume);
                        volumeImageButton.setSaveEnabled(true);
                    } else {
                        volumeImageButton.setImageResource(R.drawable.ic_volume_off);
                        volumeImageButton.setSaveEnabled(false);
                    }
                    connection.setVolume(0);
                }
        });

        volumeSeekBar.setMax(512);
        volumeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (volumeSeekBar.getProgress() == 0){
                    volumeImageButton.setImageResource(R.drawable.ic_volume_0);
                    connection.setVolume(0);
                }else{
                    if (volumeImageButton.isSaveEnabled()){
                        volumeImageButton.setImageResource(R.drawable.ic_volume);
                        connection.setVolume(volumeSeekBar.getProgress());
                    }
                    else {
                        volumeImageButton.setImageResource(R.drawable.ic_volume_off);
                        connection.setVolume(0);
                    }
                }
            }
        });
    }



    @Override
    public void onTouch(View itemView, MotionEvent event, int selectedPosition, Bookmark current) {
        if(current.documentPath != null)
            changePDFPageBookmarkButton.setEnabled(true);
        else
            changePDFPageBookmarkButton.setEnabled(false);
        changePDFBookmarkButton.setEnabled(true);
    }
}

