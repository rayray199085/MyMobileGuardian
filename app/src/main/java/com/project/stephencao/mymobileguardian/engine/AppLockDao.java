package com.project.stephencao.mymobileguardian.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.project.stephencao.mymobileguardian.utils.MyOpenHelperForAppLock;

import java.util.ArrayList;
import java.util.List;


public class AppLockDao {
    public static void insertItemIntoDB(Context context, String packageName) {
        MyOpenHelperForAppLock myOpenHelperForAppLock = new MyOpenHelperForAppLock(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForAppLock.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("packagename", packageName);
        sqLiteDatabase.insert("privacy", null, contentValues);
        sqLiteDatabase.close();
        context.getContentResolver().notifyChange(Uri.parse("content://applock/update"),null);
    }

    public static void deleteItemFromDB(Context context, String packageName) {
        MyOpenHelperForAppLock myOpenHelperForAppLock = new MyOpenHelperForAppLock(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForAppLock.getWritableDatabase();
        sqLiteDatabase.delete("privacy", "packagename=?", new String[]{packageName});
        sqLiteDatabase.close();
        context.getContentResolver().notifyChange(Uri.parse("content://applock/update"),null);
    }

    public static List<String> queryAll(Context context) {
        MyOpenHelperForAppLock myOpenHelperForAppLock = new MyOpenHelperForAppLock(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForAppLock.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("privacy", new String[]{"packagename"}, null,
               null, null, null, null);
        List<String> lockPackageNameList = new ArrayList<>();
        while (cursor.moveToNext()) {
            String packageName = cursor.getString(0);
            lockPackageNameList.add(packageName);
        }
        cursor.close();
        sqLiteDatabase.close();
        return lockPackageNameList;
    }
}
