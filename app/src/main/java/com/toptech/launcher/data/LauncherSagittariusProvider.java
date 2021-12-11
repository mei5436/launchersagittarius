package com.toptech.launcher.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class LauncherSagittariusProvider extends ContentProvider {
    private static final String TAG = "LauncherSagittariusProvider";
    private static final UriMatcher mMatcher = new UriMatcher(-1);
    private LauncherDatabaseHelper dbHelper;
    private SQLiteDatabase dbreader;
    private SQLiteDatabase dbwriter;

    static {
        mMatcher.addURI("com.toptech.sagittarius.launcher", "tbl_sagittarius_file", 1);
        mMatcher.addURI("com.toptech.sagittarius.launcher", "tbl_sagittarius_mostvisited", 2);
        mMatcher.addURI("com.toptech.sagittarius.launcher", "tbl_sagittarius_location", 3);
    }

    public boolean onCreate() {
        this.dbHelper = LauncherDatabaseHelper.getInstance(getContext());
        return true;
    }

    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        this.dbreader = this.dbHelper.getReadableDatabase();
        switch (mMatcher.match(uri)) {
            case 1:
                return this.dbreader.query("tbl_sagittarius_file", projection, selection, selectionArgs, null, null, sortOrder, uri.getQueryParameter("limit"));
            case 2:
                if (TextUtils.isEmpty(sortOrder)) {
                    sortOrder = "frequency DESC,time DESC";
                }
                return this.dbreader.query("tbl_sagittarius_mostvisited", projection, selection, selectionArgs, null, null, sortOrder);
            case 3:
                return this.dbreader.query("tb1_sagittarius_location", projection, selection, selectionArgs, null, null, sortOrder);
            default:
                return null;
        }
    }

    public String getType(Uri uri) {
        return null;
    }

    public Uri insert(Uri uri, ContentValues values) {
        long l;
        this.dbwriter = this.dbHelper.getWritableDatabase();
        switch (mMatcher.match(uri)) {
            case 1:
                l = this.dbwriter.insert("tbl_sagittarius_file", null, values);
                break;
            case 2:
                l = this.dbwriter.insert("tbl_sagittarius_mostvisited", null, values);
                break;
            case 3:
                l = this.dbwriter.insert("tbl_sagittarius_location", null, values);
                break;
            default:
                Log.e(TAG, "insert error");
                return null;
        }
        return ContentUris.withAppendedId(uri, l);
    }

    public int delete(Uri uri, String selection, String[] selectionArgs) {
        this.dbwriter = this.dbHelper.getWritableDatabase();
        switch (mMatcher.match(uri)) {
            case 1:
                return this.dbwriter.delete("tbl_sagittarius_file", selection, selectionArgs);
            case 2:
                return this.dbwriter.delete("tbl_sagittarius_mostvisited", selection, selectionArgs);
            default:
                return 0;
        }
    }

    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        this.dbwriter = this.dbHelper.getWritableDatabase();
        switch (mMatcher.match(uri)) {
            case 2:
                return this.dbwriter.update("tbl_sagittarius_mostvisited", values, selection, selectionArgs);
            default:
                return 0;
        }
    }
}
