package com.toptech.launcher.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

public class LauncherDatabaseHelper extends SQLiteOpenHelper {
    private static LauncherDatabaseHelper instance = null;

    public static final class MostVisited implements BaseColumns {
        public static final Uri CONTENT_URI = Uri.parse("content://com.toptech.sagittarius.launcher/tbl_sagittarius_mostvisited");
    }

    public LauncherDatabaseHelper(Context context) {
        super(context, "launcher.db", (SQLiteDatabase.CursorFactory) null, 1);
    }

    public static LauncherDatabaseHelper getInstance(Context paramContext) {
        if (instance == null) {
            instance = new LauncherDatabaseHelper(paramContext);
        }
        return instance;
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE tbl_sagittarius_file(_id INTEGER PRIMARY KEY,name TEXT NOT NULL DEFAULT '',path TEXT NOT NULL DEFAULT '',kind INTEGER NOT NULL,UNIQUE(path))");
        db.execSQL("CREATE TABLE tbl_sagittarius_mostvisited (_id INTEGER PRIMARY KEY,appname TEXT,packagename TEXT,activityname TEXT UNIQUE ,frequency INTEGER,time INTEGER )");
        db.execSQL("CREATE TABLE tbl_sagittarius_location(_id INTEGER PRIMARY KEY,country TEXT NOT NULL DEFAULT '',region TEXT NOT NULL DEFAULT '',city TEXT NOT NULL DEFAULT '',woeid TEXT NOT NULL DEFAULT '',UNIQUE(country,region,city))");
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS tbl_sagittarius_mostvisited");
        onCreate(db);
    }
}
