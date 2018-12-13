package com.project.stephencao.mymobileguardian.bean;

public class ScanFilesInformation {
    private boolean isVirus;
    private String packageName;
    private String name;

    public boolean isVirus() {
        return isVirus;
    }

    public void setVirus(boolean virus) {
        isVirus = virus;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
