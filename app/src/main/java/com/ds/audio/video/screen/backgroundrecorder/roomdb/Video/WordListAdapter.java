package com.ds.audio.video.screen.backgroundrecorder.roomdb.Video;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;

import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_TimeHelper;

public class WordListAdapter extends ListAdapter<ScheduleVideo, WordViewHolder> {

    WordViewModel myWordViewModel;

    public WordListAdapter(@NonNull DiffUtil.ItemCallback<ScheduleVideo> diffCallback, WordViewModel mWordViewModel) {
        super(diffCallback);
        myWordViewModel = mWordViewModel;
    }

    @Override
    public WordViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return WordViewHolder.create(parent);
    }

    @Override
    public void onBindViewHolder(WordViewHolder holder, int position) {
        ScheduleVideo current = getItem(position);
        holder.bind(Video_TimeHelper.GetTimeandDatefromMiliSeconds((long) current.getTime()),current,myWordViewModel);
    }

    public static class WordDiff extends DiffUtil.ItemCallback<ScheduleVideo> {

        @Override
        public boolean areItemsTheSame(@NonNull ScheduleVideo oldItem, @NonNull ScheduleVideo newItem) {
            return oldItem == newItem;
        }

        @Override
        public boolean areContentsTheSame(@NonNull ScheduleVideo oldItem, @NonNull ScheduleVideo newItem) {
            return String.valueOf(oldItem.getTime()).equals(String.valueOf(newItem.getTime()));
        }
    }
}