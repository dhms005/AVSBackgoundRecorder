package com.ds.audio.video.screen.backgroundrecorder.roomdb.Video;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.R;

class WordViewHolder extends RecyclerView.ViewHolder {
    private final TextView wordItemView;
    private final ImageView img_delete;

    private WordViewHolder(View itemView) {
        super(itemView);
        wordItemView = itemView.findViewById(R.id.textView);
        img_delete = itemView.findViewById(R.id.img_delete);
    }

    public void bind(String text, ScheduleVideo current, WordViewModel myWordViewModel) {
        wordItemView.setText(text);
        //my new set on click listener
        img_delete.setOnClickListener(view -> myWordViewModel.delete(current));
    }

    static WordViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule_video, parent, false);
        return new WordViewHolder(view);
    }
}