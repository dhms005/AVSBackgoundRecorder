package com.ds.audio.video.screen.backgroundrecorder.roomdb.Video;

import static com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts.FROM_AUDIO;
import static com.ds.audio.video.screen.backgroundrecorder.DevSpy_Define.DevSpy_Conts.FROM_VIDEO;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {

    private WordDao mWordDao;
    private LiveData<List<ScheduleVideo>> getScheduleVideo;
    private LiveData<List<ScheduleVideo>> getScheduleAudio;
    ScheduleVideo time ;

    // Note that in order to unit test the WordRepository, you have to remove the Application
    // dependency. This adds complexity and much more code, and this sample is not about testing.
    // See the BasicSample in the android-architecture-components repository at
    // https://github.com/googlesamples
    public WordRepository(Application application) {
        WordRoomDatabase db = WordRoomDatabase.getDatabase(application);
        mWordDao = db.wordDao();
        getScheduleVideo = mWordDao.getScheduleVideo(FROM_VIDEO);
        getScheduleAudio = mWordDao.getScheduleAudio(FROM_AUDIO);

    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<ScheduleVideo>> getScheduleVideo() {
        return getScheduleVideo;
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    LiveData<List<ScheduleVideo>> getScheduleAudio() {
        return getScheduleAudio;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(ScheduleVideo word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.insert(word);
        });
    }

    void delete(ScheduleVideo word) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.deleteByUserId(word.getId());
        });
    }

   public void deleteTimer(double time) {
        WordRoomDatabase.databaseWriteExecutor.execute(() -> {
            mWordDao.deleteByTime((long) time);
        });
    }

//    ScheduleVideo getClosetTimeData() {
//        time = mWordDao.getClosetTimeData(FROM_VIDEO);
//        return time;
//    }
}