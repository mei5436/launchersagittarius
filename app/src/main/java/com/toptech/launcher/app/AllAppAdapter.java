package com.toptech.launcher.app;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.toptech.launchersagittarius.R;

public class AllAppAdapter extends CursorAdapter {
    Context context;

    public AllAppAdapter(Context context2, Cursor c) {
        super(context2, c, true);
        this.context = context2;
    }

    @SuppressLint({"NewApi"})
    public View newView(Context context2, Cursor cursor, ViewGroup parent) {
        AppItemViewHolder appitem = new AppItemViewHolder();
        View view = LayoutInflater.from(context2).inflate(R.layout.app_item, (ViewGroup) null);
        appitem.icon = (ImageView) view.findViewById(R.id.app_icon);
        appitem.name = (TextView) view.findViewById(R.id.app_name);
        appitem.appitem = (RelativeLayout) view.findViewById(R.id.appitem);
        view.setTag(appitem);
        return view;
    }

    public void bindView(View view, Context context2, Cursor cursor) {
        Drawable icon = null;
        String lable = null;
        AppItemViewHolder localAppItemViewHolder = (AppItemViewHolder) view.getTag();
        cursor.getInt(cursor.getColumnIndex("frequency"));
        String packagename = cursor.getString(cursor.getColumnIndex("packagename"));
        String activityname = cursor.getString(cursor.getColumnIndex("activityname"));
        try {
            icon = context2.getPackageManager().getActivityIcon(new ComponentName(packagename, activityname));
            lable = context2.getPackageManager().getActivityInfo(new ComponentName(packagename, activityname), 0).loadLabel(context2.getPackageManager()).toString();
            if (lable == null) {
                lable = context2.getPackageManager().getApplicationLabel(context2.getPackageManager().getApplicationInfo(activityname, 0)).toString();
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("SCH20150821", "getactionicon error");
            e.printStackTrace();
        }
        if (icon != null) {
            localAppItemViewHolder.icon.setImageDrawable(icon);
        }
        localAppItemViewHolder.appitem.setBackground(getDrawable(((int) (Math.random() * 255.0d)) + 1, ((int) (Math.random() * 255.0d)) + 1, ((int) (Math.random() * 255.0d)) + 1));
        localAppItemViewHolder.name.setText(lable);
        localAppItemViewHolder.activityName = activityname;
        localAppItemViewHolder.packageName = packagename;
    }

    private Drawable getDrawable(int r, int g, int b) {
        GradientDrawable draw = (GradientDrawable) this.context.getResources().getDrawable(R.drawable.appitemcolor);
        draw.setColor(Color.rgb(r, g, b));
        return draw;
    }
}
