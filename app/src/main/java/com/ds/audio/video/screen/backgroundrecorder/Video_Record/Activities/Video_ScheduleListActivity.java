package com.ds.audio.video.screen.backgroundrecorder.Video_Record.Activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.Video_Record.Helper.Video_TimeHelper;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordListAdapter;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordViewModel;

public class Video_ScheduleListActivity extends AppCompatActivity {
    private WordViewModel mWordViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.video_schedule_list_activity);
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.rvScheduleVideo);
        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff(), mWordViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mWordViewModel.getScheduleVideo().observe(this, words -> {
            // Update the cached copy of the words in the adapter.

            if (words.size() > 0) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    words.forEach(scheduleVideo -> {
                        Log.e("DBINSERT", "" + Video_TimeHelper.GetTimeandDatefromMiliSeconds((long) scheduleVideo.getTime()));
                    });
                }
            }
            adapter.submitList(words);
            adapter.notifyDataSetChanged();
        });

        findViewById(R.id.back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        findViewById(R.id.tvAddSchedule).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Video_ScheduleListActivity.this, Video_Save_Schedule_Activity.class);
                startActivity(intent);
            }
        });
    }
}