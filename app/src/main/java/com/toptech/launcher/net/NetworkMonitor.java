package com.toptech.launcher.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import java.lang.ref.WeakReference;

public class NetworkMonitor extends BroadcastReceiver {
    private static final String TAG = "NetworkMonitor";
    private boolean flag = false;
    private ConnectivityManager localConnectivityManager;
    private int mActiveInterface = 0;
    private int mActiveStatus = 0;
    private WeakReference<Context> mContextRef;
    private boolean mEthHWConnected = false;
    private INetworkUpdateListener mListener;
    private boolean mWifiConnected = false;
    private boolean mWifiEnabled = false;
    private int mWifiLevel = 3;
    private int mWifiRssi = 65336;

    public interface INetworkUpdateListener {
        void onUpdateNetworkConnectivity(Bundle bundle);
    }

    public NetworkMonitor(Context context, INetworkUpdateListener paramINetworkUpdateListener) {
        this.mContextRef = new WeakReference<>(context);
        this.mListener = paramINetworkUpdateListener;
        this.localConnectivityManager = (ConnectivityManager) context.getSystemService("connectivity");
    }

    private Bundle getCurrentConnectivityInfo() {
        Bundle localBundle = new Bundle();
        localBundle.putInt("NetworkMonitor.interface", this.mActiveInterface);
        localBundle.putInt("NetworkMonitor.Status", this.mActiveStatus);
        if (this.mActiveInterface == 1) {
            localBundle.putInt("NetworkMonitor.wifi.level", this.mWifiLevel);
        }
        Log.v(TAG, "NetworkMonitor.interface:" + this.mActiveInterface + "NetworkMonitor.Status:" + this.mActiveStatus + "NetworkMonitor.wifi.level:" + this.mWifiLevel);
        return localBundle;
    }

    private void updateActiveNetwork() {
        if (this.mWifiEnabled) {
            this.mActiveInterface = 1;
            if (this.mWifiConnected) {
                this.mActiveStatus = 1;
            } else {
                this.mActiveStatus = 0;
            }
        } else {
            this.mActiveInterface = 0;
            if (this.mEthHWConnected) {
                this.mActiveStatus = 1;
            } else {
                this.mActiveStatus = 0;
            }
        }
    }

    public void onReceive(Context context, Intent intent) {
        String str = intent.getAction();
        boolean bWIFIisA = this.localConnectivityManager.getNetworkInfo(1).isAvailable();
        boolean bWIFIisC = this.localConnectivityManager.getNetworkInfo(1).isConnected();
        boolean bETHERNETisC = this.localConnectivityManager.getNetworkInfo(9).isConnected();
        Log.d(TAG, "OnReceive by the str: " + str);
        if ("android.net.conn.CONNECTIVITY_CHANGE".equals(str)) {
            if (bWIFIisA) {
                Log.d("sch", "the network type is TYPE_WIFI");
                this.mWifiEnabled = true;
                if (!bWIFIisC) {
                    this.mEthHWConnected = bETHERNETisC;
                    this.mWifiConnected = false;
                } else {
                    this.mEthHWConnected = bETHERNETisC;
                    this.mWifiLevel = WifiManager.calculateSignalLevel(this.mWifiRssi, 4);
                    this.mWifiConnected = true;
                }
            } else {
                Log.d("sch", "the network type is TYPE_ETHERNET");
                this.mWifiEnabled = false;
                this.mWifiConnected = false;
                this.mEthHWConnected = bETHERNETisC;
            }
            updateActiveNetwork();
            if (this.mListener != null) {
                this.mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
            }
            Log.d(TAG, "CONNECTIVITY_CHANGE etchconnect wifieable wificonnect:  " + this.mEthHWConnected + " " + this.mWifiEnabled + " " + this.mWifiConnected);
        } else if ("android.net.wifi.WIFI_STATE_CHANGED".equals(str)) {
            this.mWifiEnabled = bWIFIisA;
            updateActiveNetwork();
            if (this.mListener != null) {
                this.mListener.onUpdateNetworkConnectivity(getCurrentConnectivityInfo());
            }
        } else if ("android.net.wifi.STATE_CHANGE".equals(str)) {
            if (bWIFIisC) {
                this.mWifiLevel = 0;
                this.mWifiRssi = 65336;
            }
        } else if ("android.net.wifi.RSSI_CHANGED".equals(str) && this.mWifiConnected) {
            this.mWifiRssi = intent.getIntExtra("newRssi", 65336);
            this.mWifiLevel = WifiManager.calculateSignalLevel(this.mWifiRssi, 4);
        }
    }

    public void startMonitor() {
        IntentFilter localIntentFilter = new IntentFilter();
        localIntentFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
        localIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        localIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
        localIntentFilter.addAction("android.net.wifi.RSSI_CHANGED");
        Context localContext = this.mContextRef.get();
        if (localContext != null && !this.flag) {
            localContext.registerReceiver(this, localIntentFilter);
            this.flag = true;
        }
        this.mWifiEnabled = false;
        this.mWifiConnected = false;
        this.mEthHWConnected = false;
    }

    public void stopMonitor() {
        Context localContext = this.mContextRef.get();
        if (localContext != null && this.flag) {
            this.flag = false;
            localContext.unregisterReceiver(this);
        }
    }
}
