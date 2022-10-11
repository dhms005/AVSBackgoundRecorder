package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Helper.Audio_TimeHelper;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordListAdapter;
import com.ds.audio.video.screen.backgroundrecorder.roomdb.Video.WordViewModel;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

public class Audio_ScheduleListActivity extends AppCompatActivity {
    private WordViewModel mWordViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
        setContentView(R.layout.audio_schedule_list_activity);
        mWordViewModel = new ViewModelProvider(this).get(WordViewModel.class);
        RecyclerView recyclerView = findViewById(R.id.rvScheduleVideo);
        final WordListAdapter adapter = new WordListAdapter(new WordListAdapter.WordDiff(), mWordViewModel);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        mWordViewModel.getScheduleAudio().observe(this, words -> {
            // Update the cached copy of the words in the adapter.

            if (words.size()>0){
                Log.e("@@@",""+words.get(0).getTime());
                Log.e("@@@",""+ Audio_TimeHelper.GetTimeandDatefromMiliSeconds((long) words.get(0).getTime()));
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
                Intent intent = new Intent(Audio_ScheduleListActivity.this, Audio_Save_Schedule_Activity.class);
                startActivity(intent);
            }
        });
    }
}