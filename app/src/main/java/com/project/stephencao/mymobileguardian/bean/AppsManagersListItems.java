package com.project.stephencao.mymobileguardian.bean;

import android.graphics.drawable.Drawable;

public class AppsManagersListItems {
    private Drawable appIcon;
    private String appName;
    private boolean isStoreInSDCard;
    private boolean isSystemApp;
    private String packageName;

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public boolean isStoreInSDCard() {
        return isStoreInSDCard;
    }

    public void setStoreInSDCard(boolean storeInSDCard) {
        isStoreInSDCard = storeInSDCard;
    }

    public boolean isSystemApp() {
        return isSystemApp;
    }

    public void setSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String toString() {
        return "AppsManagersListItems{" +
                "appIcon=" + appIcon +
                ", appName='" + appName + '\'' +
                ", isStoreInSDCard=" + isStoreInSDCard +
                ", isSystemApp=" + isSystemApp +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
