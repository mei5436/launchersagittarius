package com.toptech.launcher.adapter;

import android.graphics.Canvas;
import android.view.View;
import com.toptech.launcher.ui.MainUpView;

public interface IAnimAdapter {
    MainUpView getMainUpView();

    boolean onDrawMainUpView(Canvas canvas);

    void onFocusView(View view, float f, float f2);

    void onInitAdapter(MainUpView mainUpView);

    void onOldFocusView(View view, float f, float f2);

    void setMainUpView(MainUpView mainUpView);
}
