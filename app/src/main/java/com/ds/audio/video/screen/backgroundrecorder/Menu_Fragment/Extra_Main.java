package com.ds.audio.video.screen.backgroundrecorder.Menu_Fragment;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.ds.audio.video.screen.backgroundrecorder.R;

/**
 * A simple {@link Fragment} subclass.
 */

// this is the main view which is show all  the video in list
public class Extra_Main extends Fragment  {

    View view;
    Context context;

    public Extra_Main() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.devspy_fragment_cctv_main, container, false);
        context = getContext();
        return view;
    }




}
