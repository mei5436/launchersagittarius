package com.toptech.launcher;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import com.toptech.launcher.tools.ConstantUtil;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class SagittariusTool {
    private static SagittariusTool mSagittariusTool;
    private String ALBUM_PATH;
    private String mJson;
    private ArrayList<String> mJsonList = new ArrayList<>();
    private ArrayList<String> mViewList = new ArrayList<>();
    private ArrayList<String> mtitleList = new ArrayList<>();
    public String strUrl = ConstantUtil.URL_COMMEND_LIST;

    public static SagittariusTool getInstance() {
        if (mSagittariusTool == null) {
            mSagittariusTool = new SagittariusTool();
        }
        return mSagittariusTool;
    }

    public void setJson(String str) {
        this.mJson = str;
    }

    public String getJson() {
        return this.mJson;
    }

    public void setArrrayJson(ArrayList<String> strlist) {
        this.mJsonList.clear();
        this.mJsonList.addAll(strlist);
    }

    public ArrayList<String> getArrrayJson() {
        return this.mJsonList;
    }

    public void setTitleArrrayJson(ArrayList<String> strlist) {
        this.mtitleList.clear();
        this.mtitleList.addAll(strlist);
    }

    public ArrayList<String> getTitleArrrayJson() {
        return this.mtitleList;
    }

    public void setViewArrrayJson(ArrayList<String> strlist) {
        this.mViewList.clear();
        this.mViewList.addAll(strlist);
    }

    public ArrayList<String> getViewArrrayJson() {
        return this.mViewList;
    }

    public void setPATH(String str) {
        this.ALBUM_PATH = str;
    }

    public String getPATH() {
        return this.ALBUM_PATH;
    }

    /* JADX WARNING: Removed duplicated region for block: B:20:0x005d A[SYNTHETIC, Splitter:B:20:0x005d] */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0062  */
    /* JADX WARNING: Removed duplicated region for block: B:29:0x008d A[SYNTHETIC, Splitter:B:29:0x008d] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0092  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x009e A[SYNTHETIC, Splitter:B:37:0x009e] */
    /* JADX WARNING: Removed duplicated region for block: B:40:0x00a3  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.Bitmap returnBitMap(java.lang.String r11) {
        /*
        // Method dump skipped, instructions count: 184
        */
        throw new UnsupportedOperationException("Method not decompiled: com.toptech.launcher.SagittariusTool.returnBitMap(java.lang.String):android.graphics.Bitmap");
    }

    public String connServerForResult(String strUrl2) {
        CloneNotSupportedException e;
        IOException e2;
        ClientProtocolException e3;
        HttpGet httpRequest = new HttpGet(strUrl2);
        String strResult = "";
        try {
            try {
                HttpResponse httpResponse = new DefaultHttpClient().execute(httpRequest);
                if (httpResponse.getStatusLine().getStatusCode() == 200) {
                    strResult = EntityUtils.toString(httpResponse.getEntity());
                }
                httpRequest.clone();
            } catch (ClientProtocolException e4) {
                e3 = e4;
                Log.e("sch", "protocol error");
                e3.printStackTrace();
                return strResult;
            } catch (IOException e5) {
                e2 = e5;
                Log.e("sch", "IO error");
                e2.printStackTrace();
                return strResult;
            } catch (CloneNotSupportedException e6) {
                e = e6;
                e.printStackTrace();
                return strResult;
            }
        } catch (Exception e7) {
            Log.e("sch", "protocol error");
            e7.printStackTrace();
            return strResult;
        }
        return strResult;
    }

    public String parseJson(String strResult, String name) {
        try {
            JSONObject jsonObj = new JSONObject(strResult);
            if (jsonObj != null) {
                return jsonObj.optString(name, "");
            }
        } catch (JSONException e) {
            Log.e("sch", "Json parse error");
            e.printStackTrace();
        }
        return "0";
    }

    public String parseJsonMulti(String strResult, String array, String value) {
        try {
            JSONObject jsonObjs = new JSONObject(strResult);
            if (!(jsonObjs == null || jsonObjs.optJSONObject(array) == null)) {
                return jsonObjs.optJSONObject(array).optString(value, "");
            }
        } catch (JSONException e) {
            Log.e("sch", "Jsons parse error !");
            e.printStackTrace();
        }
        return "";
    }

    public Bitmap getLocalBitmap(String name) {
        if (this.ALBUM_PATH != null) {
            File path = new File(this.ALBUM_PATH);
            if (path.listFiles() != null && path.listFiles().length > 0) {
                String file = new StringBuffer(this.ALBUM_PATH + File.separator + name).toString();
                if (new File(file).exists()) {
                    return BitmapFactory.decodeFile(file);
                }
            }
        }
        return null;
    }
}
