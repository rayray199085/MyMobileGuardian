package com.project.stephencao.mymobileguardian.service;

import android.app.Service;
import android.content.*;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

import com.android.internal.telephony.ITelephony;
import com.project.stephencao.mymobileguardian.utils.MyOpenHelperForBlacklist;

import java.lang.reflect.Method;


public class BlacklistService extends Service {
    private BlockSMSReceiver mBlockSMSReceiver;
    private TelephonyManager mTelephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private  MyContentObserver myContentObserver;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.provider.Telephony.SMS_RECEIVED");
        intentFilter.setPriority(Integer.MAX_VALUE);
        mBlockSMSReceiver = new BlockSMSReceiver();
        registerReceiver(mBlockSMSReceiver, intentFilter);

        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);

        super.onCreate();
    }

    class MyPhoneStateListener extends PhoneStateListener {
        //overwrite phone status change listen method
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: { // idle status
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: { // on hold or dialing
                    break;
                }
                case TelephonyManager.CALL_STATE_RINGING: {
                    endCall(phoneNumber);
                    break;
                }
            }
        }
    }

    private void endCall(String phoneNumber) {
        MyOpenHelperForBlacklist myOpenHelperForBlacklist = new MyOpenHelperForBlacklist(getApplicationContext());
        SQLiteDatabase sqLiteDatabase = myOpenHelperForBlacklist.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query("info", new String[]{"type"}, "phone=?",
                new String[]{phoneNumber}, null, null, null);
        System.out.println(cursor.getCount());
        while (cursor.moveToNext()) {
            String type = cursor.getString(0);
            if ("Call".equals(type)|| "Both".equals(type)) {
                // block the call
                try {
                    Class<?> aClass = Class.forName("android.os.ServiceManager");
                    Method method = aClass.getMethod("getService", String.class);
                    IBinder iBinder = (IBinder) method.invoke(null, Context.TELEPHONY_SERVICE);
                    ITelephony iTelephony = ITelephony.Stub.asInterface(iBinder);
                    iTelephony.endCall();
                } catch (Exception e) {
                    System.out.println("cannot block the call");
                }
                // delete the record
                myContentObserver = new MyContentObserver(new Handler(),phoneNumber);
                getContentResolver().registerContentObserver(Uri.parse("content://call_log/calls"),
                        true,myContentObserver);
            }
        }
    }
    class MyContentObserver extends ContentObserver {
        private String phoneNumber;

        /**
         * Creates a content observer.
         *
         * @param handler The handler to run {@link #onChange} on, or null if none.
         */
        public MyContentObserver(Handler handler,String phoneNumber) {
            super(handler);
            this.phoneNumber = phoneNumber;
        }

        @Override
        public void onChange(boolean selfChange) {
            getContentResolver().delete(Uri.parse("content://call_log/calls"),"number=?",new String[]{phoneNumber});
            super.onChange(selfChange);
        }
    }

    class BlockSMSReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            MyOpenHelperForBlacklist myOpenHelperForBlacklist = new MyOpenHelperForBlacklist(context);
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String address = smsMessage.getOriginatingAddress();
                SQLiteDatabase sqLiteDatabase = myOpenHelperForBlacklist.getWritableDatabase();
                Cursor cursor = sqLiteDatabase.query("info", new String[]{"type"}, "phone=?",
                        new String[]{address}, null, null, null);
                while (cursor.moveToNext()) {
                    String type = cursor.getString(0);
                    if ("SMS".equals(type) || "Both".equals(type)) {
                        abortBroadcast();
                    }
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        if (mBlockSMSReceiver != null) {
            unregisterReceiver(mBlockSMSReceiver);
        }
        if (mTelephonyManager != null && myPhoneStateListener != null) {
            mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE); // cancel listener
        }
        // cancel content observer
        if(myContentObserver!=null){
            getContentResolver().unregisterContentObserver(myContentObserver);
        }
        super.onDestroy();
    }
}
