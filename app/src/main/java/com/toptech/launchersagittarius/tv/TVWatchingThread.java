package com.toptech.launchersagittarius.tv;

public class TVWatchingThread implements Runnable {
    private TVView mTvView;

    public TVWatchingThread(TVView view) {
        this.mTvView = view;
    }

    public void run() {
        while (!Thread.interrupted()) {
            try {
                this.mTvView.waitForStateChange();
                this.mTvView.updateState();
            } catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
        }
    }
}
