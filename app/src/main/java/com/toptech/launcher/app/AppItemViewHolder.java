package com.toptech.launcher.app;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.toptech.launchersagittarius.R;
import java.util.ArrayList;

public class AppItemViewHolder {
    public String activityName;
    public ArrayList<Integer> appColor = new ArrayList<>();
    public RelativeLayout appitem;
    public ImageView icon;
    public TextView name;
    public String packageName;
    public int size;

    public AppItemViewHolder() {
        init();
    }

    private void init() {
        this.appColor.clear();
        this.appColor.add(Integer.valueOf((int) R.drawable.appbisque));
        this.appColor.add(Integer.valueOf((int) R.drawable.appblue));
        this.appColor.add(Integer.valueOf((int) R.drawable.appcrimson));
        this.appColor.add(Integer.valueOf((int) R.drawable.appdarkblue));
        this.appColor.add(Integer.valueOf((int) R.drawable.appdeeppink));
        this.appColor.add(Integer.valueOf((int) R.drawable.applightblue));
        this.appColor.add(Integer.valueOf((int) R.drawable.appnavy));
        this.appColor.add(Integer.valueOf((int) R.drawable.apporangered));
        this.appColor.add(Integer.valueOf((int) R.drawable.appred));
        this.appColor.add(Integer.valueOf((int) R.drawable.apptomato));
        this.size = this.appColor.size();
    }

    private Drawable getDrawable(int id) {
        ShapeDrawable shap = new ShapeDrawable(new RoundRectShape(new float[]{12.0f, 12.0f, 12.0f, 12.0f, 0.0f, 0.0f, 0.0f, 0.0f}, null, null));
        shap.getPaint().setColor(id);
        return shap;
    }
}
