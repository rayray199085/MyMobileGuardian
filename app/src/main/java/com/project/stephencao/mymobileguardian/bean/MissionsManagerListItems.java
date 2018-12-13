package com.project.stephencao.mymobileguardian.bean;

import android.graphics.drawable.Drawable;

public class MissionsManagerListItems {
    private Drawable missionIcon;
    private String missionName;
    private long ramSpaceOccupation;
    private boolean isChosen;
    private String packageName;

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public long getRamSpaceOccupation() {
        return ramSpaceOccupation;
    }

    public void setRamSpaceOccupation(long ramSpaceOccupation) {
        this.ramSpaceOccupation = ramSpaceOccupation;
    }

    private boolean isSystemMission;

    public boolean isSystemMission() {
        return isSystemMission;
    }

    public void setSystemMission(boolean systemMission) {
        isSystemMission = systemMission;
    }

    public boolean isChosen() {
        return isChosen;
    }

    public void setChosen(boolean chosen) {
        isChosen = chosen;
    }

    public Drawable getMissionIcon() {
        return missionIcon;
    }

    public void setMissionIcon(Drawable missionIcon) {
        this.missionIcon = missionIcon;
    }

    public String getMissionName() {
        return missionName;
    }

    public void setMissionName(String missionName) {
        this.missionName = missionName;
    }


    @Override
    public String toString() {
        return "MissionsManagerListItems{" +
                "missionIcon=" + missionIcon +
                ", missionName='" + missionName + '\'' +
                ", ramSpaceOccupation='" + ramSpaceOccupation + '\'' +
                '}';
    }
}
