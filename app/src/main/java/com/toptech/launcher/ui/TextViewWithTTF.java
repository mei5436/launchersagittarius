package com.toptech.launcher.ui;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.TextView;
import com.toptech.launchersagittarius.R;

public class TextViewWithTTF extends TextView {
    private static final TypeFaceMgr SFontMgr = new TypeFaceMgr();

    public TextViewWithTTF(Context context) {
        super(context);
    }

    public TextViewWithTTF(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.TextViewWithTTF);
        setTypeface(SFontMgr.getTypeface(array.getString(0)));
        array.recycle();
    }

    public TextViewWithTTF(Context context, String aTTFName) {
        super(context);
        setTypeface(SFontMgr.getTypeface(aTTFName));
    }

    public void setFont(String aTTFName) {
        setTypeface(SFontMgr.getTypeface(aTTFName));
    }
}
