package com.project.stephencao.mymobileguardian.engine;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import com.project.stephencao.mymobileguardian.bean.AppsManagersListItems;

import java.util.ArrayList;
import java.util.List;

public class AcquireAppsInfo {
    /**
     * get package information
     * @param context
     * @return
     */
    public static List<AppsManagersListItems> getAppsInfoList(Context context) {
        PackageManager packageManager = context.getPackageManager();
        List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
        List<AppsManagersListItems> appsManagersListItemsList = new ArrayList<>();
        AppsManagersListItems appsManagersListItems;
        for (PackageInfo packageInfo : installedPackages) {
            appsManagersListItems = new AppsManagersListItems();

            appsManagersListItems.setPackageName(packageInfo.packageName);
            appsManagersListItems.setAppName(packageInfo.applicationInfo.loadLabel(packageManager).toString());
            appsManagersListItems.setAppIcon(packageInfo.applicationInfo.loadIcon(packageManager));
            //finite-state machine
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
                appsManagersListItems.setSystemApp(true);
            } else {
                appsManagersListItems.setSystemApp(false);
            }
            if ((packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_EXTERNAL_STORAGE) == ApplicationInfo.FLAG_EXTERNAL_STORAGE) {
                appsManagersListItems.setStoreInSDCard(true);
            } else {
                appsManagersListItems.setStoreInSDCard(false);
            }
            appsManagersListItemsList.add(appsManagersListItems);
        }
        return appsManagersListItemsList;
    }
}
