package com.toptech.launcher.tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import com.toptech.launcher.beans.CommendBean;
import com.toptech.launcher.tools.ConstantUtil;
import com.toptech.launcher.tools.FileUtil;
import com.toptech.launcher.tools.JsonParser;
import com.toptech.launcher.tools.Network;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

public class TaskRequestCommendListImages extends AsyncTask<Void, Void, Void> {
    private static final String TAG = "TaskRequestCommendListImages";
    private static TaskRequestCommendListImages mInstance = null;
    private static final Object mLock = new Object();
    private Context context;
    private ArrayList<CommendBean> mCommendList;
    private String url;

    public static TaskRequestCommendListImages getInstance(Context context2, String url2) {
        synchronized (mLock) {
            if (mInstance != null) {
                if (mInstance.getStatus() == AsyncTask.Status.RUNNING) {
                    Log.e(TAG, "task cancel(true)");
                    mInstance.cancel(true);
                }
                mInstance = null;
            }
            mInstance = new TaskRequestCommendListImages(context2, url2);
        }
        return mInstance;
    }

    public TaskRequestCommendListImages(Context context2, String url2) {
        this.context = context2;
        this.url = url2;
    }

    /* access modifiers changed from: protected */
    public Void doInBackground(Void... params) {
        String old_json = FileUtil.readFileData(FileUtil.getCachePath(this.context, FileUtil.JSON_FILES_PATH) + FileUtil.JSON_FILE_NAME_COMMEND_LIST);
        ArrayList<CommendBean> list = null;
        if (old_json != null && !old_json.equals("")) {
            list = JsonParser.getCommendBeanList(old_json);
        }
        String json = doGet();
        Log.e(TAG, "json -->> " + json);
        if (json == null || json.equals("")) {
            return null;
        }
        this.mCommendList = JsonParser.getCommendBeanList(json);
        if (onDownLoadImages(list, JsonParser.getCommendListTimeStamp(json))) {
            FileUtil.writeFileData(FileUtil.getCachePath(this.context, FileUtil.JSON_FILES_PATH) + FileUtil.JSON_FILE_NAME_COMMEND_LIST, json);
        } else {
            Log.e(TAG, "onDownload error");
        }
        Log.e(TAG, "action -->> ACTION_UPDATE_COMMEND_LIST");
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra("mCommendList", this.mCommendList);
        intent.setAction(ConstantUtil.ACTION_UPDATE_COMMEND_LIST);
        this.context.sendStickyBroadcast(intent);
        return null;
    }

    private boolean onDownLoadImages(ArrayList<CommendBean> list, ArrayList<String> mTimeStamps) {
        boolean status = true;
        String dir = FileUtil.getCachePath(this.context, FileUtil.IMAGE_PATH + FileUtil.DIR_COMMEND_LIST);
        Log.e(TAG, "dir -->> " + dir);
        if (list != null) {
            Iterator i$ = list.iterator();
            while (i$.hasNext()) {
                CommendBean mCommendBean = (CommendBean) i$.next();
                if (mTimeStamps == null || !mTimeStamps.contains(mCommendBean.mtime)) {
                    Log.e(TAG, "remove file -->> " + dir + "/" + mCommendBean.pic_name);
                    FileUtil.onDeleteFiles(new File(dir + "/" + mCommendBean.pic_name));
                } else {
                    Log.e(TAG, mCommendBean.pic_name + " has download!");
                    mTimeStamps.remove(mCommendBean.mtime);
                }
            }
        } else {
            Log.e(TAG, "list = null !");
        }
        if (this.mCommendList != null) {
            Iterator i$2 = this.mCommendList.iterator();
            while (i$2.hasNext()) {
                CommendBean mCommendBean2 = (CommendBean) i$2.next();
                if (mCommendBean2 != null && (mTimeStamps == null || mTimeStamps.contains(mCommendBean2.mtime))) {
                    Bitmap bitmap = Network.onDownloadBitmap(mCommendBean2.pic);
                    if (bitmap != null) {
                        boolean b = FileUtil.onSaveImage(bitmap, dir + "/" + mCommendBean2.pic_name);
                        if (status) {
                            status = b;
                        }
                        if (!bitmap.isRecycled()) {
                            bitmap.recycle();
                            System.gc();
                        }
                    } else {
                        Log.e(TAG, "bitmap = null");
                        status = false;
                    }
                }
            }
            return status;
        }
        Log.e(TAG, "mCommendList = null !");
        return false;
    }

    /* access modifiers changed from: protected */
    public void onPostExecute(Void result) {
        super.onPostExecute((Void) result);
        mInstance = null;
    }

    private String doGet() {
        IOException e;
        ClientProtocolException e2;
        String strResult = "";
        try {
            try {
                HttpResponse httpResponse = new DefaultHttpClient().execute(new HttpGet(this.url));
                Log.e("zoipuus", "HttpStatus -->> " + httpResponse.getStatusLine().getStatusCode());
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                }
            } catch (ClientProtocolException e3) {
                e2 = e3;
                Log.e("sch", "protocol error");
                e2.printStackTrace();
                return strResult;
            } catch (IOException e4) {
                e = e4;
                Log.e("sch", "IO error");
                e.printStackTrace();
                return strResult;
            }
        } catch (Exception e5) {
            Log.e("sch", "protocol error");
            e5.printStackTrace();
            return strResult;
        }
        return strResult;
    }
}
