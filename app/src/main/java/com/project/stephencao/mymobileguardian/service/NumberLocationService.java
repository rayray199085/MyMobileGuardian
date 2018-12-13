package com.project.stephencao.mymobileguardian.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.*;
import android.widget.TextView;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.engine.AddressDao;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

public class NumberLocationService extends Service {
    private TelephonyManager mTelephonyManager;
    private MyPhoneStateListener myPhoneStateListener;
    private View mToastView;
    private OutgoingCallReceiver mOutgoingCallReceiver;
    private Point outSize;
    private WindowManager mWindowManager;
    private final WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
    private TextView mTextView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String address = (String) msg.obj;
            mTextView.setText(address);
        }
    };

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mTelephonyManager = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
        myPhoneStateListener = new MyPhoneStateListener();
        mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        WindowManager windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        outSize = new Point();
        windowManager.getDefaultDisplay().getSize(outSize);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_NEW_OUTGOING_CALL); //listen outgoing call and set number location notification

        mOutgoingCallReceiver = new OutgoingCallReceiver();
        registerReceiver(mOutgoingCallReceiver,intentFilter);
        super.onCreate();
    }
    class OutgoingCallReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String phoneNumber = getResultData(); // get the call out number
            showToast(phoneNumber);
        }
    }

    class MyPhoneStateListener extends PhoneStateListener {
        //overwrite phone status change listen method
        @Override
        public void onCallStateChanged(int state, String phoneNumber) {
            switch (state) {
                case TelephonyManager.CALL_STATE_IDLE: { // idle status
                    if (mToastView != null && mWindowManager != null) {
                        mWindowManager.removeView(mToastView);
                    }
                    break;
                }
                case TelephonyManager.CALL_STATE_OFFHOOK: { // on hold or dialing
                    break;
                }
                case TelephonyManager.CALL_STATE_RINGING: {
                    showToast(phoneNumber);
                    break;
                }
            }
        }
    }

    public void showToast(String phoneNumber) {
        final WindowManager.LayoutParams params = mParams;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.format = PixelFormat.TRANSLUCENT;
        params.gravity = Gravity.LEFT + Gravity.TOP; // at top left corner
        params.type = WindowManager.LayoutParams.TYPE_PHONE; // show the toast when phone call is coming
        params.setTitle("Toast");
        params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        mToastView = View.inflate(this, R.layout.view_toast, null);
        mTextView = mToastView.findViewById(R.id.tv_toast);
        //params.x means the position on top left corner of the content
        params.x = SharePreferencesUtil.getInteger(this, ConstantValues.TOAST_POSITION_X);
        params.y = SharePreferencesUtil.getInteger(this, ConstantValues.TOAST_POSITION_Y);
        mToastView.setOnTouchListener(new View.OnTouchListener() {
            int startX = 0;
            int startY = 0;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {

                    case MotionEvent.ACTION_UP: {
                        SharePreferencesUtil.recordInteger(getApplicationContext(),
                                ConstantValues.TOAST_POSITION_X, params.x);
                        SharePreferencesUtil.recordInteger(getApplicationContext(),
                                ConstantValues.TOAST_POSITION_Y, params.y);
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int moveX = (int) event.getRawX();
                        int moveY = (int) event.getRawY();
                        int distanceX = moveX - startX;
                        int distanceY = moveY - startY;

                        params.x = params.x + distanceX;
                        params.y = params.y + distanceY;

                        if(params.x<0){
                            params.x = 0;
                        }
                        if(params.y<0){
                            params.y =0;
                        }
                        if (params.x>outSize.x - mToastView.getWidth()){
                            params.x = outSize.x - mToastView.getWidth();
                        }
                        if(params.y > outSize.y - mToastView.getHeight() -22){
                            params.y = outSize.y - mToastView.getHeight() -22;
                        }
                        mWindowManager.updateViewLayout(mToastView,params);

                        startX = moveX;
                        startY = moveY;
                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        startX = (int) event.getRawX();
                        startY = (int) event.getRawY();
                        break;
                    }
                }
                return true;// false for onClick and onTouch exist together
            }
        });

        int toastStyles = SharePreferencesUtil.getInteger(this, ConstantValues.TOAST_STYLES);
        if (toastStyles == 0) {
            mTextView.setBackgroundResource(R.drawable.toast_bg_default);
        } else {
            mTextView.setBackgroundResource(toastStyles);
        }
        mWindowManager.addView(mToastView, params);
        queryPhoneNumberLocation(phoneNumber);
    }

    private void queryPhoneNumberLocation(final String phoneNumber) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String address = AddressDao.getAddress(phoneNumber);
                Message message = Message.obtain();
                message.obj = address;
                handler.sendMessage(message);
            }
        }).start();

    }

    @Override
    public void onDestroy() {
        if (mTelephonyManager != null && myPhoneStateListener != null) {
            mTelephonyManager.listen(myPhoneStateListener, PhoneStateListener.LISTEN_NONE); // cancel listener
        }
        if(mOutgoingCallReceiver!=null){
            unregisterReceiver(mOutgoingCallReceiver);
        }
        super.onDestroy();
    }
}
