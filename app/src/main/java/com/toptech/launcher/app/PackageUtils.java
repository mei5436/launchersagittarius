package com.toptech.launcher.app;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import com.toptech.launcher.ShareData;
import com.toptech.launcher.data.LauncherDatabaseHelper;
import java.util.Collections;
import java.util.List;

public class PackageUtils {
    private static final String TAG = PackageUtils.class.getSimpleName();

    private PackageUtils() {
        throw new AssertionError();
    }

    public static List<ResolveInfo> queryAplicationInfo(Context paramContext) {
        if (paramContext == null) {
            return null;
        }
        Intent intent = new Intent("android.intent.action.MAIN", (Uri) null);
        intent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> list = paramContext.getPackageManager().queryIntentActivities(intent, 0);
        ShareData.getInstance().setResolveInfos(list);
        Collections.sort(list, new ResolveInfo.DisplayNameComparator(paramContext.getPackageManager()));
        return list;
    }

    public static void updateAppInfoToDataBase(Context paramContext, boolean paramBoolean, String packagename, String activityname, String action) {
        ContentResolver cResolver = paramContext.getContentResolver();
        ContentValues cv = new ContentValues();
        if (paramBoolean) {
            cResolver.delete(LauncherDatabaseHelper.MostVisited.CONTENT_URI, null, null);
            for (ResolveInfo rinfo : queryAplicationInfo(paramContext)) {
                if (!rinfo.activityInfo.packageName.equals("com.jrm.localmm") && !rinfo.activityInfo.packageName.equals("com.android.inputmethod.latin") && !rinfo.activityInfo.packageName.equals("com.android.settings") && !rinfo.activityInfo.packageName.equals("com.mstar.tv.tvplayer.ui")) {
                    cv.clear();
                    cv.put("appname", rinfo.activityInfo.loadLabel(paramContext.getPackageManager()).toString());
                    cv.put("packagename", rinfo.activityInfo.packageName);
                    cv.put("activityname", rinfo.activityInfo.name);
                    cv.put("frequency", (Integer) 0);
                    cv.put("time", (Integer) 0);
                    cResolver.insert(LauncherDatabaseHelper.MostVisited.CONTENT_URI, cv);
                }
            }
        }
        if ("com.toptech.launcher.DataBaseAction.ACTION_INSERT".equals(action)) {
            ComponentName cName = new ComponentName(packagename, activityname);
            if (!packagename.equals("com.jrm.localmm") && !packagename.equals("com.android.inputmethod.latin") && !packagename.equals("com.android.settings") && !packagename.equals("com.mstar.tv.tvplayer.ui")) {
                try {
                    String insertname = paramContext.getPackageManager().getActivityInfo(cName, 0).loadLabel(paramContext.getPackageManager()).toString();
                    cv.clear();
                    cv.put("appname", insertname);
                    cv.put("packagename", packagename);
                    cv.put("activityname", activityname);
                    cv.put("frequency", (Integer) 0);
                    cv.put("time", (Integer) 0);
                    cResolver.insert(LauncherDatabaseHelper.MostVisited.CONTENT_URI, cv);
                } catch (PackageManager.NameNotFoundException e) {
                    Log.e(TAG, "updateAppInfoToDataBase -> getactivity error");
                    e.printStackTrace();
                }
            }
        } else if ("com.toptech.launcher.DataBaseAction.ACTION_REMOVE".equals(action)) {
            Log.d(TAG, "remove from database " + activityname);
            cResolver.delete(LauncherDatabaseHelper.MostVisited.CONTENT_URI, "activityname = ?", new String[]{activityname});
        } else if ("com.toptech.launcher.DataBaseAction.ACTION_UPDATE".equals(action)) {
            Cursor cursor = cResolver.query(LauncherDatabaseHelper.MostVisited.CONTENT_URI, new String[]{"_id", "packagename", "frequency", "time"}, "activityname = ?", new String[]{activityname}, null);
            if (cursor.moveToFirst()) {
                int i = cursor.getInt(cursor.getColumnIndex("frequency"));
                cv.clear();
                cv.put("frequency", Integer.valueOf(i + 1));
                cv.put("time", Long.valueOf(System.currentTimeMillis()));
                cResolver.update(LauncherDatabaseHelper.MostVisited.CONTENT_URI, cv, "activityname = ?", new String[]{activityname});
            }
            cursor.close();
        }
    }
}
