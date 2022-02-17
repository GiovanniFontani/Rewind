package com.example.rewind.tutorial_fragment;

import android.os.Bundle;

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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup viewGroup = (ViewGroup) inflater.inflate(
                R.layout.fragment_tutorial, container, false);
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new BookmarkFragmentTutorial(this));
        fragmentList.add(new VideoPlayerFragmentTutorial(this));
        ViewPagerAdapter adapter = new ViewPagerAdapter(requireActivity().getSupportFragmentManager(),getLifecycle(),fragmentList);
        ((ViewPager2) viewGroup.findViewById(R.id.viewPagerTutorial)).setAdapter(adapter);
        viewPager2 = viewGroup.findViewById(R.id.viewPagerTutorial);
        return viewGroup;
    }

    public ViewPager2 getViewPager2(){
        return viewPager2;
    }
}