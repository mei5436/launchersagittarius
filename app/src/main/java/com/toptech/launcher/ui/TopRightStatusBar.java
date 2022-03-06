package com.toptech.launcher.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Canvas;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Environment;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;

import com.toptech.launcher.ConstantResource;
import com.toptech.launcher.net.NetworkMonitor;
import com.toptech.launchersagittarius.R;

import java.io.File;

public class TopRightStatusBar extends LinearLayout implements NetworkMonitor.INetworkUpdateListener {
    private static final String TAG = "TopRightStatusBar";
    private Context mContext = null;
    private ImageView mNetworkStatusImageView;
    private TextClock mTextClock;
    public BroadcastReceiver mUsbListener = new BroadcastReceiver() {
        /* class com.toptech.launcher.ui.TopRightStatusBar.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            File[] file;
            String action = intent.getAction();
            Log.d(TopRightStatusBar.TAG, "mUsbListener " + action);
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                TopRightStatusBar.this.mUsbStatusImageView.setImageResource(R.drawable.conn_stat_usb_connected);
            }
            if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                TopRightStatusBar.this.mUsbStatusImageView.setImageResource(R.drawable.conn_stat_usb_disconnected);
                for (File file2 : new File("//mnt//usb").listFiles()) {
                    if (Environment.getStorageState(file2).equals("mounted")) {
                        TopRightStatusBar.this.mUsbStatusImageView.setImageResource(R.drawable.conn_stat_usb_connected);
                        return;
                    }
                }
            }
        }
    };
    private ImageView mUsbStatusImageView;

    public TopRightStatusBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        View mBarLayour = LayoutInflater.from(context).inflate(R.layout.top_layout_status, (ViewGroup) this, true);
        setOrientation(0);
        this.mUsbStatusImageView = (ImageView) mBarLayour.findViewById(R.id.top_right_usb_state);
        this.mNetworkStatusImageView = (ImageView) mBarLayour.findViewById(R.id.top_right_network_state);
        this.mTextClock = (TextClock) mBarLayour.findViewById(R.id.top_right_clock);
        this.mNetworkStatusImageView.setFocusable(false);
        this.mUsbStatusImageView.setFocusable(false);
        IntentFilter filter = new IntentFilter();
        filter.addAction("android.intent.action.MEDIA_MOUNTED");
        filter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        filter.addDataScheme("file");
        context.registerReceiver(this.mUsbListener, filter);
        setFocusable(false);
        initNetworkStatusImg();
    }

    public TopRightStatusBar(Context context) {
        super(context);
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        try {
            this.mContext.unregisterReceiver(this.mUsbListener);
        } catch (Exception e) {
        }
    }

    /* access modifiers changed from: protected */
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    private void initNetworkStatusImg() {
        File[] file;
        ConnectivityManager localConnectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean bWIFIisA = localConnectivityManager.getNetworkInfo(1).isAvailable();
        boolean bWIFIisC = localConnectivityManager.getNetworkInfo(1).isConnected();
        NetworkInfo networkInfo = localConnectivityManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        boolean bETHERNETisC = networkInfo != null ? networkInfo.isConnected() : false;
        if (bWIFIisC || bETHERNETisC) {
            if (bETHERNETisC) {
                this.mNetworkStatusImageView.setImageResource(R.drawable.conn_stat_eth_connected);
            } else if (bWIFIisC) {
                this.mNetworkStatusImageView.setImageResource(R.drawable.conn_stat_wifi_signal_3);
            }
        } else if (bWIFIisA) {
            this.mNetworkStatusImageView.setImageResource(R.drawable.wifi_disconnect);
        } else {
            this.mNetworkStatusImageView.setImageResource(R.drawable.conn_stat_eth_disconnected);
        }
        this.mUsbStatusImageView.setImageResource(R.drawable.conn_stat_usb_disconnected);
        File dir = new File("//mnt//usb");
        if (dir.exists() && dir.isDirectory()) {
            for (File file2 : dir.listFiles()) {
                if (Environment.getStorageState(file2).equals("mounted")) {
                    this.mUsbStatusImageView.setImageResource(R.drawable.conn_stat_usb_connected);
                }
            }
        }
    }

    private void setNetworkStatusImg() {
        ConnectivityManager localConnectivityManager = (ConnectivityManager) this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean bWIFIConnect = localConnectivityManager.getNetworkInfo(1).isConnectedOrConnecting();
        boolean bETHERNETConnnect = localConnectivityManager.getNetworkInfo(9).isConnectedOrConnecting();
        if (bWIFIConnect || !bETHERNETConnnect) {
            this.mNetworkStatusImageView.setImageResource(R.drawable.wifi_disconnect);
        } else {
            this.mNetworkStatusImageView.setImageResource(R.drawable.conn_stat_eth_connected);
        }
    }

    @Override // com.toptech.launcher.net.NetworkMonitor.INetworkUpdateListener
    public void onUpdateNetworkConnectivity(Bundle paramBundle) {
        int isVidfiable = paramBundle.getInt("NetworkMonitor.interface", 4);
        int connectStatus = paramBundle.getInt("NetworkMonitor.Status", 4);
        Log.v(TAG, "mCurrentInterfaceId:" + isVidfiable + " statusId:" + connectStatus);
        if (isVidfiable == 0) {
            switch (connectStatus) {
                case 0:
                    this.mNetworkStatusImageView.setImageResource(R.drawable.conn_stat_eth_disconnected);
                    return;
                case 1:
                    this.mNetworkStatusImageView.setImageResource(R.drawable.conn_stat_eth_connected);
                    return;
                default:
                    return;
            }
        } else if (isVidfiable == 1) {
            switch (connectStatus) {
                case 0:
                    this.mNetworkStatusImageView.setImageResource(R.drawable.wifi_disconnect);
                    return;
                case 1:
                    this.mNetworkStatusImageView.setImageResource(ConstantResource.WIFI_SIGNAL_LEVLES[paramBundle.getInt("NetworkMonitor.wifi.level")]);
                    return;
                default:
                    return;
            }
        }
    }
}
