package com.toptech.launcher.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.IntentCompat;
import android.util.Log;
import com.toptech.launcher.PackageUpdateType;

public class PackageReceiver extends BroadcastReceiver {
    private static final String TAG = PackageReceiver.class.getSimpleName();
    private static final Intent serviceIntent = new Intent();
    private String[] r0;
    public static final String EXTRA_CHANGED_PACKAGE_LIST = "android.intent.extra.changed_package_list";
    public static final String EXTRA_CHANGED_UID_LIST = "android.intent.extra.changed_uid_list";

    public static final String ACTION_EXTERNAL_APPLICATIONS_AVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_AVAILABLE";
    public static final String ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE = "android.intent.action.EXTERNAL_APPLICATIONS_UNAVAILABLE";

    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, intent.getAction());
        String action = intent.getAction();
        if ("android.intent.action.PACKAGE_CHANGED".equals(action) || "android.intent.action.PACKAGE_REMOVED".equals(action) || "android.intent.action.PACKAGE_ADDED".equals(action)) {
            String schemeSpecificPart = intent.getData().getSchemeSpecificPart();
            boolean booleanExtra = intent.getBooleanExtra("android.intent.extra.REPLACING", false);
            Log.d(TAG, "replacing:" + booleanExtra + " packageName:" + schemeSpecificPart);
            int ordinal = PackageUpdateType.PACKAGE_UPDATE_TYPE_NONE.ordinal();
            if (schemeSpecificPart != null && schemeSpecificPart.length() != 0 && !schemeSpecificPart.equals("com.google.android.gms")) {
                if ("android.intent.action.PACKAGE_CHANGED".equals(action)) {
                    ordinal = PackageUpdateType.PACKAGE_UPDATE_TYPE_REPLACED.ordinal();
                } else if ("android.intent.action.PACKAGE_REMOVED".equals(action)) {
                    if (!booleanExtra) {
                        ordinal = PackageUpdateType.PACKAGE_UPDATE_TYPE_REMOVED.ordinal();
                        if (schemeSpecificPart.equals("com.skype.rover")) {
                            SharedPreferences.Editor edit = context.getSharedPreferences("MainActivity", 0).edit();
                            edit.putBoolean("com.toptech.launcher.ALL_APPSINFO_IN_DATABASE", false);
                            edit.commit();
                        }
                    }
                } else if ("android.intent.action.PACKAGE_ADDED".equals(action)) {
                    ordinal = !booleanExtra ? PackageUpdateType.PACKAGE_UPDATE_TYPE_ADDED.ordinal() : PackageUpdateType.PACKAGE_UPDATE_TYPE_REPLACED.ordinal();
                }
                if (ordinal != PackageUpdateType.PACKAGE_UPDATE_TYPE_NONE.ordinal()) {
                    serviceIntent.setClass(context, PackageUpdateService.class);
                    serviceIntent.putExtra("com.toptech.launcher.PACKAGE_UPDATE_TYPE", ordinal);
                    serviceIntent.putExtra("com.toptech.launcher.PACKAGE_NAME", new String[]{schemeSpecificPart});
                    Log.d(TAG, "PackageUpdateService update TYPE/NAME:" + ordinal + "/" + schemeSpecificPart);
                    context.startService(serviceIntent);
                }
            }
        } else if (ACTION_EXTERNAL_APPLICATIONS_AVAILABLE.equals(action)) {
            this.r0 = intent.getStringArrayExtra(EXTRA_CHANGED_PACKAGE_LIST);
            serviceIntent.setClass(context, PackageUpdateService.class);
            serviceIntent.putExtra("com.toptech.launcher.PACKAGE_UPDATE_TYPE", PackageUpdateType.PACKAGE_UPDATE_TYPE_ADDED.ordinal());
            serviceIntent.putExtra("com.toptech.launcher.PACKAGE_NAME", this.r0);
            context.startService(serviceIntent);
        } else if (ACTION_EXTERNAL_APPLICATIONS_UNAVAILABLE.equals(action)) {
            this.r0 = intent.getStringArrayExtra(EXTRA_CHANGED_PACKAGE_LIST);
            serviceIntent.setClass(context, PackageUpdateService.class);
            serviceIntent.putExtra("com.toptech.launcher.PACKAGE_UPDATE_TYPE", PackageUpdateType.PACKAGE_UPDATE_TYPE_REMOVED.ordinal());
            serviceIntent.putExtra("com.toptech.launcher.PACKAGE_NAME", this.r0);
            context.startService(serviceIntent);
        } else {
            if ("android.intent.action.LOCALE_CHANGED".equals(action) || "android.intent.action.CONFIGURATION_CHANGED".equals(action) || "android.search.action.GLOBAL_SEARCH_ACTIVITY_CHANGED".equals(action) || "android.search.action.SEARCHABLES_CHANGED".equals(action)) {
            }
        }
    }
}
