package com.toptech.launcher.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import com.toptech.launcher.adapter.IAnimAdapter;
import com.toptech.launcher.adapter.OpenBaseAnimAdapter;
import com.toptech.launchersagittarius.R;

public class MainUpView extends FrameLayout {
    private static final float DEFUALT_SCALE = 1.0f;
    private static final int DEFULAT_SIZE = 500;
    private static final String TAG = "MainUpView";
    IAnimAdapter mAnimAdapter;
    private Context mContext;
    private Drawable mDrawableShadow;
    private Drawable mDrawableUpRect;
    private Rect mShadowPaddingRect = new Rect(0, 0, 0, 0);
    private Rect mUpPaddingRect = new Rect(0, 0, 0, 0);

    public MainUpView(Context context) {
        super(context, null, 0);
        init(context, null);
    }

    public MainUpView(Context context, View view) {
        super(context, null, 0);
        ViewGroup viewGroup;
        if (!(view == null || (viewGroup = (ViewGroup) view.getRootView()) == null || getParent() == viewGroup)) {
            viewGroup.addView(this, new FrameLayout.LayoutParams((int) DEFULAT_SIZE, (int) DEFULAT_SIZE));
        }
        init(context, null);
    }

    public MainUpView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init(context, attrs);
    }

    public MainUpView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        setWillNotDraw(false);
        this.mContext = context;
        try {
            this.mDrawableUpRect = this.mContext.getResources().getDrawable(R.drawable.white_light_10);
        } catch (Exception e) {
            Log.d(TAG, "get mDrawableUpRect res failed");
            e.printStackTrace();
        }
        if (attrs != null) {
            TypedArray tArray = context.obtainStyledAttributes(attrs, R.styleable.MainUpView);
            int upImageRes = tArray.getResourceId(2, 0);
            if (upImageRes != 0) {
                this.mDrawableUpRect = context.getResources().getDrawable(upImageRes);
            }
            int shadowImageRes = tArray.getResourceId(3, 0);
            if (shadowImageRes != 0) {
                this.mDrawableShadow = context.getResources().getDrawable(shadowImageRes);
            }
            tArray.recycle();
        }
        IAnimAdapter baseAnimAdapter = new OpenBaseAnimAdapter();
        baseAnimAdapter.onInitAdapter(this);
        baseAnimAdapter.setMainUpView(this);
        setAnimAdapter(baseAnimAdapter);
    }

    public void setUpRectResource(int resId) {
        try {
            this.mDrawableUpRect = this.mContext.getResources().getDrawable(resId);
            invalidate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setcwidth(int width) {
        getLayoutParams().width = width;
        requestLayout();
    }

    public int getcwidth() {
        return getLayoutParams().width;
    }

    public void setcheight(int height) {
        getLayoutParams().height = height;
        requestLayout();
    }

    public int getcheight() {
        return getLayoutParams().height;
    }

    public void setUpRectDrawable(Drawable upRectDrawable) {
        this.mDrawableUpRect = upRectDrawable;
        invalidate();
    }

    public Drawable getUpRectDrawable() {
        return this.mDrawableUpRect;
    }

    public void setShadowResource(int resId) {
        try {
            this.mDrawableShadow = this.mContext.getResources().getDrawable(resId);
            invalidate();
        } catch (Exception e) {
            this.mDrawableShadow = null;
            e.printStackTrace();
        }
    }

    public Drawable getShadowDrawable() {
        return this.mDrawableShadow;
    }

    public void setShadowDrawable(Drawable shadowDrawable) {
        this.mDrawableShadow = shadowDrawable;
        invalidate();
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        if (this.mAnimAdapter == null || !this.mAnimAdapter.onDrawMainUpView(canvas)) {
            super.onDraw(canvas);
        }
    }

    public void setDrawShadowPadding(int size) {
        setDrawShadowRectPadding(new Rect(size, size, size, size));
    }

    public void setDrawShadowRectPadding(Rect rect) {
        this.mShadowPaddingRect.set(rect);
        invalidate();
    }

    public Rect getDrawShadowRect() {
        return this.mShadowPaddingRect;
    }

    public void setDrawUpRectPadding(int size) {
        setDrawUpRectPadding(new Rect(size, size, size, size));
    }

    public void setDrawUpRectPadding(Rect rect) {
        this.mUpPaddingRect.set(rect);
        invalidate();
    }

    public Rect getDrawUpRect() {
        return this.mUpPaddingRect;
    }

    public void setFocusView(View view, float scale) {
        this.mAnimAdapter.onFocusView(view, scale, scale);
    }

    public void setFocusView(View view, float scaleX, float scaleY) {
        if (this.mAnimAdapter != null) {
            this.mAnimAdapter.onFocusView(view, scaleX, scaleY);
        }
    }

    public void setFocusView(View newView, View oldView, float scale) {
        setFocusView(newView, scale);
        setUnFocusView(oldView);
    }

    public void setUnFocusView(View view, float scaleX, float scaleY) {
        if (this.mAnimAdapter != null) {
            this.mAnimAdapter.onOldFocusView(view, scaleX, scaleY);
        }
    }

    public void setUnFocusView(View view) {
        setUnFocusView(view, DEFUALT_SCALE, DEFUALT_SCALE);
    }

    public void setAnimAdapter(IAnimAdapter adapter) {
        this.mAnimAdapter = adapter;
        if (this.mAnimAdapter != null) {
            this.mAnimAdapter.onInitAdapter(this);
            this.mAnimAdapter.setMainUpView(this);
            invalidate();
        }
    }

    public IAnimAdapter getAnimAdapter() {
        return this.mAnimAdapter;
    }
}
