package com.toptech.launcher.json;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.toptech.launcher.tools.MUrlTools;

public class JsonReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent server = new Intent(context, JsonServer.class);
        server.setAction(MUrlTools.ACTION_JSON_UPDATE_ARRAYLIST);
        context.startService(server);
    }
}
