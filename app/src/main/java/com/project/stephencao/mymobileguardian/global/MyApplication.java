package com.project.stephencao.mymobileguardian.global;

import android.app.Application;
import android.os.Environment;

import java.io.*;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                e.printStackTrace();
                File file = new File(Environment.getExternalStorageDirectory(), "myError.log");
                PrintWriter printWriter = null;
                try {
                    printWriter = new PrintWriter(file);
                    e.printStackTrace(printWriter);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                finally {
                    if(printWriter!=null){
                        printWriter.close();
                    }
                    System.exit(0);
                }
            }
        });
    }
}
