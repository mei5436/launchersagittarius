package com.toptech.launcher.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RelativeLayout;

public class MainLayout extends RelativeLayout {
    private int position = 0;

    public MainLayout(Context context) {
        super(context, null, 0);
        init(context);
    }

    public MainLayout(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context);
    }

    public MainLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        setClipChildren(false);
        setClipToPadding(false);
        setChildrenDrawingOrderEnabled(true);
    }

    public void bringChildToFront(View child) {
        this.position = indexOfChild(child);
        if (this.position != -1) {
            postInvalidate();
        }
    }

    /* access modifiers changed from: protected */
    public int getChildDrawingOrder(int childCount, int i) {
        if (this.position == -1) {
            return i;
        }
        if (i == childCount - 1) {
            return this.position;
        }
        if (i == this.position) {
            return childCount - 1;
        }
        return i;
    }
}
