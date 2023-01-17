package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Model.DevSpy_Apps;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.util.List;

public class DevSpy_CPUApplicationsScanningAdapter extends RecyclerView.Adapter<DevSpy_CPUApplicationsScanningAdapter.MyViewHolder> {
    public List<DevSpy_Apps> apps;

    public DevSpy_CPUApplicationsScanningAdapter(List<DevSpy_Apps> list) {
        this.apps = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.devspy_scan_cpu_apps, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.size.setText("");
        myViewHolder.image.setImageDrawable(this.apps.get(i).getImage());
    }

    public int getItemCount() {
        return this.apps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        public TextView size;

        public MyViewHolder(View view) {
            super(view);
            this.size = (TextView) view.findViewById(R.id.apptext);
            this.image = (ImageView) view.findViewById(R.id.appimage);
        }
    }
}
