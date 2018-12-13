package com.project.stephencao.mymobileguardian.bean;

import android.graphics.drawable.Drawable;

public class CachedAppInfoListItems {
    private Drawable icon;
    private String packageName;
    private String appName;
    private String cachedDataSize;

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getCachedDataSize() {
        return cachedDataSize;
    }

    public void setCachedDataSize(String cachedDataSize) {
        this.cachedDataSize = cachedDataSize;
    }
}
