package com.toptech.launchersagittarius.tv;

import android.util.Log;
import java.io.IOException;

public class GetFileDataFromBottom {
    public final String TAG = "GetFileDataFromBottom";

    public static String getFileDataString(String filename, String section, String variable, String defaultValue) {
        String string = null;
        try {
            string = IniFileReadWrite.getProfileString(filename, section, variable, defaultValue);
            Log.d("zsr", "get: " + string);
            return string;
        } catch (IOException e) {
            Log.d("TAG", "error: " + e.toString());
            e.printStackTrace();
            return string;
        }
    }
}
