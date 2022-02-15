package com.example.rewind;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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
    }

    //TODO: tutorial button, dovrebbe aprire un DIALOG a "quasi" tutta pagina, con le istruzioni per fare roba.

    //TODO: ^^^^^^^decidere se farlo per ogni pagina, o una singola volta per tutte le pagine.^^^^^^^^^^^^^^^
}