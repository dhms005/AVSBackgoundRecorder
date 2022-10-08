package com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


import com.ds.audio.video.screen.backgroundrecorder.Extra.CY_M_Model.CY_M_Apps;
import com.ds.audio.video.screen.backgroundrecorder.R;

import java.util.List;

public class CY_M_RecyclerAdapter extends RecyclerView.Adapter<CY_M_RecyclerAdapter.MyViewHolder> {
    public List<CY_M_Apps> apps;

    public CY_M_RecyclerAdapter(List<CY_M_Apps> list) {
        this.apps = list;
    }

    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cy_m_recycler_apps, viewGroup, false));
    }

    public void onBindViewHolder(MyViewHolder myViewHolder, int i) {
        CY_M_Apps CYMApps2 = this.apps.get(i);
        myViewHolder.size.setText(CYMApps2.getSize());
        myViewHolder.image.setImageDrawable(CYMApps2.getImage());
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
