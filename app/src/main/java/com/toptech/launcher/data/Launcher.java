package com.toptech.launcher.data;

import android.net.Uri;

public class Launcher {
    public static final Uri CONTENT_FILE_URI = Uri.parse("content://com.toptech.launcher/tbl_sagittarius_file");
    public static final Uri CONTENT_LOCATION_URI = Uri.parse("content://com.toptech.launcher/tbl_sagittarius_location");
    public static final Uri[] CONTENT_URI = {CONTENT_FILE_URI};
}
