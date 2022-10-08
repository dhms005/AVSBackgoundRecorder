package com.ds.audio.video.screen.backgroundrecorder.roomdb.Video;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "schedule_table")
public class ScheduleVideo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private long time;
    private String type;
    private String camera;
    private double duration;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double getDuration() {
        return duration;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    public String getCamera() {
        return camera;
    }

    public void setCamera(String camera) {
        this.camera = camera;
    }

    public ScheduleVideo(@NonNull long time, String type, double duration, String camera) {
        this.time = time;
        this.type = type;
        this.duration = duration;
        this.camera = camera;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}