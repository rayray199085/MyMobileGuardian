package com.project.stephencao.mymobileguardian.activity;

import android.app.AlertDialog;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import com.project.stephencao.mymobileguardian.R;
import com.project.stephencao.mymobileguardian.receiver.MyDeviceAdminReceiver;
import com.project.stephencao.mymobileguardian.service.BlacklistService;
import com.project.stephencao.mymobileguardian.service.NumberLocationService;
import com.project.stephencao.mymobileguardian.service.WatchDogService;
import com.project.stephencao.mymobileguardian.utils.ConstantValues;
import com.project.stephencao.mymobileguardian.utils.ServiceUtil;
import com.project.stephencao.mymobileguardian.utils.SharePreferencesUtil;
import com.project.stephencao.mymobileguardian.utils.ToastUtil;
import com.project.stephencao.mymobileguardian.view.MySettingStylesView;
import com.project.stephencao.mymobileguardian.view.MySettingsItemsView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener {

    private MySettingsItemsView mAutoUpgradeView, mRegistrationLocationView, mBlacklistView, mAppLockView, myDeviceAdminView;
    private MySettingStylesView mDisplayStyleView, mDisplayPositionView;
    private ComponentName mDeviceAdminSample;
    private DevicePolicyManager mDPM;
    private String[] mToastStyles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        mDeviceAdminSample = new ComponentName(this, MyDeviceAdminReceiver.class);// context, receiver.class
        mDPM = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
        mToastStyles = new String[]{"Default", "Orange", "Blue", "Grey", "Green"};
        initUI();
    }

    private void initUI() {

        mAutoUpgradeView = findViewById(R.id.sv_auto_upgrade);
        mRegistrationLocationView = findViewById(R.id.sv_registration_locaion);
        mBlacklistView = findViewById(R.id.sv_blacklist);
        mAppLockView = findViewById(R.id.sv_application_lock);
        myDeviceAdminView = findViewById(R.id.sv_my_device_administrator);

        mDisplayStyleView = findViewById(R.id.ss_registration_location_display_style);
        mDisplayPositionView = findViewById(R.id.ss_registration_location_display_position);

        mAutoUpgradeView.setTitleContent("Automatic Updates");
        mAutoUpgradeView.setDescriptionContent("Automatic Updates is closed");
        mAutoUpgradeView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED);
        mAutoUpgradeView.setOnClickListener(this);

        mRegistrationLocationView.setTitleContent("Cellphone number's registration location");
        mRegistrationLocationView.setDescriptionContent("Cellphone number's registration location is closed");
        mRegistrationLocationView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED);
        mRegistrationLocationView.setOnClickListener(this);

        mBlacklistView.setTitleContent("Blacklist");
        mBlacklistView.setDescriptionContent("Blacklist is closed");
        mBlacklistView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED);
        mBlacklistView.setOnClickListener(this);

        mAppLockView.setTitleContent("Application Lock");
        mAppLockView.setDescriptionContent("Application Lock is closed");
        mAppLockView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED);
        mAppLockView.setOnClickListener(this);

        myDeviceAdminView.setTitleContent("My Device Administrator");
        myDeviceAdminView.setDescriptionContent("Device Administrator is closed");
        myDeviceAdminView.setDescriptionStyle(ConstantValues.DESCRIPTION_STYLE_OPEN_CLOSED);
        myDeviceAdminView.setOnClickListener(this);

        mDisplayStyleView.setStyleTitleContent("Telephone number's registration location display styles");
        mDisplayStyleView.setOnClickListener(this);

        mDisplayPositionView.setStyleTitleContent("Cellphone number's registration location display position");
        mDisplayPositionView.setStyleDescriptionTextView("Set the display position of its registration location");
        mDisplayPositionView.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        boolean upgradeFlag = SharePreferencesUtil.getBoolean(this, ConstantValues.AUTO_UPGRADE);
        mAutoUpgradeView.setCheckBoxStatus(upgradeFlag);

        boolean locationFlag = SharePreferencesUtil.getBoolean(this, ConstantValues.NUMBER_REGISTRATION_LOCATION);
        boolean isNumberLocationServiceRunning = ServiceUtil.isRunning(this, "com.project.stephencao.mymobileguardian.service.NumberLocationService");
        mRegistrationLocationView.setCheckBoxStatus(locationFlag && isNumberLocationServiceRunning);

        boolean blacklistFlag = SharePreferencesUtil.getBoolean(this, ConstantValues.BLACKLIST);
        boolean isBlacklistServiceRunning = ServiceUtil.isRunning(this, "com.project.stephencao.mymobileguardian.service.BlacklistService");
        mBlacklistView.setCheckBoxStatus(blacklistFlag && isBlacklistServiceRunning);

        boolean appLockFlag = SharePreferencesUtil.getBoolean(this, ConstantValues.APPLICATION_LOCK);
        boolean isAppLockServiceRunning = ServiceUtil.isRunning(this, "com.project.stephencao.mymobileguardian.service.WatchDogService");
        mAppLockView.setCheckBoxStatus(appLockFlag && isAppLockServiceRunning);

        if (!mDPM.isAdminActive(mDeviceAdminSample)) {
            myDeviceAdminView.setCheckBoxStatus(false);
        } else {
            myDeviceAdminView.setCheckBoxStatus(true);
            myDeviceAdminView.setClickable(false);
        }
        int toastStyles = SharePreferencesUtil.getInteger(this, ConstantValues.TOAST_STYLES);
        switch (toastStyles) {
            case 0: {
                mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[0]);
                SharePreferencesUtil.recordInteger(this, ConstantValues.TOAST_STYLES, 0);
                break;
            }
            case R.drawable.toast_bg_orange: {
                mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[1]);
                break;
            }
            case R.drawable.toast_bg_blue: {
                mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[2]);
                break;
            }
            case R.drawable.toast_bg_grey: {
                mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[3]);
                break;
            }
            case R.drawable.toast_bg_green: {
                mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[4]);
                break;
            }
        }
        super.onResume();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.sv_auto_upgrade: {
                boolean flag = mAutoUpgradeView.setCheckBoxChecked();
                SharePreferencesUtil.recordBoolean(this, ConstantValues.AUTO_UPGRADE, flag);
                break;
            }
            case R.id.sv_registration_locaion: {
                boolean flag = mRegistrationLocationView.setCheckBoxChecked();
                SharePreferencesUtil.recordBoolean(this, ConstantValues.NUMBER_REGISTRATION_LOCATION, flag);
                if (flag) {
                    startService(new Intent(getApplicationContext(), NumberLocationService.class));
                } else {
                    // stop service
                    stopService(new Intent(getApplicationContext(), NumberLocationService.class));
                }
                break;
            }
            case R.id.sv_blacklist: {
                boolean flag = mBlacklistView.setCheckBoxChecked();
                SharePreferencesUtil.recordBoolean(this, ConstantValues.BLACKLIST, flag);
                if (flag) {
                    startService(new Intent(getApplicationContext(), BlacklistService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), BlacklistService.class));
                }
                break;
            }
            case R.id.sv_application_lock: {
                boolean flag = mAppLockView.setCheckBoxChecked();
                SharePreferencesUtil.recordBoolean(this, ConstantValues.APPLICATION_LOCK, flag);
                if (flag) {
                    startService(new Intent(getApplicationContext(), WatchDogService.class));
                } else {
                    stopService(new Intent(getApplicationContext(), WatchDogService.class));
                }
                break;
            }
            case R.id.sv_my_device_administrator: {
                if (!mDPM.isAdminActive(mDeviceAdminSample)) {
                    Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
                    intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, mDeviceAdminSample);
                    intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
                            "device administrator");
                    startActivity(intent);
                }
                break;
            }
            case R.id.ss_registration_location_display_style: {
                showToastStylesDialog();
                break;
            }
            case R.id.ss_registration_location_display_position: {
                startActivity(new Intent(this, ToastIncomingCallLocationActivity.class));
                break;
            }
        }

    }

    private void showToastStylesDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.star);
        builder.setTitle("Please Select a style");
        builder.setSingleChoiceItems(mToastStyles, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0: {
                        SharePreferencesUtil.recordInteger(SettingsActivity.this, ConstantValues.TOAST_STYLES, 0);
                        dialog.dismiss();
                        mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[0]);
                        break;
                    }
                    case 1: {
                        SharePreferencesUtil.recordInteger(SettingsActivity.this, ConstantValues.TOAST_STYLES, R.drawable.toast_bg_orange);
                        dialog.dismiss();
                        mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[1]);
                        break;
                    }
                    case 2: {
                        SharePreferencesUtil.recordInteger(SettingsActivity.this, ConstantValues.TOAST_STYLES, R.drawable.toast_bg_blue);
                        dialog.dismiss();
                        mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[2]);
                        break;
                    }
                    case 3: {
                        SharePreferencesUtil.recordInteger(SettingsActivity.this, ConstantValues.TOAST_STYLES, R.drawable.toast_bg_grey);
                        dialog.dismiss();
                        mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[3]);
                        break;
                    }
                    case 4: {
                        SharePreferencesUtil.recordInteger(SettingsActivity.this, ConstantValues.TOAST_STYLES, R.drawable.toast_bg_green);
                        dialog.dismiss();
                        mDisplayStyleView.setStyleDescriptionTextView(mToastStyles[4]);
                        break;
                    }
                }

            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }
}
