package com.project.stephencao.mymobileguardian.service;

import android.app.PendingIntent;
import android.app.Service;
import android.appwidget.AppWidgetManager;
import android.content.*;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.text.format.Formatter;
import android.widget.RemoteViews;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.engine.AcquireMissionInfo;
import com.project.stephencao.mymobileguardian.receiver.MyAppWidgetProvider;

import java.util.Timer;
import java.util.TimerTask;

public class AppWidgetService extends Service {
    private Timer mTimer;
    private MyScreenStatusReceiver myScreenStatusReceiver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        startTimer();
        registerScreenStatus();
        super.onCreate();
    }

    private void registerScreenStatus() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_SCREEN_ON);
        intentFilter.addAction(Intent.ACTION_SCREEN_OFF);
        myScreenStatusReceiver = new MyScreenStatusReceiver();
        registerReceiver(myScreenStatusReceiver, intentFilter);
    }

    class MyScreenStatusReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
                if (mTimer == null) {
                    startTimer();
                }
            } else {
                cancelTimer();
            }
        }
    }

    private void cancelTimer() {
        if (mTimer != null) {
            mTimer.cancel();
        }
        mTimer = null;
    }

    private void startTimer() {
        mTimer = new Timer();
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                updateAppWidget();
            }
        };
        mTimer.schedule(timerTask, 0, 5000);
    }

    private void updateAppWidget() {
        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        RemoteViews remoteViews = new RemoteViews(getPackageName(), R.layout.example_appwidget_info);


        int totalNumberOfMissions = AcquireMissionInfo.getTotalNumberOfMissions(this);
        long availableRAMSpace = AcquireMissionInfo.getAvailableRAMSpace(this);

        remoteViews.setTextViewText(R.id.tv_app_widget_process_count, "Running Applications：" + String.valueOf(totalNumberOfMissions));
        String availableSpace = Formatter.formatFileSize(this, availableRAMSpace);
        remoteViews.setTextViewText(R.id.tv_app_widget_available_ram, "Available RAM：" + availableSpace);
        ComponentName componentName = new ComponentName(this, MyAppWidgetProvider.class);

        // set on click event
        Intent intent = new Intent("android.intent.action.HOME_ENTRANCE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        // click the view and enter home page
        remoteViews.setOnClickPendingIntent(R.id.ll_app_widget_root, pendingIntent);

        // click the button to send a broadcast
        Intent broadcastIntent = new Intent("android.intent.action.KILL_ALL_BACKGROUND_MISSIONS");
        PendingIntent broadcastPendingIntent = PendingIntent.getBroadcast(this, 0, broadcastIntent, PendingIntent.FLAG_CANCEL_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.btn_app_widget_clear_all, broadcastPendingIntent);

        widgetManager.updateAppWidget(componentName, remoteViews);
    }

    @Override
    public void onDestroy() {
        if (myScreenStatusReceiver != null) {
            unregisterReceiver(myScreenStatusReceiver);
        }
        cancelTimer();
        super.onDestroy();
    }
}
