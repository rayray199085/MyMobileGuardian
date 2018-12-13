package com.project.stephencao.mymobileguardian.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.project.stephencao.mymobileguardian.engine.AcquireMissionInfo;

public class MyAppWidgetClearMissionsReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        AcquireMissionInfo.killAll(context);
    }
}
