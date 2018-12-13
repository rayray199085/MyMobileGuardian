package com.project.stephencao.mymobileguardian.engine;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AddressDao {
    public static String dbPath = "/data/data/com.project.stephencao.mymobileguardian/files/address.db";

    /**
     * @param phoneNumber
     * @return phone number location
     */
    public static String getAddress(String phoneNumber) {
        String mobileRegex = "^1[358]\\d{5,9}";
        String fixedRegex = "^0\\d{2,3}\\d{7,8}";
        SQLiteDatabase sqLiteDatabase = SQLiteDatabase.openDatabase(
                dbPath, null, SQLiteDatabase.OPEN_READONLY);
        if (phoneNumber.matches(mobileRegex)) {
            Cursor cursor = sqLiteDatabase.query("info", new String[]{"area,city,cardtype"},
                    "mobileprefix=?", new String[]{phoneNumber.substring(0, 7)},
                    null, null, null);
            String result = "";
            while (cursor.moveToNext()) {
                String areaCode = cursor.getString(0);
                String cityName = cursor.getString(1);
                String cardType = cursor.getString(2);
                result = "City: " + cityName + "\nCard Type: " + cardType;
            }
            sqLiteDatabase.close();
            return result;
        }
        else if(phoneNumber.matches(fixedRegex)){
            Cursor cursor = sqLiteDatabase.query("info", new String[]{"city"},
                    "area=?", new String[]{phoneNumber.substring(0, 4)},
                    null, null, null);
            String result = "";
            while (cursor.moveToNext()) {
                String cityName = cursor.getString(0);
                result = "City: " + cityName + "\nFixed telephone number";
            }
            if("".equals(result)){
                Cursor cursor1 = sqLiteDatabase.query("info", new String[]{"city"},
                        "area=?", new String[]{phoneNumber.substring(0, 3)},
                        null, null, null);
                while (cursor1.moveToNext()) {
                    String cityName = cursor1.getString(0);
                    result = "City: " + cityName + "\nFixed telephone number";
                }
            }
            sqLiteDatabase.close();
            return result;
        }
        else{
            return "Unknown Location";
        }



    }
}
