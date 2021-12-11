package com.toptech.launcher;

import android.content.pm.ResolveInfo;
import android.support.v4.app.Fragment;
import java.util.ArrayList;
import java.util.List;

public class ShareData {
    private static ShareData mShareData;
    private int PAGE_COUNT = 3;
    private int mCurrentInpute = 44;
    private int mCurrentPage = 1;
    private List<Fragment> mMainPageFragments = new ArrayList();
    private List<ResolveInfo> mResolveInfos = new ArrayList();

    public static ShareData getInstance() {
        if (mShareData == null) {
            mShareData = new ShareData();
        }
        return mShareData;
    }

    public List<Fragment> getMainPageFragments() {
        return this.mMainPageFragments;
    }

    public int getPageCount() {
        return this.PAGE_COUNT;
    }

    public void setResolveInfos(List<ResolveInfo> paramList) {
        this.mResolveInfos = paramList;
    }

    public List<ResolveInfo> setResolveInfos() {
        return this.mResolveInfos;
    }

    public void setmCurrentPage(int num) {
        if (num < 3 && num >= 0) {
            this.mCurrentPage = num;
        }
    }

    public int getmCurrentPage() {
        return this.mCurrentPage;
    }

    public void setmCurrentInput(int num) {
        this.mCurrentInpute = num;
    }

    public int getmCurrentInput() {
        return this.mCurrentInpute;
    }
}
