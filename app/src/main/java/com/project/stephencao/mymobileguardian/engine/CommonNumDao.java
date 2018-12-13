package com.project.stephencao.mymobileguardian.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.project.stephencao.mymobileguardian.bean.CommonNumChildClassListItems;
import com.project.stephencao.mymobileguardian.bean.CommonNumClassListItems;

import java.util.ArrayList;
import java.util.List;

public class CommonNumDao {
    public static String dbPath = "/data/data/com.project.stephencao.mymobileguardian/files/commonnum.db";

    public static List<CommonNumClassListItems> getGroupData() {
        List<CommonNumClassListItems> commonNumClassListItemsList = new ArrayList<>();
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.query("classlist", new String[]{"name", "idx"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            CommonNumClassListItems commonNumClassListItems = new CommonNumClassListItems();
            commonNumClassListItems.setName(cursor.getString(0));
            commonNumClassListItems.setId(Integer.parseInt(cursor.getString(1)));
            commonNumClassListItems.setCommonNumChildClassListItemsList(getChildClassData(commonNumClassListItems.getId()));
            commonNumClassListItemsList.add(commonNumClassListItems);
        }
        cursor.close();
        sqLiteDatabase.close();
        return commonNumClassListItemsList;
    }

    private static List<CommonNumChildClassListItems> getChildClassData(int id) {
        List<CommonNumChildClassListItems> commonNumChildClassListItemsList = new ArrayList<>();
        String tableName = "table" + id;
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(dbPath, null, SQLiteDatabase.OPEN_READONLY);
        Cursor cursor = sqLiteDatabase.query(tableName, new String[]{"_id", "number", "name"},
                null, null, null, null, null);
        while (cursor.moveToNext()) {
            CommonNumChildClassListItems commonNumChildClassListItems = new CommonNumChildClassListItems();
            commonNumChildClassListItems.setId(Integer.parseInt(cursor.getString(0)));
            commonNumChildClassListItems.setNumber(cursor.getString(1));
            commonNumChildClassListItems.setName(cursor.getString(2));
            commonNumChildClassListItemsList.add(commonNumChildClassListItems);
        }
        cursor.close();
        sqLiteDatabase.close();
        return commonNumChildClassListItemsList;
    }

}
