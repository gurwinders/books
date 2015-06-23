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



    public Database(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
db.execSQL("");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
