package com.example.rewind.tutorial_fragment;

import android.content.pm.ActivityInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rewind.R;
import com.example.rewind.ViewPagerAdapter;
import com.example.rewind.tutorial_fragment.BookmarkFragmentTutorial;

import java.util.ArrayList;


public class TutorialFragment extends Fragment {
    private ViewPager2 viewPager2;

    public TutorialFragment(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(
                R.layout.fragment_tutorial, container, false);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BookmarkFragmentTutorial(this));
        fragmentList.add(new VideoPlayerFragmentTutorial(this));
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(),getLifecycle(),fragmentList);
        viewPager2 = viewGroup.findViewById(R.id.viewPagerTutorial);
        viewPager2.setAdapter(adapter);

        return viewGroup;
    }

    /*
    public void onSaveInstanceState(@NonNull Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putString("ipv4Address", ipv4Address);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_UNSPECIFIED);

    }

    public ViewPager2 getViewPager2(){
        return viewPager2;
    }
}