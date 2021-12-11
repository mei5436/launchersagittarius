package com.toptech.launcher.ui;

import android.graphics.Typeface;
import java.util.HashMap;

/* compiled from: TextViewWithTTF */
class TypeFaceMgr {
    HashMap<String, Typeface> mTypefaces = new HashMap<>();

    public Typeface getTypeface(String aTTFName) {
        if (this.mTypefaces.containsKey(aTTFName)) {
            return this.mTypefaces.get(aTTFName);
        }
        try {
            Typeface font = Typeface.createFromFile("/system/fonts/" + aTTFName);
            this.mTypefaces.put(aTTFName, font);
            return font;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
