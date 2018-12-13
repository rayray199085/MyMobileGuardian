package com.project.stephencao.mymobileguardian.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

public class ServiceUtil {
    /**
     * return the status of whtether the service is running or not
     * @param serviceName
     * @return
     */
    public static boolean isRunning(Context context, String serviceName){
        // by using activity manager to get all service
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo info : runningServices){
           if(serviceName.equals(info.service.getClassName())){
               return true;
           }
        }
        return false;
    }
}
