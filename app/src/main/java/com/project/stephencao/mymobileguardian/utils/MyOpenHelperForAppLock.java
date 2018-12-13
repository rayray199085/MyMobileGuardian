package com.project.stephencao.mymobileguardian.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelperForAppLock extends SQLiteOpenHelper {
    public MyOpenHelperForAppLock(Context context){
        super(context,"applock.db",null,1);
    }

    public MyOpenHelperForAppLock(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table privacy(_id integer primary key autoincrement,packagename verchar(50))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
