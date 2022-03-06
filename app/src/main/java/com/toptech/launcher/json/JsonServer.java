package com.toptech.launcher.json;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import com.toptech.launcher.SagittariusTool;
import com.toptech.launcher.tasks.TaskRequestCommendListImages;
import com.toptech.launcher.tools.ConstantUtil;
import com.toptech.launcher.tools.MUrlTools;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class JsonServer extends IntentService {
    private String ALBUM_PATH;
    private final String TAG = "JsonServer";
    private ConnectivityManager connectivityManager;
    private SagittariusTool mSagittariusTool;
    private NetworkInfo netInfo;

    public JsonServer() {
        super("JsonServer");
    }

    public void onCreate() {
        this.connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        this.netInfo = this.connectivityManager.getActiveNetworkInfo();
        this.mSagittariusTool = SagittariusTool.getInstance();
        this.ALBUM_PATH = getCacheDir().getAbsolutePath() + "/Picture";
        File path = new File(this.ALBUM_PATH);
        if (!path.exists()) {
            path.mkdir();
        }
        this.mSagittariusTool.setPATH(this.ALBUM_PATH);
        this.mSagittariusTool = SagittariusTool.getInstance();
        super.onCreate();
    }

    /* access modifiers changed from: protected */
    public void onHandleIntent(Intent intent) {
        if (MUrlTools.ACTION_JSON_UPDATE_ARRAYLIST.equals(intent.getAction())) {
            onRequestCommendListImages();
        }
    }

    private void onRequestCommendListImages() {
        Log.e("JsonServer", "onRequestCommendListImages");
        TaskRequestCommendListImages.getInstance(getApplicationContext(), ConstantUtil.URL_COMMEND_LIST).execute(new Void[0]);
    }

    private void updateJsonList() {
        if (this.netInfo != null && this.netInfo.isConnected()) {
            this.mSagittariusTool.setJson(this.mSagittariusTool.connServerForResult(this.mSagittariusTool.strUrl));
            String mJson = this.mSagittariusTool.getJson();
            if (!mJson.equals("") && mJson.contains("allnum")) {
                Log.d("JsonServer", "JSON:   " + mJson);
                ArrayList<String> pnglist = new ArrayList<>();
                ArrayList<String> titlelist = new ArrayList<>();
                ArrayList<String> viewlist = new ArrayList<>();
                for (int i = 0; i < Integer.parseInt(this.mSagittariusTool.parseJson(mJson, "allnum")); i++) {
                    pnglist.add(this.mSagittariusTool.parseJsonMulti(mJson, Integer.toString(i + 1), "appico"));
                    titlelist.add(this.mSagittariusTool.parseJsonMulti(mJson, Integer.toString(i + 1), "apptitle"));
                    viewlist.add(this.mSagittariusTool.parseJsonMulti(mJson, Integer.toString(i + 1), "view"));
                }
                if (pnglist.size() > 0) {
                    this.mSagittariusTool.setArrrayJson(pnglist);
                    this.mSagittariusTool.setTitleArrrayJson(titlelist);
                    this.mSagittariusTool.setViewArrrayJson(viewlist);
                    try {
                        downloadJsonList();
                    } catch (IOException e) {
                        Log.e("sch", "failed to download recommendation");
                        e.printStackTrace();
                    }
                    Log.d("sch", "Succeed read string to SagittariusTool     count:  " + pnglist.size());
                }
            }
        }
    }

    private void downloadJsonList() throws IOException {
        ArrayList<String> pnglist = this.mSagittariusTool.getArrrayJson();
        if (pnglist.size() > 0) {
            for (int i = 0; i < pnglist.size(); i++) {
                Bitmap mBitmap = this.mSagittariusTool.returnBitMap(pnglist.get(i));
                if (mBitmap != null) {
                    saveFile(mBitmap, i + ".jpg");
                }
                if (mBitmap != null && mBitmap.isRecycled()) {
                    mBitmap.recycle();
                }
            }
        }
    }

    private void saveFile(Bitmap bm, String fileName) throws IOException {
        File dirFile = new File(this.ALBUM_PATH);
        if (!dirFile.exists()) {
            dirFile.mkdir();
        }
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(new File(this.ALBUM_PATH + File.separator + fileName)));
        bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
        bos.flush();
        bos.close();
    }
}
