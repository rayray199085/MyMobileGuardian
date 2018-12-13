package com.project.stephencao.mymobileguardian.service;

import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import com.project.stephencao.mymobileguardian.activity.BlockedPageActivity;
import com.project.stephencao.mymobileguardian.engine.AppLockDao;

import java.util.List;

public class WatchDogService extends Service {
    private boolean isWatch = true;
    private MyWatchDogPassAppReceiver myWatchDogPassAppReceiver;
    private String passPackageName;
    private MyContentObserver myContentObserver;
    private List<String> mStringList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        watch();
        IntentFilter intentFilter = new IntentFilter("android.intent.action.PASS_WATCHING_THIS_APP");

        myWatchDogPassAppReceiver = new MyWatchDogPassAppReceiver();
        registerReceiver(myWatchDogPassAppReceiver,intentFilter);

        myContentObserver = new MyContentObserver(new Handler());
        getContentResolver().registerContentObserver(Uri.parse("content://applock/update"),true, myContentObserver);
        super.onCreate();
    }
    class MyContentObserver extends ContentObserver{

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler) {
            super(handler);
        }

        @Override
        public void onChange(boolean selfChange) {
           new Thread(new Runnable() {
               @Override
               public void run() {
                   mStringList = AppLockDao.queryAll(WatchDogService.this);
               }
           }).start();
            super.onChange(selfChange);
        }
    }

    class MyWatchDogPassAppReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            passPackageName = intent.getStringExtra("packageName");
        }
    }

    private void watch() {

        new Thread(new Runnable() {
            @Override
            public void run() {
                mStringList = AppLockDao.queryAll(WatchDogService.this);
                while (isWatch) {
                    ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
                    List<ActivityManager.RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
                    ActivityManager.RunningTaskInfo runningTaskInfo = runningTasks.get(0); // the newest opened process
                    String packageName = runningTaskInfo.topActivity.getPackageName();
                    if (mStringList.contains(packageName)) {
                        if(!packageName.equals(passPackageName)){
                            Intent intent = new Intent(WatchDogService.this, BlockedPageActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra("packageName",packageName);
                            startActivity(intent);
                        }
                    }
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    @Override
    public void onDestroy() {
        if(myWatchDogPassAppReceiver!=null){
            unregisterReceiver(myWatchDogPassAppReceiver);
        }
        isWatch = false;
        if(myContentObserver!=null){
           getContentResolver().unregisterContentObserver(myContentObserver);
        }
        super.onDestroy();
    }
}
