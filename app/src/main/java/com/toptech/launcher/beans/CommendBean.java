package com.toptech.launcher.beans;

import android.os.Parcel;
import android.os.Parcelable;

public class CommendBean implements Parcelable {
    public static final Parcelable.Creator<CommendBean> CREATOR = new Parcelable.Creator<CommendBean>() {
        /* class com.toptech.launcher.beans.CommendBean.AnonymousClass1 */

        @Override // android.os.Parcelable.Creator
        public CommendBean createFromParcel(Parcel source) {
            CommendBean mCommendBean = new CommendBean();
            mCommendBean.pic = source.readString();
            mCommendBean.view = source.readString();
            mCommendBean.title = source.readString();
            mCommendBean.mtime = source.readString();
            mCommendBean.pic_name = source.readString();
            mCommendBean.packname = source.readString();
            return mCommendBean;
        }

        @Override // android.os.Parcelable.Creator
        public CommendBean[] newArray(int size) {
            return new CommendBean[size];
        }
    };
    public String mtime;
    public String packname;
    public String pic;
    public String pic_name;
    public String title;
    public String view;

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.pic);
        dest.writeString(this.view);
        dest.writeString(this.title);
        dest.writeString(this.mtime);
        dest.writeString(this.pic_name);
        dest.writeString(this.packname);
    }
}
