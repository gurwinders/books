package com.rajpal.books.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by asus on 23-06-15.
 */
public class Database extends SQLiteOpenHelper {


    public static final int VERSION = 1;
    public static final String DB_NAME = "m.db";
    public static final String TABLE_Downloads = "downloads";
    public static final String Download_COLUMN_id = "id";
    public static final String Download_COLUMN_path = "path";
    public static final String Download_COLUMN_name = "name";
    public static final String Download_COLUMN_time = "time";
    public static final String Download_COLUMN_content_length = "c_lenght";
    public static final String Download_COLUMN_download_size = "size";
    public static final String Download_COLUMN_last_modified = "last_modified";
    public static final String Download_COLUMN_downloading_status = "status";

    String CreateTable = "create table " + TABLE_Downloads + " (" + Download_COLUMN_id
            + " integer primary key, " + Download_COLUMN_name + " text, "
            + Download_COLUMN_path + " text, " + Download_COLUMN_time
            + " text, " + Download_COLUMN_content_length + " text, " + Download_COLUMN_download_size
            + " text, " + Download_COLUMN_last_modified + " text, "
            + Download_COLUMN_downloading_status + " text)";

    public Database(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CreateTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
