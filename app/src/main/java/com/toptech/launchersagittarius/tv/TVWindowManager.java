package com.toptech.launchersagittarius.tv;

import android.content.Context;
import android.util.Log;

public class TVWindowManager {
    private String TAG = getClass().getSimpleName();
    private Thread mThread = null;
    private TVView mTvView = null;

    public enum TV_STATE {
        OPEN,
        CLOSE
    }

    public TVWindowManager(Context context, int left, int top, int width, int height, TVView view) {
        Log.d("sch", "x,y,width,heiht======" + left + " " + top + " " + " " + width + " " + height);
        if (view == null) {
            Log.d("sch", "view===null");
            this.mTvView = new TVView(context, left, top, width, height);
            return;
        }
        Log.d("sch", "view==!!=null");
        this.mTvView = view;
    }

    public boolean getState() {
        return this.mTvView.getState();
    }

    public boolean resetTVWindow(boolean flage) {
        Log.d("sch", "resetTVWindow");
        return this.mTvView.scaleSmallWindow(flage);
    }

    public void setState(TV_STATE paramTV_STATE) {
        Log.d(this.TAG, "set state:" + paramTV_STATE);
        if (paramTV_STATE == TV_STATE.OPEN) {
            this.mTvView.setState(true);
        } else {
            this.mTvView.setState(false);
        }
    }

    public void checkState() {
        this.mTvView.checkView();
    }

    public void start() {
        Log.d(this.TAG, "start thread");
        this.mTvView.resetState();
        this.mThread = new Thread(new TVWatchingThread(this.mTvView));
        this.mThread.start();
    }

    public void stop() {
        Log.d(this.TAG, "stop thread");
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
    }
}
