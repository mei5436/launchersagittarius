package com.toptech.launcher.tools;

import android.util.Log;
import com.toptech.launcher.beans.CommendBean;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JsonParser {
    private static final String TAG = "JsonParser";

    public static ArrayList<CommendBean> getCommendBeanList(String json) {
        ArrayList<CommendBean> list = null;
        try {
            JSONArray mArray = new JSONArray(json);
            if (mArray == null || mArray.length() <= 0) {
                Log.e(TAG, "json_string has no data!");
                return null;
            }
            Log.e(TAG, "JSONArray -->> " + mArray);
            ArrayList<CommendBean> list2 = new ArrayList<>();
            for (int index = 0; index < mArray.length(); index++) {
                JSONObject mObject = mArray.optJSONObject(index);
                if (mObject != null) {
                    list2.add(getCommendBean(mObject));
                } else {
                    Log.e(TAG, "mObject -->> null");
                }
            }
            return list2;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return list;
        }
    }

    private static CommendBean getCommendBean(JSONObject mObject) {
        Log.e(TAG, "mObject -->> " + mObject);
        CommendBean mCommendBean = new CommendBean();
        mCommendBean.pic = mObject.optString("pic", "http://img.znds.com/uploads/new/160723/9-160H30J043317.png");
        mCommendBean.view = mObject.optString("view", "http://down.znds.com/dbapinew/view.php?id=14");
        mCommendBean.title = mObject.optString("title", "推荐列表");
        mCommendBean.mtime = mObject.optString("mtime", "0");
        if (mObject.optString("pic").length() > 1) {
            mCommendBean.pic_name = mObject.optString("pic_name", mCommendBean.pic.substring(mCommendBean.pic.lastIndexOf("/") + 1, mCommendBean.pic.length()));
        }
        mCommendBean.packname = mObject.optString("packname", "net.myvst.v2");
        return mCommendBean;
    }

    public static ArrayList<String> getCommendListTimeStamp(String old_json) {
        ArrayList<String> list = null;
        try {
            JSONArray mArray = new JSONArray(old_json);
            if (mArray == null || mArray.length() <= 0) {
                Log.e(TAG, "json_string has no data!");
                return null;
            }
            Log.e(TAG, "JSONArray -->> " + mArray);
            ArrayList<String> list2 = new ArrayList<>();
            for (int index = 0; index < mArray.length(); index++) {
                try {
                    JSONObject mObject = mArray.optJSONObject(index);
                    if (mObject != null) {
                        list2.add(mObject.optString("mtime", "0"));
                    } else {
                        Log.e(TAG, "mObject -->> null");
                    }
                } catch (Exception e) {
                    e = e;
                    list = list2;
                    e.printStackTrace();
                    return list;
                }
            }
            return list2;
        } catch (JSONException e2) {
            e2.printStackTrace();
            return list;
        }
    }
}
