package com.project.stephencao.mymobileguardian.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import com.project.stephencao.mymobileguardian.activity.SplashActivity;

public class ToastUtil {
    /**
     *
     * @param context
     * @param content to be shown in the Toast
     */
    public static void show(Context context, String content) {
      Toast.makeText(context,content,Toast.LENGTH_SHORT).show();
    }

}
