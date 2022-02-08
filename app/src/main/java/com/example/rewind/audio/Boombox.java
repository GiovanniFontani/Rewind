package com.example.rewind.audio;

import android.app.Activity;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import com.example.rewind.R;

import java.util.HashMap;
import java.util.List;

public class Boombox {
    private static Boombox instance;
    private final SoundPool pool;
    private final HashMap<Integer,Integer> sounds;
    private boolean loaded;

    private Boombox(){
        AudioAttributes audioAttributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        pool = new SoundPool.Builder().setMaxStreams(3)
                .setAudioAttributes(audioAttributes)
                .build();
        sounds = new HashMap<>();
        loaded = false;
    }

    public static Boombox getInstance() {
        if(instance == null){
            instance = new Boombox();
        }
        return instance;
    }

    public void load(Activity activity){
        sounds.put(R.raw.ui_tap1, pool.load(activity,R.raw.ui_tap1,1));
        sounds.put(R.raw.ui_tap2,pool.load(activity,R.raw.ui_tap2,1));
        sounds.put(R.raw.ui_tap3,pool.load(activity,R.raw.ui_tap3,1));
        sounds.put(R.raw.ui_tap4,pool.load(activity,R.raw.ui_tap4,1));
        sounds.put(R.raw.bookmark_backward_vp,pool.load(activity,R.raw.bookmark_backward_vp,1));
        sounds.put(R.raw.bookmark_forward_vp,pool.load(activity,R.raw.bookmark_forward_vp,1));
        sounds.put(R.raw.navigation_transition_left,pool.load(activity,R.raw.navigation_transition_left,1));
        sounds.put(R.raw.navigation_transition_right,pool.load(activity,R.raw.navigation_transition_right,1));
        sounds.put(R.raw.save_bookmark_vp,pool.load(activity,R.raw.save_bookmark_vp,1));
        loaded = true;
    }

    public void play(int soundID){
        if(sounds.containsKey(soundID)) {
            int sound = sounds.get(soundID);
            pool.play(sound, 1, 1, 0, 0, 1);
        }else{
            Log.e("Sound Err", "Sound not found.");
        }
    }
}
