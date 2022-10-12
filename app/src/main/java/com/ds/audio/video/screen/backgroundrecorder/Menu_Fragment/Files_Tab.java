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
public class Files_Tab extends Fragment  {

    View view;
    Context context;

    public Files_Tab() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.devspy_fragment_cctv_files_tab, container, false);
        context = getContext();
        return view;
    }




}
