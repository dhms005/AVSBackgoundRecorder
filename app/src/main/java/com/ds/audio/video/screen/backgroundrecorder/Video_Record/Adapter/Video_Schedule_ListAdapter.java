package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_TimeHelper;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.UserModel;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.Video_Database_Helper;

import java.util.ArrayList;

public class Video_Schedule_ListAdapter extends RecyclerView.Adapter<Video_Schedule_ListAdapter.ViewHolder> {
    ArrayList<UserModel> users;

    // RecyclerView recyclerView;
    public Video_Schedule_ListAdapter(ArrayList<UserModel> users) {
        this.users = users;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.item_schedule_video, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final UserModel myListData = users.get(position);
        holder.textView.setText(Video_TimeHelper.GetTimeandDatefromMiliSeconds(Long.parseLong(myListData.getV_time())));
        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(view.getContext(), "click on item: " + myListData.getV_time(), Toast.LENGTH_LONG).show();

                Video_Database_Helper.Video_deleteEntry(myListData.getV_time());

            }
        });
    }


    @Override
    public int getItemCount() {
        return users.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView img_delete;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.img_delete = (ImageView) itemView.findViewById(R.id.img_delete);

        }
    }
}  