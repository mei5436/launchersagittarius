package com.toptech.launcher.net;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import java.util.ArrayList;
import java.util.List;

public class NetworkStateMonitor {
    private static NetworkStateMonitor mInstance;
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        /* class com.toptech.launcher.net.NetworkStateMonitor.AnonymousClass1 */

        public void onReceive(Context context, Intent intent) {
            NetworkInfo info = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
            if (info == null) {
                return;
            }
            if (info.getType() != 9 && info.getType() != 1) {
                return;
            }
            if (info.isConnected()) {
                NetworkStateMonitor.this.notifyObersers(0);
            } else if (info.isAvailable()) {
                NetworkStateMonitor.this.notifyObersers(1);
            }
        }
    };
    private Context mContext;
    private List<INetworkState> mINetworkStates = new ArrayList();

    private NetworkStateMonitor(Context paramContext) {
        this.mContext = paramContext;
        IntentFilter localIntentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        localIntentFilter.addAction("android.net.wifi.STATE_CHANGE");
        localIntentFilter.addAction("android.net.wifi.WIFI_STATE_CHANGED");
        paramContext.registerReceiver(this.mBroadcastReceiver, localIntentFilter);
    }

    public static NetworkStateMonitor getInstance(Context paramContext) {
        if (mInstance == null) {
            mInstance = new NetworkStateMonitor(paramContext);
        }
        return mInstance;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void notifyObersers(int paramInt) {
        if (paramInt == 1) {
            for (INetworkState iNetworkState : this.mINetworkStates) {
                iNetworkState.onNetworkAvaliable();
            }
        }
        if (paramInt == 0) {
            for (INetworkState iNetworkState2 : this.mINetworkStates) {
                iNetworkState2.onNetworkConnected();
            }
        }
    }

    public void registerListener(INetworkState paramINetworkState) {
        this.mINetworkStates.add(paramINetworkState);
    }
}
