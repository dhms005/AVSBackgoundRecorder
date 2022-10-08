package com.ds.audio.video.screen.backgroundrecorder.roomdb.Video;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface WordDao {

    // allowing the insert of the same word multiple times by passing a
    // conflict resolution strategy
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(ScheduleVideo word);

    @Query("DELETE FROM schedule_table")
    void deleteAll();

    @Query("SELECT * FROM schedule_table WHERE type = :from ORDER BY time ASC")
    LiveData<List<ScheduleVideo>> getScheduleVideo(String from);

    @Query("SELECT * FROM schedule_table WHERE type = :from ORDER BY time ASC")
    LiveData<List<ScheduleVideo>> getScheduleAudio(String from);

    //Aded My New Logic
    @Query("DELETE FROM schedule_table WHERE id = :userId")
    void deleteByUserId(long userId);

    //Aded My New Logic
    @Query("DELETE FROM schedule_table WHERE time = :time")
    void deleteByTime(long time);

}