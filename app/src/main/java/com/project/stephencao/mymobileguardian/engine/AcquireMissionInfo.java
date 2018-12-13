package com.project.stephencao.mymobileguardian.engine;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Debug;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.bean.MissionsManagerListItems;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AcquireMissionInfo {
    // get the total number of missions
    public static int getTotalNumberOfMissions(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        return runningAppProcesses.size();
    }

    // get available RAM space
    public static long getAvailableRAMSpace(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        activityManager.getMemoryInfo(memoryInfo);
        return memoryInfo.availMem;
    }

    public static long getTotalRAMSpace(Context context) {
//        this method is for api 16 and higher
//        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
//        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
//        activityManager.getMemoryInfo(memoryInfo);
//        return memoryInfo.totalMem;
        String totalMem = "";
        BufferedReader bufferedReader = null;
        try {

            bufferedReader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(new File("proc/meminfo"))));

            totalMem = bufferedReader.readLine();
            totalMem = totalMem.replaceAll("\\D", "");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bufferedReader != null) {
                    bufferedReader.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return Long.valueOf(totalMem) * 1024;
        }
    }

    public static List<MissionsManagerListItems> getMissionInfo(Context context) {
        List<MissionsManagerListItems> missionsManagerListItemsList = new ArrayList<>();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager packageManager = context.getPackageManager();
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcesses) {
            MissionsManagerListItems missionsManagerListItems = new MissionsManagerListItems();
            // get process name
            missionsManagerListItems.setPackageName(appProcessInfo.processName);
            Debug.MemoryInfo[] processMemoryInfo = activityManager.getProcessMemoryInfo(new int[]{appProcessInfo.pid});
            Debug.MemoryInfo memoryInfo = processMemoryInfo[0];
            // unit kb, get the size of space
            long totalPrivateDirty = (long) memoryInfo.getTotalPrivateDirty() * 1024;
            missionsManagerListItems.setRamSpaceOccupation(totalPrivateDirty);
            try {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(appProcessInfo.processName, 0);
                // mission name
                missionsManagerListItems.setMissionName(applicationInfo.loadLabel(packageManager).toString());
                // mission icon
                missionsManagerListItems.setMissionIcon(applicationInfo.loadIcon(packageManager));

                if ((applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                    missionsManagerListItems.setSystemMission(true);
                } else {
                    missionsManagerListItems.setSystemMission(false);
                }
            } catch (PackageManager.NameNotFoundException e) {
                missionsManagerListItems.setMissionName(appProcessInfo.processName);
                missionsManagerListItems.setMissionIcon(context.getResources().getDrawable(R.drawable.star));
                missionsManagerListItems.setSystemMission(true);
                e.printStackTrace();
            }
            missionsManagerListItemsList.add(missionsManagerListItems);
        }
        return missionsManagerListItemsList;
    }

    public static void killProcess(Context context, MissionsManagerListItems item) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.killBackgroundProcesses(item.getPackageName());
    }

    public static void killAll(Context context) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
            if (!context.getPackageName().equals(runningAppProcessInfo.processName)) {
                activityManager.killBackgroundProcesses(runningAppProcessInfo.processName);
            }
        }
    }
}
