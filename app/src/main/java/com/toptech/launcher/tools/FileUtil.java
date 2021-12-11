package com.toptech.launcher.tools;

import android.content.Context;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.apache.http.util.EncodingUtils;

public class FileUtil {
    public static final String DIR_COMMEND_LIST = "/CommendList";
    public static final String DIR_NORMAL_IMAGE_PATH = "/Pictures";
    public static final String DIR_NORMAL_JSON_FILES_PATH = "/JsonFiles";
    public static final String DIR_TEST = "/Test";
    public static String IMAGE_PATH = DIR_NORMAL_IMAGE_PATH;
    public static String JSON_FILES_PATH = DIR_NORMAL_JSON_FILES_PATH;
    public static final String JSON_FILE_NAME_COMMEND_LIST = "/CommendList.txt";

    public static void writeFileData(String fileName, String message) {
        try {
            FileOutputStream fout = new FileOutputStream(fileName);
            fout.write(message.getBytes());
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String readFileData(String fileName) {
        if (!new File(fileName).exists()) {
            return null;
        }
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(fileName);
            byte[] buffer = new byte[fin.available()];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
            return res;
        } catch (Exception e) {
            e.printStackTrace();
            return res;
        }
    }

    public static File[] readFiles(String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            return dir.listFiles();
        }
        return null;
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0044 A[SYNTHETIC, Splitter:B:22:0x0044] */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x0055 A[SYNTHETIC, Splitter:B:29:0x0055] */
    /* JADX WARNING: Removed duplicated region for block: B:35:0x0062 A[SYNTHETIC, Splitter:B:35:0x0062] */
    /* JADX WARNING: Removed duplicated region for block: B:49:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:51:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static boolean onSaveImage(android.graphics.Bitmap r8, java.lang.String r9) {
        /*
        // Method dump skipped, instructions count: 119
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toptech.launcher.tools.FileUtil.onSaveImage(android.graphics.Bitmap, java.lang.String):boolean");
    }

    public static void onDeleteFiles(File dir) {
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                onDeleteFiles(file);
            }
        } else if (dir.isFile()) {
            dir.delete();
        }
    }

    public static String getCachePath(Context context, String path) {
        String path2 = context.getCacheDir().getAbsolutePath() + path;
        File dir = new File(path2);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return path2;
    }

    public static String getCachePath(Context context, String path, String dir1) {
        String path2 = getCachePath(context, path);
        File dir = new File(path2);
        if (!dir.exists()) {
            dir.mkdir();
        }
        return path2;
    }
}
