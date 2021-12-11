package com.toptech.launcher.app;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListAdapter;
import android.widget.Toast;
import com.toptech.launchersagittarius.R;

public class AllAppListActivity extends Activity {
    private static final String TAG = "AllAppListActivity";
    private GridView appGridView;
    private AllAppAdapter mAllAppAdapter;

    /* access modifiers changed from: protected */
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.allapplication);
        this.appGridView = (GridView) findViewById(R.id.appgridview);
        initGridView();
        this.appGridView.setSelection(0);
    }

    private void initGridView() {
        getPackageManager();
        new Intent("android.intent.action.MAIN", (Uri) null).addCategory("android.intent.category.LAUNCHER");
        this.mAllAppAdapter = new AllAppAdapter(this, null);
        this.appGridView.setAdapter((ListAdapter) this.mAllAppAdapter);
        this.appGridView.setOnItemClickListener(new AllAppItemcListener());
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i(TAG, "++++++++++++++-onKeyDown++++++++++++++");
        return super.onKeyDown(keyCode, event);
    }

    /* access modifiers changed from: private */
    public class AllAppItemcListener implements AdapterView.OnItemClickListener {
        private AllAppItemcListener() {
        }

        @Override // android.widget.AdapterView.OnItemClickListener
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ResolveInfo appInfo = (ResolveInfo) parent.getItemAtPosition(position);
            Intent mIntent = AllAppListActivity.this.getPackageManager().getLaunchIntentForPackage(appInfo.activityInfo.packageName);
            if (mIntent != null) {
                AllAppListActivity.this.showtoast(appInfo.activityInfo.packageName);
                mIntent.addFlags(268435456);
                try {
                    AllAppListActivity.this.startActivity(mIntent);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AllAppListActivity.this, AllAppListActivity.this.getResources().getString(R.string.not_found), 0).show();
                }
            } else {
                Toast.makeText(AllAppListActivity.this, AllAppListActivity.this.getResources().getString(R.string.not_found), 0).show();
            }
        }
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void showtoast(String packName) {
        if (packName != null && packName.contentEquals("com.tencent.hd.qq")) {
            Toast.makeText(this, getResources().getString(R.string.use_mouse), 1).show();
        }
    }
}
