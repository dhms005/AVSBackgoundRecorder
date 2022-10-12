package com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.ds.audio.video.screen.backgroundrecorder.Extra.DevSpy_Model.DevSpy_ItemPower;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.util.List;

public class DevSpy_PowerModeAdapter extends RecyclerView.Adapter<DevSpy_PowerModeAdapter.MyViewHolder> {
    public List<DevSpy_ItemPower> apps;

    public DevSpy_PowerModeAdapter(List<DevSpy_ItemPower> list) {
        this.apps = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.devspy_item_power, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        myViewHolder.size.setText(this.apps.get(i).getText());
    }

    public int getItemCount() {
        return this.apps.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView size;

        public MyViewHolder(View view) {
            super(view);
            this.size = (TextView) view.findViewById(R.id.items);
        }
    }
}
