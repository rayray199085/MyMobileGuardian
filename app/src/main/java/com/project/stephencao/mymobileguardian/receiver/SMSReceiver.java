package com.project.stephencao.mymobileguardian.receiver;

import android.annotation.SuppressLint;
import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.service.LocationService;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;

import java.io.File;
import java.io.IOException;

import static android.content.Context.DEVICE_POLICY_SERVICE;

public class SMSReceiver extends BroadcastReceiver {
    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDPM;

    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) {
        mDeviceAdminSample = new ComponentName(context, MyDeviceAdminReceiver.class);// context, receiver.class
        mDPM = (DevicePolicyManager) context.getSystemService(DEVICE_POLICY_SERVICE);
        boolean isProtecting = SharePreferencesUtil.getBoolean(context, ConstantValues.SECURITY_SETTINGS_FINISHED);
        if (isProtecting) {
            Object[] objects = (Object[]) intent.getExtras().get("pdus");
            for (Object obj : objects) {
                SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) obj);
                String address = smsMessage.getOriginatingAddress();
                String messageBody = smsMessage.getMessageBody();
                // play alarm sound
                if (messageBody.contains("#*alarm*#")) {
                    MediaPlayer mediaPlayer = MediaPlayer.create(context,R.raw.alarm);
                    mediaPlayer.setLooping(true);
                    mediaPlayer.start();
                } else if (messageBody.contains("#*location*#")) {
                    Intent locationIntent = new Intent(context, LocationService.class);
                    context.startService(locationIntent);
                } else if (messageBody.contains("#*erasedata*#")) {
                    if (mDPM.isAdminActive(mDeviceAdminSample)) {
                        mDPM.wipeData(0); // wipe internal data
                        mDPM.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
                    } else {
                        ToastUtil.show(context, "Activate admin first.");
                    }
                } else if (messageBody.contains("#*lockscreen*#")) {
                    if (mDPM.isAdminActive(mDeviceAdminSample)) {
                        mDPM.lockNow();
                        mDPM.resetPassword(ConstantValues.SECURITY_PASSWORD, 0); // the same password as the security settings
                    } else {
                        ToastUtil.show(context, "Activate admin first.");
                    }
                }
            }
        }
    }
}
