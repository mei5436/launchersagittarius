package com.toptech.launcher.app;

import android.app.IntentService;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import com.toptech.launcher.PackageUpdateType;
import com.toptech.launcher.data.LauncherDatabaseHelper;
import java.util.ArrayList;
import java.util.List;

public class PackageUpdateService extends IntentService {
    private static final String TAG = PackageUpdateService.class.getSimpleName();
    private PackageManager packageManager;

    public PackageUpdateService() {
        super("PackageUpdateService");
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        prepareUpdatePackageFromPkgName(intent.getIntExtra("com.toptech.launcher.PACKAGE_UPDATE_TYPE", 0), intent.getStringArrayExtra("com.toptech.launcher.PACKAGE_NAME"));
    }

    private void prepareUpdatePackageFromPkgName(int type, String[] pkgName) {
        this.packageManager = getPackageManager();
        if (this.packageManager != null) {
            Log.v(TAG, "type:" + type + " pkgName:" + pkgName);
            if (type == PackageUpdateType.PACKAGE_UPDATE_TYPE_ADDED.ordinal() || type == PackageUpdateType.PACKAGE_UPDATE_TYPE_REPLACED.ordinal()) {
                List<ResolveInfo> matches = findActivitiesForPackage(this, pkgName);
                if (matches.size() > 0) {
                    for (ResolveInfo info : matches) {
                        processPkg(info.activityInfo.packageName, info.activityInfo.name, type);
                    }
                }
            } else if (PackageUpdateType.PACKAGE_UPDATE_TYPE_REMOVED.ordinal() == type) {
                ContentResolver resolver = getContentResolver();
                for (int i = 0; i < pkgName.length; i++) {
                    resolver.delete(LauncherDatabaseHelper.MostVisited.CONTENT_URI, "packagename = ?", new String[]{pkgName[i]});
                }
                sendMSGToUI(PackageUpdateType.PACKAGE_UPDATE_TYPE_REMOVED.ordinal());
            }
        }
    }

    private void processPkg(String pkgName, String activity, int type) {
        if (type == PackageUpdateType.PACKAGE_UPDATE_TYPE_ADDED.ordinal()) {
            addPackageToDatabase(pkgName, activity);
        } else if (type == PackageUpdateType.PACKAGE_UPDATE_TYPE_REPLACED.ordinal()) {
            changePackageToDatabase(pkgName, activity);
        }
    }

    private static List<ResolveInfo> findActivitiesForPackage(Context context, String[] packageName) {
        PackageManager packageManager2 = context.getPackageManager();
        Intent mainIntent = new Intent("android.intent.action.MAIN", (Uri) null);
        mainIntent.addCategory("android.intent.category.LAUNCHER");
        List<ResolveInfo> apps = new ArrayList<>();
        for (String str : packageName) {
            mainIntent.setPackage(str);
            apps.addAll(packageManager2.queryIntentActivities(mainIntent, 0));
        }
        return apps;
    }

    private void addPackageToDatabase(String p, String a) {
        Log.v(TAG, "addPackageToDatabase in--------------:");
        PackageUtils.updateAppInfoToDataBase(getApplicationContext(), false, p, a, "com.toptech.launcher.DataBaseAction.ACTION_INSERT");
        sendMSGToUI(PackageUpdateType.PACKAGE_UPDATE_TYPE_ADDED.ordinal());
    }

    private void changePackageToDatabase(String p, String a) {
        Log.v(TAG, "updatePackageToDatabase in--------------:");
        PackageUtils.updateAppInfoToDataBase(getApplicationContext(), false, p, a, "com.toptech.launcher.DataBaseAction.ACTION_UPDATE");
        sendMSGToUI(PackageUpdateType.PACKAGE_UPDATE_TYPE_REPLACED.ordinal());
    }

    private void sendMSGToUI(int type) {
        Intent intent = new Intent("com.toptech.launcher.LOCAL_BROADCAST_PACKAGE_UPDATE");
        intent.putExtra("com.toptech.launcher.LOCAL_BROADCAST_PACKAGE_UPDATE_EXTRA", type);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
