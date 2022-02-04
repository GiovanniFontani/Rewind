package com.example.rewind;

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
                NavHostFragment.findNavController(VideoPlayerFragment.this)
                        .navigate(R.id.action_videoPlayerFragment_to_introFragment);
            }
        });
    }
}