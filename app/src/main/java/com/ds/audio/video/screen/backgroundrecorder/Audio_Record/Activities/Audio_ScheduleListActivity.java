package com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ds.audio.video.screen.backgroundrecorder.Audio_Record.Adapter.Audio_Schedule_ListAdapter;
import com.ds.audio.video.screen.backgroundrecorder.R;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.UserModel;
import com.ds.audio.video.screen.backgroundrecorder.databasetable.Video_Database_Helper;
import com.github.mylibrary.Notification.Ads.Constant_ad;
import com.github.mylibrary.Notification.Ads.SharePrefUtils;

import java.util.ArrayList;

public class Audio_ScheduleListActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    public static Audio_Schedule_ListAdapter adapter;
    ArrayList<UserModel> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SharePrefUtils.getString(Constant_ad.AD_NAV_BAR, "1").equals("0")) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }

        setContentView(R.layout.audio_schedule_list_activity);

        users = Video_Database_Helper.Audio_getRows();

        recyclerView = (RecyclerView) findViewById(R.id.rvScheduleVideo);
        adapter = new Audio_Schedule_ListAdapter(users);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

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

    @Override
    protected void onResume() {
        super.onResume();
        users = Video_Database_Helper.Audio_getRows();
        adapter.notifyDataSetChanged();

    }
}