package com.project.stephencao.mymobileguardian.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.project.stephencao.mymobileguardian.engine.AcquireMissionInfo;

public class LockScreenClearMissionsService extends Service {
    private IntentFilter mIntentFilter;
    private MyLockScreenReceiver myLockScreenReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {

        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        myLockScreenReceiver = new MyLockScreenReceiver();
        registerReceiver(myLockScreenReceiver, mIntentFilter);
        super.onCreate();
    }

    class MyLockScreenReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            AcquireMissionInfo.killAll(context);
        }
    }

    @Override
    public void onDestroy() {
        if (myLockScreenReceiver != null) {
            unregisterReceiver(myLockScreenReceiver);
        }
        super.onDestroy();
    }


}
