package com.project.stephencao.mymobileguardian.engine;

import android.app.ProgressDialog;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import com.project.stephencao.mymobileguardian.activity.ToolsActivity;
import org.xmlpull.v1.XmlSerializer;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SMSBackup {
    public static void backup(Context context, File smsBackup, ToolsActivity.CallBack callBack) {
        File file = smsBackup;
        Cursor cursor = context.getContentResolver().query(Uri.parse("content://sms/"), new String[]{"address,date,type,body"},
                null, null, null);
        if (callBack != null) {
            callBack.setMax(cursor.getCount());
        }
        int counter = 1;
        try {
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file));
            XmlSerializer xmlSerializer = Xml.newSerializer();
            xmlSerializer.setOutput(bufferedOutputStream, "utf-8");
            xmlSerializer.startDocument("utf-8", true);
            xmlSerializer.startTag(null, "smss");
            while (cursor.moveToNext()) {
                xmlSerializer.startTag(null, "sms");
                xmlSerializer.startTag(null, "address");
                xmlSerializer.text(cursor.getString(0));
                xmlSerializer.endTag(null, "address");

                xmlSerializer.startTag(null, "date");
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date date = new Date();
                date.setTime(Long.valueOf(cursor.getString(1)));
                String formatDate = simpleDateFormat.format(date);
                xmlSerializer.text(formatDate);
                xmlSerializer.endTag(null, "date");

                xmlSerializer.startTag(null, "type");
                xmlSerializer.text(cursor.getString(2));
                xmlSerializer.endTag(null, "type");

                xmlSerializer.startTag(null, "body");
                xmlSerializer.text(cursor.getString(3));
                xmlSerializer.endTag(null, "body");
                xmlSerializer.endTag(null, "sms");
                if (callBack != null) {
                    callBack.setProgress(counter);
                }
                Thread.sleep(200);
                counter++;
            }
            xmlSerializer.endTag(null, "smss");
            xmlSerializer.endDocument();
            bufferedOutputStream.close();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
