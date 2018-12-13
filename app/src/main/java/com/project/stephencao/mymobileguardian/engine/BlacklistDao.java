package com.project.stephencao.mymobileguardian.engine;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.project.stephencao.mymobileguardian.utils.MyOpenHelperForBlacklist;

public class BlacklistDao {

    public static void insertItemToDB(Context context,String phoneNumber, String type){
        ContentValues contentValues = new ContentValues();
        contentValues.put("phone",phoneNumber);
        contentValues.put("type",type);
        MyOpenHelperForBlacklist myOpenHelperForBlacklist = new MyOpenHelperForBlacklist(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForBlacklist.getWritableDatabase();
        sqLiteDatabase.insert("info",null,contentValues);
        sqLiteDatabase.close();
    }

    public static void deleteItemFromDB(Context context, String phoneNumber, String type){
        MyOpenHelperForBlacklist myOpenHelperForBlacklist = new MyOpenHelperForBlacklist(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForBlacklist.getWritableDatabase();
        sqLiteDatabase.delete("info","phone=? and type=?",new String[]{phoneNumber,type});
        sqLiteDatabase.close();
    }
    public static int getDataItemsCount(Context context){
        MyOpenHelperForBlacklist myOpenHelperForBlacklist = new MyOpenHelperForBlacklist(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForBlacklist.getWritableDatabase();
        int count = 0;
        Cursor cursor = sqLiteDatabase.rawQuery("select count(*) from info", null);
        if(cursor.moveToNext()){
            count = cursor.getInt(0);
        }
        sqLiteDatabase.close();
        return count;
    }

    public static Cursor queryPartialItemsFromDB(Context context, int index){
        MyOpenHelperForBlacklist myOpenHelperForBlacklist = new MyOpenHelperForBlacklist(context);
        SQLiteDatabase sqLiteDatabase = myOpenHelperForBlacklist.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.rawQuery("select phone,type from info order by _id desc limit ?,15", new String[]{String.valueOf(index)});
        return cursor;
    }
}
