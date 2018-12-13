package com.project.stephencao.mymobileguardian.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MyOpenHelperForBlacklist extends SQLiteOpenHelper {
    public MyOpenHelperForBlacklist(Context context){
        super(context,"blacklist.db",null,1);
    }

    public MyOpenHelperForBlacklist(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table info(_id integer primary key autoincrement,phone verchar(20),type verchar(20))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
