package com.project.stephencao.mymobileguardian.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class AntiVirusDao {
    public static String dbPath = "/data/data/com.project.stephencao.mymobileguardian/files/antivirus.db";

    public static List<String> queryAll(){
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath,null,SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.query("datable", new String[]{"md5"},
                null, null, null, null, null);
        List<String> virusMD5List = new ArrayList<>();
        while (cursor.moveToNext()){
            String virusMD5 = cursor.getString(0);
            virusMD5List.add(virusMD5);
        }
        cursor.close();
        sqLiteDatabase.close();
        return virusMD5List;
    }
}
