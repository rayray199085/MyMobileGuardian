package com.project.stephencao.mymobileguardian.receiver;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.util.Log;
import com.project.stephencao.mymobileguardian.service.NumberLocationService;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;

/**
 * BootReceiver get the sim card serial number after the phone reboot
 * Send a message to the contact person if the serial numbers are different
 */
public class BootReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if("android.intent.action.BOOT_COMPLETED".equals(action)){
           TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            @SuppressLint("MissingPermission") String simSerialNumber = telephonyManager.getSimSerialNumber();

            if(simSerialNumber!=null && !"".equals(simSerialNumber)){
                String originalSIMSerialNumber = SharePreferencesUtil.getString(context, ConstantValues.SECURITY_SIM_CARD_SERIAL_NUMBER);
                if(originalSIMSerialNumber!=null && !originalSIMSerialNumber.equals(simSerialNumber)){
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(SharePreferencesUtil.getString(context,ConstantValues.SECURITY_CONTACT_NUMBER),
                            null,"Warning!!! SIM card has been replaced.",null,null);
                }
            }
            boolean numberLocationFlag = SharePreferencesUtil.getBoolean(context,ConstantValues.NUMBER_REGISTRATION_LOCATION);
            if(numberLocationFlag){
                context.startService(new Intent(context, NumberLocationService.class));
            }

        }
    }
}
