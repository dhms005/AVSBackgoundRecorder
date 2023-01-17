package com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.ScreenshotApp.Service.FloatingService;

public class DeleteActionFragment extends Fragment {
    public static DeleteActionFragment newInstance() {
        return new DeleteActionFragment();
    }

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_delete_action, viewGroup, false);
        inflate.findViewById(R.id.clearDemo).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                FragmentActivity activity = DeleteActionFragment.this.getActivity();
                activity.stopService(new Intent(activity, FloatingService.class));
            }
        });
        return inflate;
    }
}
