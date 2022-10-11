package com.ds.audio.video.screen.backgroundrecorder.databasetable;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONException;

import java.util.ArrayList;

public class Video_Database_Helper {
    static ArrayList<UserModel> users = new ArrayList<>();
    static final String DATABASE_NAME = "UsersDatabase.db";
    static final String Video_TABLE_NAME = "Video_Schudule";
    static final String Audio_TABLE_NAME = "Audio_Schudule";
    static final int DATABASE_VERSION = 1;
    // SQL Statement to create a new database.
    static final String Video_DATABASE_CREATE = "create table " + Video_TABLE_NAME + "( ID integer primary key autoincrement," + Schedule_Contant.v_time + "  text," + Schedule_Contant.v_duration + "  text," + Schedule_Contant.v_cam + " text); ";
    static final String Audio_DATABASE_CREATE = "create table " + Audio_TABLE_NAME + "( ID integer primary key autoincrement," + Schedule_Contant.v_time + "  text," + Schedule_Contant.v_duration + "  text," + Schedule_Contant.v_cam + " text); ";
    private static final String TAG = "UsersDatabaseAdapter:";

    // Variable to hold the database instance
    public static SQLiteDatabase db;
    // Context of the application using the database.
    private final Context context;
    // Database open/upgrade helper
    private static DataBaseHelper dbHelper;

    public Video_Database_Helper(Context _context) {
        context = _context;
        dbHelper = new DataBaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Method to open the Database
    public Video_Database_Helper open() throws SQLException {
        db = dbHelper.getWritableDatabase();
        return this;
    }

    // Method to close the Database
    public void close() {
        db.close();
    }

    // method returns an Instance of the Database
    public SQLiteDatabase getDatabaseInstance() {
        return db;
    }

    // method to insert a record in Table
    public static String Video_insertEntry(String v_time, String v_duration, String v_cam) {

        try {


            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put(Schedule_Contant.v_time, v_time);
            newValues.put(Schedule_Contant.v_duration, v_duration);
            newValues.put(Schedule_Contant.v_cam, v_cam);

            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result = db.insert(Video_TABLE_NAME, null, newValues);
            Log.i("Row Insert Result ", String.valueOf(result));
//            toast("User Info Saved! Total Row Count is "+getRowCount());
//            db.close();

        } catch (Exception ex) {
        }
        return "ok";
    }

    // method to insert a record in Table
    public static String Audio_insertEntry(String v_time, String v_duration, String v_cam) {

        try {


            ContentValues newValues = new ContentValues();
            // Assign values for each column.
            newValues.put(Schedule_Contant.v_time, v_time);
            newValues.put(Schedule_Contant.v_duration, v_duration);
            newValues.put(Schedule_Contant.v_cam, v_cam);

            // Insert the row into your table
            db = dbHelper.getWritableDatabase();
            long result = db.insert(Audio_TABLE_NAME, null, newValues);
            Log.i("Row Insert Result ", String.valueOf(result));
//            toast("User Info Saved! Total Row Count is "+getRowCount());
//            db.close();

        } catch (Exception ex) {
        }
        return "ok";
    }

    // method to get all Rows Saved in Table
    @SuppressLint("Range")
    public static ArrayList<UserModel> Video_getRows() {

        users.clear();
        UserModel user;
        db = dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(Video_TABLE_NAME, null, null, null, null, null, Schedule_Contant.v_time + " ASC", null);
        while (projCursor.moveToNext()) {

            user = new UserModel();
            user.setID(projCursor.getString(projCursor.getColumnIndex("ID")));
            user.setV_time(projCursor.getString(projCursor.getColumnIndex(Schedule_Contant.v_time)));
            user.setV_duration(projCursor.getString(projCursor.getColumnIndex(Schedule_Contant.v_time)));
            user.setV_cam(projCursor.getString(projCursor.getColumnIndex(Schedule_Contant.v_time)));
            users.add(user);
        }
        projCursor.close();
        return users;
    }

    // method to get all Rows Saved in Table
    @SuppressLint("Range")
    public static ArrayList<UserModel> Audio_getRows() {

        users.clear();
        UserModel user;
        db = dbHelper.getReadableDatabase();
        Cursor projCursor = db.query(Audio_TABLE_NAME, null, null, null, null, null, Schedule_Contant.v_time + " ASC", null);
        while (projCursor.moveToNext()) {

            user = new UserModel();
            user.setID(projCursor.getString(projCursor.getColumnIndex("ID")));
            user.setV_time(projCursor.getString(projCursor.getColumnIndex(Schedule_Contant.v_time)));
            user.setV_duration(projCursor.getString(projCursor.getColumnIndex(Schedule_Contant.v_time)));
            user.setV_cam(projCursor.getString(projCursor.getColumnIndex(Schedule_Contant.v_time)));
            users.add(user);
        }
        projCursor.close();
        return users;
    }

    // method to delete a Record in Tbale using Primary Key Here it is ID
    public static int Video_deleteEntry(String v_time) {

        String where = Schedule_Contant.v_time + "=?";
        int numberOFEntriesDeleted = db.delete(Video_TABLE_NAME, where, new String[]{v_time});
//        toast("Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted);
        return numberOFEntriesDeleted;
    }

    // method to delete a Record in Tbale using Primary Key Here it is ID
    public static int Audio_deleteEntry(String v_time) {

        String where = Schedule_Contant.v_time + "=?";
        int numberOFEntriesDeleted = db.delete(Audio_TABLE_NAME, where, new String[]{v_time});
//        toast("Number fo Entry Deleted Successfully : "+numberOFEntriesDeleted);
        return numberOFEntriesDeleted;
    }


    // method to get Count of Toatal Rows in Table
    public static int Video_getRowCount() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Video_TABLE_NAME, null, null, null, null, null, null);
//        toast("Row Count is "+cursor.getCount());
        //      db.close();
        return cursor.getCount();
    }

    // method to get Count of Toatal Rows in Table
    public static int Audio_getRowCount() {
        db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query(Audio_TABLE_NAME, null, null, null, null, null, null);
//        toast("Row Count is "+cursor.getCount());
        //      db.close();
        return cursor.getCount();
    }

    // method to Truncate/ Remove All Rows in Table
    public static void truncateTable() {
        db = dbHelper.getReadableDatabase();
        db.delete(Video_TABLE_NAME, "1", null);
//        db.close();
//        toast("Table Data Truncated!");
    }

    // method to Update an Existing Row in Table
    public static void Video_updateEntry(String ID, String v_time, String v_duration, String v_cam) {
        ContentValues updatedValues = new ContentValues();
        updatedValues.put(Schedule_Contant.v_time, v_time);
        updatedValues.put(Schedule_Contant.v_duration, v_duration);
        updatedValues.put(Schedule_Contant.v_cam, v_cam);
        String where = "ID = ?";
        db = dbHelper.getReadableDatabase();
        db.update(Video_TABLE_NAME, updatedValues, where, new String[]{ID});
//        db.close();
//        toast("Row Updated!");
    }

}