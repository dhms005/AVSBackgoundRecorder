package com.ds.audio.video.screen.backgroundrecorder.roomdb.Video;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class WordViewModel extends AndroidViewModel {

    private WordRepository mRepository;

    private final LiveData<List<ScheduleVideo>> getScheduleVideo;
    private final LiveData<List<ScheduleVideo>> getScheduleAudio;
    ScheduleVideo time;

    public WordViewModel(Application application) {
        super(application);
        mRepository = new WordRepository(application);
        getScheduleVideo = mRepository.getScheduleVideo();
        getScheduleAudio = mRepository.getScheduleAudio();

    }

    public LiveData<List<ScheduleVideo>> getScheduleVideo() {
        return getScheduleVideo;
    }



    public LiveData<List<ScheduleVideo>> getScheduleAudio() {
        return getScheduleAudio;
    }

    public void insert(ScheduleVideo word) {
        mRepository.insert(word);
    }


    public void delete(ScheduleVideo word) {
        mRepository.delete(word);
    }

//    public ScheduleVideo getClosetTimeData() {
//        time = mRepository.getClosetTimeData();
//        return time;
//    }
}