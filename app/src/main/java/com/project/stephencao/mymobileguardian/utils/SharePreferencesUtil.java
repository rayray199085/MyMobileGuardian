package com.project.stephencao.mymobileguardian.utils;

import android.content.Context;
import android.content.SharedPreferences;

import static android.content.Context.MODE_PRIVATE;

/**
 * Record settings information in a sharePreference file
 */
public class SharePreferencesUtil {
    private static SharedPreferences mSharedPreferences;
    private static SharedPreferences.Editor editor;

    public static void initSPfile(Context context) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
    }

    public static void recordBoolean(Context context, String key, Boolean flag) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putBoolean(key, flag);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        return mSharedPreferences.getBoolean(key, false);
    }

    public static void recordString(Context context, String key, String value) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        if (!"".equals(value)) {
            editor = mSharedPreferences.edit();
            editor.putString(key, value);
            editor.commit();
        }
    }

    public static String getString(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        return mSharedPreferences.getString(key, null);
    }

    public static void remove(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.remove(key);
        editor.commit();
    }

    public static void recordInteger(Context context, String key, int id) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        editor = mSharedPreferences.edit();
        editor.putInt(key, id);
        editor.commit();
    }

    public static int getInteger(Context context, String key) {
        mSharedPreferences = context.getSharedPreferences("SettingsInfomation", MODE_PRIVATE);
        return mSharedPreferences.getInt(key, 0);
    }
}
