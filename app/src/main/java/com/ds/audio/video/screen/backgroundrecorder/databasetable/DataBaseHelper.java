package com.ds.audio.video.screen.backgroundrecorder.databasetable;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DataBaseHelper extends SQLiteOpenHelper {
    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase _db) {
        try {
            _db.execSQL(Video_Database_Helper.Video_DATABASE_CREATE);
            _db.execSQL(Video_Database_Helper.Audio_DATABASE_CREATE);
        }catch(Exception er){
            Log.e("Error","exceptioin");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase _db, int _oldVersion, int _newVersion)
    {
        _db.execSQL("DROP TABLE IF EXISTS " + Video_Database_Helper.Video_TABLE_NAME);
        _db.execSQL("DROP TABLE IF EXISTS " + Video_Database_Helper.Audio_TABLE_NAME);
        // Create a new one.
        onCreate(_db);
    }
}